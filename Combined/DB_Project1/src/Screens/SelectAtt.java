package Screens;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import javax.swing.JFrame;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class SelectAtt extends JFrame {

	private JTable table = new JTable();;

	/**
	 * Create the application.
	 */
	public SelectAtt(String tableName) {
		this.setBounds(100, 100, 500, 300);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);

		setTitle(tableName);

		JSONParser parser = new JSONParser();
		try {

			Object obj = parser.parse(new FileReader("Data/MetaData/" + tableName + ".json"));
			JSONObject json = (JSONObject) obj;
			JSONArray headers = (JSONArray) json.get("headers");
			System.out.println(headers.toString());

			Object temp = parser.parse(headers.get(0).toString());
			JSONObject currJson = (JSONObject) temp;

			Set<String> keys = currJson.keySet();
			String[] columnNames = keys.toArray(new String[keys.size()]);
			//columnNames[keys.size()]="include?";
			
			// table.setBounds(427, 0, -424, 83);

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
				//data[columnNames.length]="include?";
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
