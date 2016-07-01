package Screens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

public class Update {
	
	String tableName;
	//List<String> columns;
	//List<String> values;
	boolean conditionsPresent;
	HashMap<String,String> columnDataMap = new HashMap<String,String>();
	
	public boolean parse(String[] tokens, String sql) {

		int setIndex = (sql.indexOf("SET") != -1) ? sql.indexOf("SET") : sql.indexOf("set");	
		
		boolean whereClausePresent = false;
		
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
	
		
		// between tableName and SET clause
		
		String btwTableSet = sql.substring(sql.indexOf(tableName)+tableName.length(),setIndex).trim();
		
		System.out.println(btwTableSet);
		
		if(!"".equalsIgnoreCase(btwTableSet)){		
			JOptionPane.showMessageDialog(null, "Invalid Syntax", "Error", JOptionPane.ERROR_MESSAGE);
			return false;			
		}
		
		//check if tableName exists in db
		if(!GlobalUtil.validateTableName(this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: Table Name does not exists", "Error", JOptionPane.ERROR_MESSAGE);
			return false;	
		}
		
		
		//everything good till here
		
		if(tokens.length < 4){		
			
			JOptionPane.showMessageDialog(null, "Invalid Syntax: Missing columnData to set", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}else{
			
			//columnData is available
			
			// check if whereClause is present
			
			int whereIndex = (sql.indexOf("WHERE") != -1) ? sql.indexOf("WHERE") : sql.indexOf("where");	
			
			if(whereIndex != -1){
				whereClausePresent = true;
			}
			
			if(whereClausePresent){
				
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
			
		return true;
			
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

	
	
	

}
