package Screens;

import bptree.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class BPlusTreeIndexing extends BTree {
	JSONParser parser = new JSONParser();

	public BPlusTreeIndexing(String table_name, String att) {
		JSONArray headers = GlobalData.tableJSonArray.get(table_name);
		try {
			for (int i = 0; i < headers.size(); i++) {
				JSONObject currJson;
				currJson = (JSONObject) parser.parse(headers.get(i).toString());
				this.insert((Comparable) currJson.get(att), headers.get(i));
				;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public BTree GetBPlusTreeIndexing(JSONArray headers, String table_name, String att) {
		BTree tree = new BTree();
		return tree;
	}

	public void SaveBTree(JSONArray headers, BTree tree, String tableName, String att) {
		try {

			JSONObject temp;
			File file = new File("Data/Index/" + tableName + "_" + att + ".json");
			FileWriter fw = null;
			BufferedWriter bw = null;

			try {

				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);

			} catch (IOException e1) {

				e1.printStackTrace();
			}

			String records = "{\"index\":[";

			records = records.substring(0, records.length() - 1);
			records += "]}";
			System.out.print(records);

			try {

				fw = new FileWriter(file.getAbsoluteFile());
				bw = new BufferedWriter(fw);

			} catch (IOException e1) {

				e1.printStackTrace();
			}

			try {
				bw.write(records);
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			try {
				bw.flush();
				bw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		} finally {
		}

	}

	// public BTree LoadBTree(JSONArray headers, String tableName, String att) {
	// try {
	// // load the index file
	// FileReader f1 = new FileReader("Data/Index/" + att + ".json");
	// Object obj = parser.parse(f1);
	// JSONObject json = (JSONObject) obj;
	// JSONArray index = (JSONArray) json.get("index");
	//
	// if (index.size() != headers.size()) {
	// JOptionPane.showMessageDialog(null, "records number from index file and
	// data file not match!", "Error",
	// JOptionPane.ERROR_MESSAGE);
	// }
	//
	// // read the key value of index file
	// Object[] db = new Object[index.size()];
	// for (int i = 0; i < index.size(); i++) {
	// JSONObject currJson = (JSONObject) parser.parse(index.get(i).toString());
	// // !!!!!!!!!!!!!!!!!!
	// // This cast may have problem
	// db[i] = currJson.get(att);
	// System.out.println("test!!" + db[i]);
	// }
	//
	// // get the tuple address from the row number in the index file
	// JSONObject[] tupleaddress = new JSONObject[index.size()];
	// for (int i = 0; i < index.size(); i++) {
	// JSONObject currJson = (JSONObject) parser.parse(index.get(i).toString());
	// // we need this weird cast to convert long to int
	// Long temp = (Long) currJson.get("row #");
	// int j = temp.intValue();
	// // !!!!!!!!!!!!!!!!!!
	// // This cast may have problem
	// tupleaddress[i] = (JSONObject) headers.get(j);
	// }
	//
	// recman = RecordManagerFactory.createRecordManager(TABLE_NAME, props);
	// tree = BTree.createInstance(recman, new StringComparator());
	// recman.setNamedObject(att, tree.getRecid());
	//
	// System.out.println("Created a new empty BTree");
	// System.out.println();
	// for (int i = 0; i < db.length; i++) {
	// System.out.println("Insert: " + db[i]);
	// tree.insert(db[i], tupleaddress[i], false);
	// }
	//
	// } catch (Exception except) {
	// except.printStackTrace();
	// }
	// return tree;
	// }
}