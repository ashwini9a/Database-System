package Screens;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JTextArea;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Home extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
					frame.setVisible(true);
					GlobalData.initTableArray();
					GlobalData.initprimaryKey();

					// newly added
					GlobalData.initTableJSonArray();
					GlobalData.initAttTableMap();
					GlobalData.initAttBTreeIndex();
				} catch (Exception e) {
					e.printStackTrace();
				}
				// test();
			}
		});
	}

	protected static void test() {
		JSONArray table_a = GlobalData.tableJSonArray.get("A");
		for (int i = 0; i < table_a.size(); i++) {
			System.out.println(table_a.get(i).toString());
		}

		JSONArray table_b = GlobalData.tableJSonArray.get("B");
		for (int i = 0; i < table_b.size(); i++) {
			System.out.println(table_b.get(i).toString());
		}

		try {
			// GlobalData.addAttBTreeIndex("A", "m");
			// GlobalData.addAttBTreeIndex("B", "x");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONParser parser = new JSONParser();
		BPlusTreeIndexing t1 = (BPlusTreeIndexing) GlobalData.AttBTreeIndex.get("m");
		BPlusTreeIndexing t2 = (BPlusTreeIndexing) GlobalData.AttBTreeIndex.get("x");

		t1.printbtree();
		JSONObject currJson = (JSONObject) t1.search((long) 1);
		System.out.println("Test tree retrival: " + currJson.get("o"));

		System.out.println("Range test:");
		JSONArray range = t1.qBptree("m", ">=", (long) 1);
		for (int i = 0; i < range.size(); i++) {
			System.out.println(range.get(i).toString());
		}
		System.out.println("key number:" + t1.getTotalKeyNumbers());
		System.out.println("tuple number:" + table_a.size());

		range = t1.qBptree("m", ">", (long) 5);
		for (int i = 0; i < range.size(); i++) {
			System.out.println(range.get(i).toString());
		}

		JSONObject testjobj = GlobalUtil.concat2jobj((JSONObject) GlobalData.tableJSonArray.get("A").get(2),
				(JSONObject) GlobalData.tableJSonArray.get("B").get(2));
		System.out.println("concat test:" + GlobalData.tableJSonArray.get("A").get(2));
		System.out.println("concat test:" + testjobj.toString());

		JSONArray jointest = BPlusTreeIndexing.qBptree("A", "m", "=", "B", "x");
		for (int i = 0; i < jointest.size(); i++) {
			System.out.println(jointest.get(i).toString());
		}
		System.out.println("concat test:" + GlobalData.AttTableMap.get("x"));

		System.out.println("concat test:" + GlobalData.AttTableMap.get("m"));
		System.out.println("type test:" + GlobalUtil.GetAttType("m"));

	}

	/**
	 * Create the frame.
	 */
	public Home() {
		setTitle("SQL");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 792, 342);
		getContentPane().setLayout(null);

		JLabel lblSqlQuery = new JLabel("SQL Query : ");
		lblSqlQuery.setBounds(10, 44, 116, 24);
		lblSqlQuery.setFont(new Font("Times New Roman", Font.BOLD, 20));
		getContentPane().add(lblSqlQuery);

		JTextArea textArea = new JTextArea();
		textArea.setBounds(135, 30, 631, 174);

		textArea.setFont(new Font("Times New Roman", Font.PLAIN, 15));

		getContentPane().add(textArea);
		JButton btnCreate = new JButton("Create Table");
		btnCreate.setBounds(449, 232, 138, 31);
		btnCreate.setFont(new Font("Times New Roman", Font.BOLD, 11));
		btnCreate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {

					CreateTable frame = new CreateTable();
					frame.setTitle("Create Table");
					frame.setVisible(true);

				} catch (Exception e1) {
					e1.printStackTrace();
				}

			}
		});
		getContentPane().add(btnCreate);
		JButton btnOk = new JButton("OK");
		btnOk.setBounds(597, 232, 75, 31);
		btnOk.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String query = textArea.getText();
				if (query.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please enter a Query", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					QValidation.validateQ1(query);
				}
			}
		});
		btnOk.setFont(new Font("Times New Roman", Font.BOLD, 12));
		getContentPane().add(btnOk);

		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(682, 232, 84, 31);
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				textArea.setText("");
			}
		});
		btnClear.setFont(new Font("Times New Roman", Font.BOLD, 12));
		getContentPane().add(btnClear);

		JButton btnLoadComplaintdb = new JButton("Load complaint.db");
		btnLoadComplaintdb.setFont(new Font("Times New Roman", Font.BOLD, 12));

		btnLoadComplaintdb.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				LoadData ld = new LoadData();

				// GlobalData.allTables.add("complaints");
				try {
					// aGlobalData.updateTableFile();
					GlobalData.addTableJSonArray("complaints");
					GlobalData.addAttTableMap("complaints");
					GlobalData.addAttBTreeIndex("complaints", "id");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});

		btnLoadComplaintdb.setBounds(289, 233, 150, 29);
		getContentPane().add(btnLoadComplaintdb);
		
		JButton btnNewButton = new JButton("List Index");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					ListBtree frame = new ListBtree();
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(97, 234, 117, 29);
		getContentPane().add(btnNewButton);
		contentPane = new JPanel(new GridLayout(5, 5));

	}
}
