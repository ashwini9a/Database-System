package Screens;

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

	public static boolean validateTableName(String name){

		boolean tableExists = false;

		//check if tableName exists
		Iterator< String> itr = GlobalData.allTables.iterator();
		
		
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
		Iterator< String> itr = GlobalData.allTables.iterator();
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

			//check if each column in the sql exists in the columnList
			for(String colName : columnNamesList){		
				boolean found = false;
				for(String tableCol : columnNames){

					if(tableCol.equalsIgnoreCase(colName)){
						found = true;
						break;
					}	  
				}
				
				if(!found){					
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

	
	
	public static HashMap<String,String> getTableColumnMap(List<WhereClause> conditions, String tableName){
		
		HashMap<String,String> tableNameMap = new HashMap<String,String>();
		
		FileReader f1;
		Iterator< String> itr = GlobalData.allTables.iterator();
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

			//check if each column in the sql exists in the columnList
			for(WhereClause where : conditions){		
				boolean found = false;				
				String colName = where.attribute1;
				for(String tableCol : columnNames){

					if(tableCol.equalsIgnoreCase(colName)){
						found = true;
						tableNameMap.put(colName, tableCol);
						break;
					}	  
				}
				
				if(!found){					
					isValidColName = false;
					break;					
				}else 
					continue;		
			}
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return tableNameMap;
	}
	


	public static boolean validateColumnNames(String colnm, String tableName){

		FileReader f1;
		Iterator< String> itr = GlobalData.allTables.iterator();
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
		Iterator< String> itr = GlobalData.allTables.iterator();
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
				
				//System.out.println("Table primary key: "+GlobalData.tablePrimaryKeyMap.get(tableName.toLowerCase()));
				
				if(!columnName.toLowerCase().equalsIgnoreCase(GlobalData.tablePrimaryKeyMap.get(tableName.toLowerCase())))
				     columnDetailMap.put(columnName, dataType);

			}
			
	      //check if each in the sql exists in the columnList
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return columnDetailMap;

	}

	public static String getDataType(String tableName, String att)
	{
		FileReader f1;
		String dataType = null;
		Iterator< String> itr = GlobalData.allTables.iterator();
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

				if(att.equalsIgnoreCase(((String) temp1.get("Column Name"))))
				{
					dataType = (String)temp1.get("Data Type");
					break;
				}
			}
			
			//check if each in the sql exists in the columnList
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
		return dataType;

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

    public static HashMap<String,String> fetchColumnDataType(String tableName){
    	
    	FileReader f1;
		Iterator< String> itr = GlobalData.allTables.iterator();
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

				if(!columnName.toLowerCase().equalsIgnoreCase(GlobalData.tablePrimaryKeyMap.get(tableName.toLowerCase())))
				     columnDetailMap.put(columnName.toLowerCase(),dataType);
			}
			
	    
		}catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return columnDetailMap;
    		
    }
	
}
