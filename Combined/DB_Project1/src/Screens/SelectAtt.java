package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
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

public class SelectAtt extends JFrame {

	private JTable table;

	/**
	 * Create the application.
	 */
	public SelectAtt(String tableName){
		
		this.setBounds(100, 100, 500, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		setTitle(tableName);

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("Data/MetaData/" + tableName + ".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");
			//System.out.println(headers.toString());

			Object temp = parser.parse(headers.get(0).toString());
			JSONObject currJson = (JSONObject) temp;
			Set<String> keys = currJson.keySet();
			String[] tempArr = keys.toArray(new String[keys.size()]);

			String[] columnNames = new String[tempArr.length + 1];
			for (int i = 0; i < tempArr.length; i++){
				   columnNames[i] = tempArr[i];
			}
			
			columnNames[tempArr.length] = "include?";
			
			table = new JTable();
			table.setModel(new DefaultTableModel(new Object[][] {}, columnNames) {
				@Override
				public boolean isCellEditable(int row, int col) {
					if (col == columnNames.length - 1)
						return true;
					else
						return false;
				}
				
				public Class<?> getColumnClass(int columnIndex)
		         {
					if(columnIndex == 3)
						 return Boolean.class;
					
					return String.class;
		             
		         }
			});

			//table.getColumn("include?").setCellEditor(new DefaultCellEditor(new JCheckBox()));
			
			//table.setEditingColumn(columnNames.length);
			//TableColumnModel tcm = table.getColumnModel();
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
				
				data[tempArr.length] =  Boolean.FALSE;
				
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

		this.setVisible(true);
	}
}