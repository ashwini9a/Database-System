package Screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Delete {	

	String tableName;
	List<String> whereConditions;
	boolean conditionFlag = false;
	HashMap<String,String> conditions = new HashMap<String,String>();
	String conditionOp;
	//List<WhereClause> conditions = new ArrayList<WhereClause>();


	public boolean parse(String sql) {


		// from clause exists
		int fromIndex = (sql.indexOf("FROM") != -1) ? sql.indexOf("FROM") : sql.indexOf("from");

		if(fromIndex == -1){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing FROM clause", "Error", JOptionPane.ERROR_MESSAGE);
			return false;	
		}

		// where clause exists

		int whereIndex = (sql.indexOf("WHERE") != -1) ? sql.indexOf("WHERE") : sql.indexOf("where");

		/*if(whereIndex == -1){			
			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing WHERE clause", "Error", JOptionPane.ERROR_MESSAGE);
			return;	
		}*/


		// check if from is at position 2
		String [] tokens = sql.split("\\s+");

		// is * present

		if(tokens.length > 2 && !tokens[1].equals("*") && !tokens[1].equalsIgnoreCase("from")){

			JOptionPane.showMessageDialog(null, "Invalid Syntax, position of from clause is wrong", "Error", JOptionPane.ERROR_MESSAGE);
			return false;

		}else if(tokens.length > 2 && tokens[2].equals("*") && !tokens[3].equalsIgnoreCase("from")){

			JOptionPane.showMessageDialog(null, "Invalid Syntax, position of from clause is wrong", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		// DELETE FROM correct

		// check if table name is present

		String tableName = "";

		if(whereIndex != -1){			
			tableName = sql.substring(fromIndex, whereIndex).trim().substring(4).trim();				
		}else if(tokens.length > 2){

			tableName = tokens[2];

		}else{		
			JOptionPane.showMessageDialog(null, "Invalid Syntax,table name missing", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}


		if("".equals(tableName.trim())){	

			JOptionPane.showMessageDialog(null, "Invalid Syntax,table name missing", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		this.tableName = tableName;

		//delete from table correct

		//check the where clause

		if(tokens.length > 3 && !"where".equalsIgnoreCase(tokens[3])){	

			JOptionPane.showMessageDialog(null, "Invalid Syntax,Missing WHERE clause", "Error", JOptionPane.ERROR_MESSAGE);
			return false;	

		}else{

			//get the string after where clause
			String condition = sql.substring(whereIndex,sql.length()).trim().substring(5).trim();

			if("".equals(condition)){

				JOptionPane.showMessageDialog(null, "Invalid Syntax, Missing conditions", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	

			}else if(condition.startsWith("and") || condition.startsWith("or") || condition.endsWith("and") || condition.endsWith("or")){		    	  
				JOptionPane.showMessageDialog(null, "Invalid Syntax, Missing conditions after And or or", "Error", JOptionPane.ERROR_MESSAGE);
				return false;	

			}else{

				boolean isValid = fetchWhereClause(condition);
				
				if(!isValid){
					JOptionPane.showMessageDialog(null, "Invalid syntax, format of where clause is incorrect", "Error", JOptionPane.ERROR_MESSAGE);
					return false;	
				}

			}			
		}


		//if syntax is correct, check if tableName and columnName exists
		/*if(!GlobalUtil.validateTableName(this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: No such table exists", "Error", JOptionPane.ERROR_MESSAGE);
			return false;

		}*/

		///if everything is good, delete records	
		deleteRecords();
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
				  
				  System.out.println("i am here");
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
				  
				  // validate that colVal is inside quotes
				  
				 // if(!validateColVal(colVal))
					//   return false;
				  
				  conditions.put(colName.trim(), colVal.trim());
				 				  
				  conditionFlag = false;
				  andOrFlag = true;
				  
			  }else if(andOrFlag){
				  
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
	
		return true;


	}


	private boolean validateColVal(String val){

		if(!val.startsWith("'") || !val.endsWith("'"))
			return false;

		return true;

	}


	public void deleteRecords(){

		JSONParser parser = new JSONParser();

		try {

			FileReader f1 = new FileReader("Data/Records/" + this.tableName + ".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("Records");

			for (int i = 0; i < headers.size(); i++) {

				JSONObject temp = (JSONObject) parser.parse(headers.get(i).toString());

				// iterate through each condition and check

				/*for(Map.Entry<String, String> entryMap : conditions.entrySet()){

					String dataVal = (String)temp.get(entryMap.getKey());
					if(dataVal.equals(entryMap.getValue())){
						headers.remove(i);
					}					 					
				}*/

			}

			// write the file back to disk
			json.put("Records", headers);
			System.out.println(json.toJSONString());

			File file = new File("Data/Records/" + tableName + ".json");
			FileWriter fw = null;
			BufferedWriter bw = null;
			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			bw.write(json.toJSONString());
			bw.flush();
			fw.close();
			bw.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}		

	}




}
