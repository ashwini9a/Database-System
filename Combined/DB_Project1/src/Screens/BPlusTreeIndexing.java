package Screens;

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

import java.util.Properties;
import jdbm.RecordManager;
import jdbm.RecordManagerFactory;

import jdbm.helper.Tuple;
import jdbm.helper.TupleBrowser;
import jdbm.helper.StringComparator;

import jdbm.btree.BTree;

/**
 * Famous People example.
 * <p>
 * Demonstrates the use of B+Tree data structure to manage a list of people and
 * their occupation. The example covers insertion, ordered traversal, reverse
 * traversal and range lookup of records.
 *
 * @author <a href="mailto:boisvert@intalio.com">Alex Boisvert</a>
 * @version $Id: FamousPeople.java,v 1.6 2003/10/21 15:32:02 boisvert Exp $
 */
public class BPlusTreeIndexing {
	RecordManager recman;
	long recid;
	Properties props = new Properties();
	TupleBrowser browser;
	Tuple tuple = new Tuple();
	BTree tree = null;
	String TABLE_NAME;
	String BTREE_NAME;
	JSONParser parser = new JSONParser();

	public BPlusTreeIndexing() {

	}

	// public BPlusTreeIndexing(JSONArray headers, String table_name, String
	// att) {
	public BTree GetBPlusTreeIndexing(JSONArray headers, String table_name, String att) {
		TABLE_NAME = table_name;
		BTREE_NAME = table_name + "_" + att;
		try {
			// open database and setup an object cache
			Object[] db = new Object[headers.size()];
			for (int i = 0; i < headers.size(); i++) {
				JSONObject currJson = (JSONObject) parser.parse(headers.get(i).toString());
				// !!!!!!!!!!!!!!!!!!
				// This cast may have problem
				db[i] = currJson.get(att);
				System.out.println("test!!" + db[i]);
			}

			JSONObject[] tupleaddress = new JSONObject[headers.size()];
			for (int i = 0; i < headers.size(); i++) {
				JSONObject currJson = (JSONObject) parser.parse(headers.get(i).toString());
				// !!!!!!!!!!!!!!!!!!
				// This cast may have problem
				tupleaddress[i] = (JSONObject) headers.get(i);
			}

			recman = RecordManagerFactory.createRecordManager(TABLE_NAME, props);
			tree = BTree.createInstance(recman, new StringComparator());
			recman.setNamedObject(att, tree.getRecid());

			System.out.println("Created a new empty BTree");
			System.out.println();
			for (int i = 0; i < db.length; i++) {
				System.out.println("Insert: " + db[i]);
				tree.insert(db[i], tupleaddress[i], false);
			}

		} catch (Exception except) {
			except.printStackTrace();
		}
		return tree;
	}

	public void SaveBTree(JSONArray headers, BTree tree, String tableName, String att) {
		try {
			TupleBrowser browser = tree.browse();
			Tuple tuple = new Tuple();
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
			while (browser.getNext(tuple)) {
				Object key1 = tuple.getKey();
				int i = headers.indexOf((JSONObject) tuple.getValue());
				JSONObject json = new JSONObject();
				json.put(att, key1);
				json.put("row #", i);

				records += json.toString() + ",";
				;
				System.out.println("save test!!!" + "position:" + "i=" + i + " key=" + key1);
			}
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public BTree LoadBTree(JSONArray headers, String tableName, String att) {
		try {
			// load the index file
			FileReader f1 = new FileReader("Data/Index/" + tableName + "_" + att + ".json");
			Object obj = parser.parse(f1);
			JSONObject json = (JSONObject) obj;
			JSONArray index = (JSONArray) json.get("index");

			if (index.size() != headers.size()) {
				JOptionPane.showMessageDialog(null, "records number from index file and data file not match!", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

			// read the key value of index file
			Object[] db = new Object[index.size()];
			for (int i = 0; i < index.size(); i++) {
				JSONObject currJson = (JSONObject) parser.parse(index.get(i).toString());
				// !!!!!!!!!!!!!!!!!!
				// This cast may have problem
				db[i] = currJson.get(att);
				System.out.println("test!!" + db[i]);
			}

			// get the tuple address from the row number in the index file
			JSONObject[] tupleaddress = new JSONObject[index.size()];
			for (int i = 0; i < index.size(); i++) {
				JSONObject currJson = (JSONObject) parser.parse(index.get(i).toString());
				//we need this weird cast to convert long to int
				Long temp = (Long) currJson.get("row #");
				int j = temp.intValue();
				// !!!!!!!!!!!!!!!!!!
				// This cast may have problem
				tupleaddress[i] = (JSONObject) headers.get(j);
			}

			recman = RecordManagerFactory.createRecordManager(TABLE_NAME, props);
			tree = BTree.createInstance(recman, new StringComparator());
			recman.setNamedObject(att, tree.getRecid());

			System.out.println("Created a new empty BTree");
			System.out.println();
			for (int i = 0; i < db.length; i++) {
				System.out.println("Insert: " + db[i]);
				tree.insert(db[i], tupleaddress[i], false);
			}

		} catch (Exception except) {
			except.printStackTrace();
		}
		return tree;
	}
}