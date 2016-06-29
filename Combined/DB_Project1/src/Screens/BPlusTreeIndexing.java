package Screens;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.Dimension;
import java.io.FileNotFoundException;
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
	Tuple tuple = new Tuple();
	TupleBrowser browser;
	BTree tree;
	Properties props;

	static String DATABASE = "db";
	static String BTREE_NAME = "btree_name";

	// static String[] db = { "Greenspan, Alan", "Williams-Byrd, Julie",
	// "Picasso, Pablo", "Stallman, Richard",
	// "Fort, Paul", "Søndergaard, Ole", "Schwarzenegger, Arnold", "Dulkinys,
	// Susanna" };

	// static String[] occupations = { "Federal Reserve Board Chairman",
	// "Engineer", "Painter", "Programmer", "Poet",
	// "Typographer", "Actor", "Designer" };

	static String PREFIX = "S";

	/**
	 * Example main entrypoint.
	 */
	public BPlusTreeIndexing(JSONArray headers, String att) {

		props = new Properties();
		JSONParser parser = new JSONParser();

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

			recman = RecordManagerFactory.createRecordManager(DATABASE, props);
			

			// try to reload an existing B+Tree
			recid = recman.getNamedObject(BTREE_NAME);
//			if (recid != 0) {
//				tree = BTree.load(recman, recid);
//				System.out.println("Reloaded existing BTree with " + tree.size() + " tuples.");
//			} else {
				// create a new B+Tree data structure and use a StringComparator
				// to order the records based on people's name.
				tree = BTree.createInstance(recman, new StringComparator());
				recman.setNamedObject(BTREE_NAME, tree.getRecid());
				System.out.println("Created a new empty BTree");
			//			}

			// insert people with their respective occupation
			System.out.println();
			for (int i = 0; i < db.length; i++) {
				System.out.println("Insert: " + db[i]);
				tree.insert(db[i], tupleaddress[i], false);
			}

			// make the data persistent in the database
			recman.commit();

			// // show list of people with their occupation
			// System.out.println();
			// System.out.println("Key Occupation ");
			// System.out.println("------------------ ------------------");

			// traverse people in order
			browser = tree.browse();
			while (browser.getNext(tuple)) {
				print(tuple);
			}

			// traverse people in reverse order
			System.out.println();
			System.out.println("Reverse order:");
			browser = tree.browse(null); // position browser at end of the list

			while (browser.getPrevious(tuple)) {
				print(tuple);
			}

			// display people whose name start with PREFIX range
			System.out.println();
			System.out.println("All people whose name start with '" + PREFIX + "':");

			browser = tree.browse(PREFIX);
			while (browser.getNext(tuple)) {
				String key = (String) tuple.getKey();
				if (key.startsWith(PREFIX)) {
					print(tuple);
				} else {
					break;
				}
			}

		} catch (Exception except) {
			except.printStackTrace();
		}
	}

	/**
	 * Print a Tuple containing a ( Person, Occupation ) pair.
	 */
	static void print(Tuple tuple) {
		// this cannot be printed since our value is address
		
		Object obj = tuple.getKey();
		System.out.println(obj);
		//below is original code
		// String person = (String) tuple.getKey();
		// String occupation = (String) tuple.getValue();
		// System.out.println(pad(person, 25) + occupation);
	}

	/**
	 * Pad a string with spaces on the right.
	 *
	 * @param str
	 *            String to add spaces
	 * @param width
	 *            Width of string after padding
	 */
	static String pad(String str, int width) {
		StringBuffer buf = new StringBuffer(str);
		int space = width - buf.length();
		while (space-- > 0) {
			buf.append(' ');
		}
		return buf.toString();
	}

}