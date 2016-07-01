package Screens;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				
		}
			
		
		//if syntax is correct, check if tableName and columnName exists
		if(!GlobalUtil.validateTableName(this.tableName)){
			JOptionPane.showMessageDialog(null, "Invalid Syntax: No such table exists", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
			
		}
		
	    ///if everything is good, delete records	
		deleteRecords();
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
				
				for(Map.Entry<String, String> entryMap : conditions.entrySet()){
					
					 String dataVal = (String)temp.get(entryMap.getKey());
					 if(dataVal.equals(entryMap.getValue())){
						 headers.remove(i);
					 }					 					
				}
							
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
