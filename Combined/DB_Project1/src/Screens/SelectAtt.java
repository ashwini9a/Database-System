package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.JButton;

public class SelectAtt extends JFrame {

	private JTable table;
	private String[] columnNames;
	private ArrayList<String> selectedAtts;

	/**
	 * Create the application.
	 */
	public SelectAtt(String tableName) {

		this.setBounds(100, 100, 500, 340);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		setTitle(tableName);

		JSONParser parser = new JSONParser();
		try {
			Object obj = parser.parse(new FileReader("Data/MetaData/" + tableName + ".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");

			columnNames = new String[] {"Column Name", "include?"};

			table = new JTable();
			table.setModel(new DefaultTableModel(new Object[][] {}, columnNames) {
				@Override
				public boolean isCellEditable(int row, int col) {
					if (col == 1)
						return true;
					else
						return false;
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					if (columnIndex == 1)
						return Boolean.class;
					return String.class;
				}
			});

			DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

			for (int i = 0; i < headers.size(); i++) {
				Object[] data = new Object[2];

				Object temp = parser.parse(headers.get(i).toString());
				JSONObject currJson = (JSONObject) temp;
				System.out.println("Test!!222!" + currJson.toString());
				data[0] = currJson.get("Column Name");
				data[1] = Boolean.FALSE;
				tableModel.addRow(data);
			}

			table.setFillsViewportHeight(true);
			JScrollPane scrollPane = new JScrollPane(table);
			scrollPane.setBounds(6, 6, 468, 100);
			scrollPane.setViewportView(table);
			scrollPane.setPreferredSize(new Dimension(468, 100));
			this.getContentPane().add(scrollPane);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JButton btnAll = new JButton("Project All Attributes");
		btnAll.setBounds(312, 233, 162, 30);
		btnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ProjectionRecords display = new ProjectionRecords();
				display.projectRecords(tableName);
			}
		});

		getContentPane().add(btnAll);

		JButton btnSelected = new JButton("Project Select Attributes");
		btnSelected.setBounds(117, 234, 187, 29);
		btnSelected.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectedAtts = new ArrayList<String>();
				// DefaultTableModel dm = (DefaultTableModel) table.getModel();
				for (int i = 0; i < table.getRowCount(); i++) {
					Boolean isChecked = Boolean.valueOf(table.getValueAt(i, 1).toString());

					if (isChecked) {
						// this may be cause problem later because 3 is fixed
						// number
						selectedAtts.add((String) table.getValueAt(i, 0));
					} else {
						//
					}
				}
				if (selectedAtts.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please select at least one attribute!", "Error", JOptionPane.ERROR_MESSAGE);
				} else {
					ProjectionRecords display = new ProjectionRecords();
					display.projectRecords(tableName, selectedAtts);
				}
			}
		});

		getContentPane().add(btnSelected);

		this.setVisible(true);
	}
}