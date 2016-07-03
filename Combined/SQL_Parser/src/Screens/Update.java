package Screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import bptree.BTree;

public class Update {

	String tableName;
	boolean conditionsPresent;
	HashMap<String,String> columnDataMap = new HashMap<String,String>();
	List<WhereClause> whereConditions = new ArrayList<WhereClause>();
	String conditionOp = null;
	HashMap<String,String> tableColumnMap;
	boolean tablePrimaryKeyInWhere = false;


	public boolean parse(String[] tokens, String sql) {

		int setIndex = (sql.indexOf("SET") != -1) ? sql.indexOf("SET") : sql.indexOf("set");	

		boolean whereClausePresent = false;


		int whereIndex = (sql.indexOf("WHERE") != -1) ? sql.indexOf("WHERE") : sql.indexOf("where");

		if(setIndex == -1){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing SET clause", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}


		if(tokens.length > 3 && !"SET".equalsIgnoreCase(tokens[2])){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,position of SET Clause is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
			return false;

		}

		/// check if tableName exists		
		String tableName  =  sql.substring(0, setIndex).trim().substring(6).trim();


		if("".equalsIgnoreCase(tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Table Name is missing", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}else{
			this.tableName = tableName;
		}

		//check if tableName exists in db
		if(!GlobalUtil.validateTableName(this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: Table Name does not exists", "Error", JOptionPane.ERROR_MESSAGE);
			return false;	
		}


		// between tableName and SET clause

		String btwTableSet = sql.substring(sql.indexOf(tableName)+tableName.length(),setIndex).trim();

		System.out.println(btwTableSet);

		if(!"".equalsIgnoreCase(btwTableSet)){		
			JOptionPane.showMessageDialog(null, "Invalid Syntax", "Error", JOptionPane.ERROR_MESSAGE);
			return false;			
		}


		//everything good till here

		if(tokens.length < 4){		

			JOptionPane.showMessageDialog(null, "Invalid Syntax: Missing columnData to set", "Error", JOptionPane.ERROR_MESSAGE);
			return false;

		}else{

			//columnData is available

			// check if whereClause is present

			if(whereIndex != -1){
				whereClausePresent = true;
			}

			if(whereClausePresent){

				this.conditionsPresent = true;
				//check if columnData available
				String btwSetWhere = sql.substring(setIndex,whereIndex).trim().substring(3).trim();

				if("".equals(btwSetWhere)){
					JOptionPane.showMessageDialog(null, "Invalid Syntax, no columdata to set", "Error", JOptionPane.ERROR_MESSAGE);
					return false;						
				}

			}

			// fetch columnData
			boolean isValid = fetchColumnData(tokens[3]);

			if(!isValid){
				JOptionPane.showMessageDialog(null, "Invalid Syntax,column Data to set is not correct", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	
			}

		}

		//check if columnName matches
		Set<String> colNames = this.columnDataMap.keySet();

		if(!GlobalUtil.validateColumnNames(new ArrayList<String>(colNames),this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Statement,column name does not exists in table", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}	


		if(whereClausePresent){

			// fetch where conditions			
			String condition = sql.substring(whereIndex,sql.length()).trim().substring(5).trim();

			if("".equals(condition)){
				JOptionPane.showMessageDialog(null, "Invalid Syntax, Missing conditions after where clause", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	

			}else if(condition.startsWith("and") || condition.startsWith("or") || condition.endsWith("and") || condition.endsWith("or")){		    	  
				JOptionPane.showMessageDialog(null, "Invalid Syntax, Missing conditions before/after AND or OR", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	

			}

			boolean isValid = fetchWhereClause(condition);

			if(!isValid){
				JOptionPane.showMessageDialog(null, "Invalid syntax, format of where clause is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	
			}

		}

		/*// if syntax is fine, check for semantics

		if(!GlobalUtil.validateTableName(this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: No such table exists", "Error", JOptionPane.ERROR_MESSAGE);
			return false;

		}*/

		/* 
		 * validate whereClause colName exists or not
		 */

		boolean validColNames = validateWhereClauseColNames();

		if(!validColNames){
			JOptionPane.showMessageDialog(null, "Invalid Column Name", "Error", JOptionPane.ERROR_MESSAGE);
			return false;			
		}

		// initialize the table column map
		initializeTheTableMap();

		//syntax and semantic correct
		updateRecordInDb();

		return true;

	}


	public void initializeTheTableMap(){

		List<String> columnsInSql = new ArrayList<String>();

		if(this.conditionsPresent){

			for(WhereClause clause: this.whereConditions){				
				columnsInSql.add(clause.attribute1);				
			}
		}

		for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

			String columnName = entryMap.getKey();
			columnsInSql.add(columnName);		

		}

		// fetch table column names
		this.tableColumnMap = GlobalUtil.getTableColumnNameMap(columnsInSql, tableName);

	}



	public boolean fetchColumnData(String sql){

		int equalIndex = sql.indexOf("=",0);

		if(equalIndex == -1){
			return false;
		}


		if(sql.startsWith(",") || sql.endsWith(",")){
			return false;
		}

		// split based on comma
		String [] columnData = sql.split(",\\s*");

		for(String columnSet : columnData){

			String [] colArr = columnSet.split("=\\s*");

			if(colArr.length < 2){
				return false;
			}

			String colName = colArr[0];
			String colVal = colArr[1];

			if("".equals(colName.trim()) || "".equals(colVal.trim()))
				return false;

			System.out.println("colName: "+colName);
			System.out.println("colVal:"+colVal);

			// validate that colVal is inside quotes

			if(!validateColVal(colVal))
				return false;

			this.columnDataMap.put(colName, colVal);

		}

		return true;


	}

	private boolean validateColVal(String val){

		if(!val.startsWith("'") || !val.endsWith("'"))
			return false;

		return true;

	}


	public boolean fetchWhereClause(String sql){

		if(sql.indexOf("=",0) == -1 && sql.indexOf("<",0) == -1 && sql.indexOf(">",0) == -1){			 
			return false;
		}

		System.out.println("inside fetchWhereClause");

		String [] firstConditionArr = sql.split("\\s+");

		boolean conditionFlag = true;
		boolean andOrFlag = false;

		for(String condition : firstConditionArr){

			System.out.println(condition);

			if(conditionFlag){	

				char operator;	
				String opt;

				if(condition.indexOf("=") != -1){			  
					operator = '=';
					opt = "=";
				}else if(condition.indexOf(">") != -1){
					operator = '>';	
					opt = ">";
				}else if(condition.indexOf("<") != -1){
					operator = '<'; 	
					opt = "<";
				}else{
					return false;
				}

				// split based on operator
				String [] columData = condition.split(opt);

				if(columData.length < 2)
					return false;

				String colName = columData[0];
				String colVal = columData[1];

				if("".equals(colName.trim()) || "".equals(colVal.trim()))
					return false;

				System.out.println("colName:"+colName);
				System.out.println("colVal:"+colVal);

				if(colName.equalsIgnoreCase(GlobalData.tablePrimaryKeyMap.get(this.tableName.toLowerCase())))
					this.tablePrimaryKeyInWhere = true;

				//conditions.put(colName.trim(), colVal.trim());
				WhereClause where = new WhereClause();
				where.attribute1 = colName;
				where.attribute2 = colVal;
				where.operation = operator;

				this.whereConditions.add(where);

				conditionFlag = false;
				andOrFlag = true;

			}else if(andOrFlag){

				//System.out.println("condition:"+condition);

				if(condition.equals("and"))
					this.conditionOp = "and";
				else if(condition.equals("or"))
					this.conditionOp = "or";		
				else{	  
					return false;
				}

				andOrFlag = false;
				conditionFlag = true;

			}			
		}

		System.out.println("condition op:"+this.conditionOp);

		return true;
	}


	public boolean validateWhereClauseColNames(){

		List<String> colNames =  new ArrayList<String>();

		for(WhereClause clause : this.whereConditions){			
			colNames.add(clause.attribute1);
		}

		if(!GlobalUtil.validateColumnNames(colNames, this.tableName))
			return false;

		return true;

	}


	public boolean updateRecordInDb(){

		if(tablePrimaryKeyInWhere){

			// use b-tree to fetch Json objects
			// if only single where clause then fetch records using B-tree
			if(conditionOp == null){

				// get the json objects from  B-tree
				WhereClause whereClause = this.whereConditions.get(0);			    
				BPlusTreeIndexing  bplusTree = GlobalData.AttBTreeIndex.get(whereClause.attribute1);	
				String operation = whereClause.operation + "";
				JSONArray jsonArray = bplusTree.qBptree(whereClause.attribute1,operation, whereClause.attribute2);

				updateJSONArray(jsonArray);
				
			}


		}

	else{

		JSONParser parser = new JSONParser();

		try {

			FileReader f1 = new FileReader("Data/Records/" + this.tableName + ".json");
			Object obj = parser.parse(f1);
			JSONObject json1 = (JSONObject) obj;
			///System.out.println(json1.toJSONString());

			try {

				JSONArray headers = (JSONArray) json1.get("Records");

				//int rindex=-1;

				int size = headers.size();

				for(int i=0; i <size;i++)
				{

					JSONObject temp = (JSONObject) headers.get(i);
					//boolean allConditionsMatch = true;

					if(this.conditionsPresent){

						if(this.conditionsPresent && conditionOp == null){

							System.out.println("no and/or: single where condition");

							boolean result = checkIfConditionMatch(temp);

							if(result){

								for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

									String columnName = entryMap.getKey();
									String columnValue = entryMap.getValue();

									String tableColumnName = tableColumnMap.get(columnName);

									// remove single quotes
									columnValue = columnValue.substring(1, columnValue.length()-1);

									temp.remove(tableColumnName);
									temp.put(tableColumnName, columnValue);

								}	

							}


						}else if(this.conditionsPresent && "And".equalsIgnoreCase(conditionOp)){

							System.out.println("conditionOp: "+this.conditionOp);			    	
							boolean result = allConditionsMatch(temp);

							if(result){
								// change here
								for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

									String columnName = entryMap.getKey();
									String columnValue = entryMap.getValue();

									String tableColumnName = tableColumnMap.get(columnName);

									// remove single quotes
									columnValue = columnValue.substring(1, columnValue.length()-1);

									temp.remove(tableColumnName);
									temp.put(tableColumnName, columnValue);

								}
							}	

						}else if(this.conditionsPresent && "Or".equalsIgnoreCase(conditionOp)){

							System.out.println("conditionOp: "+this.conditionOp);			    	
							boolean result = eitherConditionsMatch(temp);

							if(result){		
								// change here

								for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

									String columnName = entryMap.getKey();
									String columnValue = entryMap.getValue();

									String tableColumnName = tableColumnMap.get(columnName);

									// remove single quotes
									columnValue = columnValue.substring(1, columnValue.length()-1);

									temp.remove(tableColumnName);
									temp.put(tableColumnName, columnValue);

								}

							}

						}else{						
							// no where clause present						
							System.out.println("no where clause present");

							for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

								String columnName = entryMap.getKey();
								String columnValue = entryMap.getValue();

								String tableColumnName = tableColumnMap.get(columnName);

								// remove single quotes
								columnValue = columnValue.substring(1, columnValue.length()-1);

								temp.remove(tableColumnName);
								temp.put(tableColumnName, columnValue);

							}
						}				
					}
				}

				json1.put("Records", headers);

			} catch (ClassCastException e) {

				e.printStackTrace();

			}

			f1.close();

			System.out.println(json1.toJSONString());

			File file = new File("Data/Records/" + this.tableName + ".json");
			FileWriter fw = null;
			BufferedWriter bw = null;

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(json1.toJSONString());
			bw.flush();
			bw.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	return true;
}


     private void updateJSONArray(JSONArray jsonArray){
    	 
    	 
		// TODO Auto-generated method stub
		int size = jsonArray.size();
		
		for(int i=0; i < size; i++){
			
			JSONObject temp = (JSONObject) jsonArray.get(i);
			
			for(Map.Entry<String, String> entryMap : columnDataMap.entrySet()){

				String columnName = entryMap.getKey();
				String columnValue = entryMap.getValue();

				String tableColumnName = tableColumnMap.get(columnName);

				// remove single quotes
				columnValue = columnValue.substring(1, columnValue.length()-1);

				temp.remove(tableColumnName);
				temp.put(tableColumnName, columnValue);

			}

		}
			
		// update records in file
		///JSONParser parser = new JSONParser();

		FileReader f1;
		try {
			
			//f1 = new FileReader("Data/Records/" + this.tableName + ".json");
			//Object obj = parser.parse(f1);
			
			
			JSONArray maintable = GlobalData.tableJSonArray.get(this.tableName);
			
			
			JSONObject newJson = new JSONObject();
			newJson.put("Records", maintable);
			
			File file = new File("Data/Records/" + this.tableName + ".json");
			FileWriter fw = null;
			BufferedWriter bw = null;

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(newJson.toJSONString());
			bw.flush();
			bw.close();
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 	
    	 
	}


public boolean eitherConditionsMatch(JSONObject temp){

	//iterate through each condition and check
	boolean match = false;

	for(WhereClause whereClause: this.whereConditions){

		String colName = whereClause.attribute1;
		String colVal = whereClause.attribute2;

		// get table columnName
		String tableColName = tableColumnMap.get(colName);					 

		Object value = temp.get(tableColName);			 
		char operator = whereClause.operation;

		if(value != null && !(value instanceof String)){

			if(operator == '>'){

				System.out.println("Inside > ");     							 
				// System.out.println("Table value: "+value.toString());

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) > 0){
					match = true;
					break;
				}	

			}else if(operator == '<'){

				System.out.println("Inside < ");

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) < 0){
					match = true;
					break;
				}
			}else if(operator == '=') {

				System.out.println("Inside ="); 

				System.out.println("col val: "+colVal);

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString()); 

				if(searchVal.compareTo(actualVal) == 0){
					match = true;
					break;								
				}		
			}
		}else{							 

			if(value != null && colVal != null){

				colVal = colVal.substring(1, colVal.length()-1);

				System.out.println("after substring: "+colVal);

				if(colVal.equals(value.toString())){

					System.out.println(colVal+" matches "+value);
					match = true;
					break;

				}						 
			}									 
		}				
	}

	return match;
}


public boolean allConditionsMatch(JSONObject temp){

	boolean allConditionsMatch = true;

	System.out.println("inside allConditionsMatch");

	for(WhereClause whereClause: this.whereConditions){

		String colName = whereClause.attribute1;
		String colVal = whereClause.attribute2;

		System.out.println("colName:" + colName);

		System.out.println("colVal:" + colVal);

		// get table columnName
		String tableColName = tableColumnMap.get(colName);					 

		Object value = temp.get(tableColName);			 
		char operator = whereClause.operation;

		if(value != null && !(value instanceof String)){

			if(operator == '>'){

				System.out.println("Inside > ");     							 
				System.out.println("Table value: "+value.toString());

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) <= 0){
					allConditionsMatch = false;
					break;
				}	

			}else if(operator == '<'){

				System.out.println("Inside < ");

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) >= 0){
					allConditionsMatch = false;
					break;
				}

			}else if(operator == '=') {

				System.out.println("Inside ="); 

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString()); 

				if(searchVal.compareTo(actualVal) != 0){
					allConditionsMatch = false;
					break;								
				}		
			}
		}else{		

			if(value != null && colVal != null){	

				colVal = colVal.substring(1, colVal.length()-1);						 
				System.out.println("after substring: "+colVal);

				if(colVal.equals(value.toString())){
					System.out.println(colVal+" matches "+value);
					continue;
				}else{

					allConditionsMatch = false;
					break;
				}

			}else{
				allConditionsMatch = false;
				break;
			}												 
		}				
	}

	return allConditionsMatch;

}


public boolean checkIfConditionMatch(JSONObject temp){

	boolean match = true;

	System.out.println("inside checkIfConditionMatch");

	for(WhereClause whereClause: this.whereConditions){

		String colName = whereClause.attribute1;
		String colVal = whereClause.attribute2;

		//get table columnName
		String tableColName = tableColumnMap.get(colName);					 

		Object value = temp.get(tableColName);			 
		char operator = whereClause.operation;

		if(value != null && !(value instanceof String)){

			if(operator == '>'){

				System.out.println("Inside > ");     							 
				System.out.println("Table value: "+value.toString());

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) <= 0){
					match = false;
					break;
				}	

			}else if(operator == '<'){

				System.out.println("Inside < ");

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString());

				if(actualVal.compareTo(searchVal) >= 0){
					match = false;
					break;
				}
			}else if(operator == '=') {

				System.out.println("Inside ="); 

				BigDecimal searchVal = new BigDecimal(colVal);
				BigDecimal actualVal = new BigDecimal(value.toString()); 

				if(searchVal.compareTo(actualVal) != 0){
					match = false;
					break;								
				}else{
					System.out.println("match = true for "+searchVal+","+actualVal);						 
				}
			}
		}else{		

			if(value != null && colVal != null){

				colVal = colVal.substring(1, colVal.length()-1);							 
				System.out.println("after substring: "+colVal);

				if(colVal.equals(value.toString())){							 
					match = true;
					break;
				}else{

					match = false;
					break;
				}

			}else{
				match = false;
				break;
			}												 
		}				
	}		

	return match;

}

}
