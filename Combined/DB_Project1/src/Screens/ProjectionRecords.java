package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ProjectionRecords {

	private JFrame frame;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void projectRecords(String tableName, ArrayList<String> SelectedAttributes) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ProjectionRecords window = new ProjectionRecords();
					System.out.println(tableName);

					// populate records in the table
					window.frame.setTitle(tableName);
					window.populateRecords("Data/Records/" + tableName + ".json", SelectedAttributes);

					// display all the records of this table

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void projectRecords(String tableName) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

					ProjectionRecords window = new ProjectionRecords();
					System.out.println(tableName);

					// populate records in the table
					window.frame.setTitle(tableName);
					window.populateRecords("Data/Records/" + tableName + ".json");

					// display all the records of this table

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void populateRecords(String name, ArrayList<String> SelectedAttributes) {
		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader(name));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("Records");

			if (headers.size() == 0) {
				JOptionPane.showMessageDialog(null, "No Records to Display", "Warning",
						JOptionPane.INFORMATION_MESSAGE);
			} else {

				String[] columnNames = new String[SelectedAttributes.size()];
				SelectedAttributes.toArray(columnNames);
				
				table = new JTable();
				table.setModel(new DefaultTableModel(new Object[][] {}, columnNames) {

					@Override
					public boolean isCellEditable(int row, int col) {

						return false;
					}

				});

				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

				for (int i = 0; i < headers.size(); i++) {

					Object temp = parser.parse(headers.get(i).toString());
					JSONObject currJson = (JSONObject) temp;

					Object[] data = new Object[columnNames.length];
					int index = 0;
					for (String key : columnNames) {
						data[index] = currJson.get(key);
						index++;
					}
					tableModel.addRow(data);
				}

				table.setFillsViewportHeight(true);
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBounds(37, 5, 468, 100);
				scrollPane.setViewportView(table);
				scrollPane.setPreferredSize(new Dimension(468, 100));
				frame.getContentPane().add(scrollPane);

				this.frame.setVisible(true);

				// frame.pack();
			}

		} catch (FileNotFoundException e) {
			// System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void populateRecords(String name) {
		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader(name));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("Records");
			System.out.println(headers.toString());

			if (headers.size() == 0) {

				JOptionPane.showMessageDialog(null, "No Records to Display", "Warning",
						JOptionPane.INFORMATION_MESSAGE);

			} else {

				Object temp = parser.parse(headers.get(0).toString());
				JSONObject currJson = (JSONObject) temp;
				System.out.println("test!!!!" + headers.get(0).toString());
				Set<String> keys = currJson.keySet();
				String[] columnNames = keys.toArray(new String[keys.size()]);
				for (int i = 0; i < columnNames.length; i++)
					System.out.println(i+"th:" + columnNames[i]);

				table = new JTable();

				table.setModel(new DefaultTableModel(new Object[][] {}, columnNames) {

					@Override
					public boolean isCellEditable(int row, int col) {
						return false;
					}
				});

				DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

				for (int i = 0; i < headers.size(); i++) {
					temp = parser.parse(headers.get(i).toString());
					currJson = (JSONObject) temp;

					Object[] data = new Object[columnNames.length];
					int index = 0;
					for (String key : keys) {
						data[index] = currJson.get(key);
						index++;
					}
					tableModel.addRow(data);
				}

				table.setFillsViewportHeight(true);
				JScrollPane scrollPane = new JScrollPane(table);
				scrollPane.setBounds(37, 5, 468, 100);
				scrollPane.setViewportView(table);
				scrollPane.setPreferredSize(new Dimension(468, 100));
				frame.getContentPane().add(scrollPane);

				this.frame.setVisible(true);

				// frame.pack();
			}

		} catch (FileNotFoundException e) {
			// System.out.println("FileNotFoundException");
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public ProjectionRecords() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 738, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	}
}