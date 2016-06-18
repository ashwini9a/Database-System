package Screens;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class OperationFunctions {
	static String[] colnmI;

	public static void insertInTable(String tnm, JSONObject json) {

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("Data/Records/" + tnm + ".json"));
			JSONObject json1 = (JSONObject) obj;
			System.out.println(json1.toJSONString());

			try {
				JSONArray headers = (JSONArray) json1.get("Records");
				headers.add(json);
				json1.put("Records", headers);
			} catch (ClassCastException e) {
				JSONArray JA = new JSONArray();
				JA.add(json);
				json1.put("Records", JA);
			}
			
			
			System.out.println(json1.toJSONString());

			File file = new File("Data/Records/" + tnm + ".json");
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

	
	public static void updateInTable(String tnm, JSONObject json) {

	}
	
	
	public static void deleteInTable(String tnm, JSONObject json) {

	}


	public static void searchInTable(String tnm, JSONObject json) {

	}
}
