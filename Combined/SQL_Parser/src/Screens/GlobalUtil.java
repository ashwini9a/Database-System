package Screens;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class GlobalUtil {
	
	
	static List<String> allTables;
	public static void initTableArray() throws Exception
	{
		 allTables = new ArrayList<String>();
		 String fileName = "Data/TableIndex.txt";

	        // This will reference one line at a time
	     String line = null;

	     try {
	            // FileReader reads text files in the default encoding.
	         FileReader fileReader = new FileReader(fileName);

	            // Always wrap FileReader in BufferedReader.
	            BufferedReader bufferedReader = 
	                new BufferedReader(fileReader);

	            while((line = bufferedReader.readLine()) != null) {
	            	allTables.add(line);
	            	System.out.println(line);
	            }   

	            // Always close files.
	            bufferedReader.close();         
	        }
	        catch(FileNotFoundException ex) {
	            System.out.println(
	                "Unable to open file '" + 
	                fileName + "'");                
	        }
	}

	public static boolean validateTableName(String name){
		
		boolean tableExists = false;
		
		//check if tableName exists
		Iterator< String> itr = allTables.iterator();
		while(itr.hasNext())
		{
			if(name.equalsIgnoreCase(itr.next()))
			{
				tableExists=true;
			}
		}
		
		return tableExists;
		
	}
	
	
	public static boolean validateColumnNames(List<String> columnNamesList, String tableName){
		
		FileReader f1;
		Iterator< String> itr = allTables.iterator();
		while(itr.hasNext())
		{
			String tnm= itr.next();
			if(tableName.equalsIgnoreCase(tnm))
			{
				tableName = tnm;
				break;
			}
		}
		JSONParser parser = new JSONParser();
		ArrayList<String> columnNames = new ArrayList<String>();
		boolean isValidColName = true;
		
		try {
			
			f1 = new FileReader("Data/MetaData/" +tableName+ ".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");
		
	    	for(int i = 0 ; i < headers.size(); i++){
	    		
	    		Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;			
				
				String columnName = (String) temp1.get("Column Name");
				columnNames.add(columnName);
								    	       	
	    	}
	    	
	    	//check if each in the sql exists in the columnList
			
			for(String colName : columnNamesList){					
				if(!columnNames.contains(colName)){
					
					isValidColName = false;
					break;
					
				}else
					continue;					
			}
	    	
	    	
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return isValidColName;
				
	}
public static boolean validateColumnNames(String colnm, String tableName){
		
		FileReader f1;
		Iterator< String> itr = allTables.iterator();
		while(itr.hasNext())
		{
			String tnm= itr.next();
			if(tableName.equalsIgnoreCase(tnm))
			{
				tableName = tnm;
				break;
			}
		}
		JSONParser parser = new JSONParser();
		ArrayList<String> columnNames = new ArrayList<String>();
		
		
		try {
			
			f1 = new FileReader("Data/MetaData/" +tableName+ ".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");
		
	    	for(int i = 0 ; i < headers.size(); i++){
	    		
	    		Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;			
				
				String columnName = (String) temp1.get("Column Name");
				columnNames.add(columnName);
								    	       	
	    	}
	    	
	    	//check if each in the sql exists in the columnList
			
								
				if(!columnNames.contains(colnm)){
					
					return false;
					
				}
				return true;
			
	    	
	    	
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
				
		
				
	}
		
	public static HashMap<String,String> fetchColumnNames(String tableName){
		FileReader f1;
		Iterator< String> itr = allTables.iterator();
		while(itr.hasNext())
		{
			String tnm= itr.next();
			if(tableName.equalsIgnoreCase(tnm))
			{
				tableName = tnm;
				break;
			}
		}
		JSONParser parser = new JSONParser();
		HashMap<String,String> columnDetailMap = new HashMap<String,String>();
		
		
		try {
			
			f1 = new FileReader("Data/MetaData/" +tableName+ ".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");
		
	    	for(int i = 0 ; i < headers.size(); i++){
	    		
	    		Object temp = parser.parse(headers.get(i).toString());
				JSONObject temp1 = (JSONObject) temp;			
				
				String columnName = (String) temp1.get("Column Name");
				String dataType = (String)temp1.get("Data Type");
				columnDetailMap.put(columnName, dataType);
								    	       	
	    	}
	    	
	    	//check if each in the sql exists in the columnList
			
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return columnDetailMap;
		
	}
	
	
	
	
	public static boolean validateDataType(String dataType, String val){
		
		switch(dataType)
		{
		case "INT":
			try{
			int value = Integer.parseInt(val);
			}
			catch(Exception e)
			{
				
				return false;
			}
			break;
		case "VARCHAR":
			break;
		case "FLOAT":
			try{
				float value = Float.parseFloat(val);
				}
				catch(Exception e)
				{
					return false;
				}
			break;

		}
		
	   return true;	
	}
	
	

}
