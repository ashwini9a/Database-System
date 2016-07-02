package Screens;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class GlobalData{
	
	static List<String> allTables;
	static HashMap<String,String> tablePrimaryKeyMap;
	
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
	
	public static void initprimaryKey() throws Exception
	{
		tablePrimaryKeyMap = new HashMap<>();
		Iterator<String> itr = allTables.iterator();
		JSONParser parser = new JSONParser();
	      while(itr.hasNext()) {
	         String tnm = (itr.next()).toString();
	         FileReader f1=new FileReader("Data/Metadata/"+tnm+".json");
	         Object obj = parser.parse(f1);
				JSONObject json = (JSONObject) obj;
				
				JSONArray headers = (JSONArray) json.get("headers");
				int size= headers.size();
				
				for (int i = 0; i < size; i++) {
					Object temp = parser.parse(headers.get(i).toString());
					JSONObject temp1 = (JSONObject) temp;
					if((boolean)temp1.get("Key"))
					{
						String keynm = (String) temp1.get("Column Name");
						tablePrimaryKeyMap.put(tnm, keynm);
					}
					
					
				}	
	         f1.close();
	      }
	}
	
	
	
	public static void updateTableFile() throws Exception
	{
		File file = new File("Data/TableIndex.txt");
		FileWriter fw = null;
		BufferedWriter bw = null;
		fw = new FileWriter(file.getAbsoluteFile());
		bw = new BufferedWriter(fw);
		String tables="";
		Iterator<String> itr = allTables.iterator();
	      while(itr.hasNext()) {
	         tables+= (itr.next()).toString()+"\n";
	      }
	      System.out.println(tables);
	      bw.write(tables);
	      bw.flush();
	      bw.close();
	      initprimaryKey();
			
			
	}
	
	public static void deleteTableFile(String tnm) throws Exception 
	{
		System.out.println(tnm);
		allTables.remove(tnm);
		updateTableFile();
		File file1 = new File("Data/Metadata/"+tnm+".json");
		boolean f= file1.delete();
		System.out.println(f);
		File file2 = new File("Data/Records/"+tnm+".json");
		
		
		file2.delete();
		initprimaryKey();
	}

}
