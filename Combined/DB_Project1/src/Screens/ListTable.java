package Screens;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ListTable extends JFrame {

	private JPanel contentPane;
	private final JButton btnDisplayRecords = new JButton("Display Records");

	/**
	 * Create the frame.
	 */

	public ListTable(){

		setTitle("List of Tables");
		setBounds(100, 100, 558, 275);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JTable table = new JTable(){

		public void tableChanged(TableModelEvent e) {
			super.tableChanged(e);
				repaint();
			}

		};

		table.setFont(new Font("Perpetua", Font.PLAIN, 13));
		table.setModel(new DefaultTableModel(new Object[][] {},new String[] {"Table Name"}));
		table.setBackground(Color.WHITE);

		int rowCount = GlobalData.allTables.size();
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();

		for(String tableName: GlobalData.allTables){

			System.out.println("TableName:"+tableName);
			Object [] data = {tableName};
			tableModel.addRow(data);

		}

		//table.setPreferredScrollableViewportSize(new Dimension(400, 30));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(37, 5, 468, 100);
		scrollPane.setViewportView(table);
		scrollPane.setPreferredSize(new Dimension(468,100));
		contentPane.add(scrollPane);

		btnDisplayRecords.setBounds(379, 116, 128, 25);
		btnDisplayRecords.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnDisplayRecords.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel)table.getModel();
				int rowIndex = table.getSelectedRow();
				String tableName = GlobalData.allTables.get(rowIndex);
				System.out.println("Table to display:"+tableName);
				DisplayRecords display = new DisplayRecords();
				display.displayRecords(tableName);				
			}
		});	

		contentPane.add(btnDisplayRecords);		

		JButton btnNewButton = new JButton("Delete Table");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				//delete table
				DefaultTableModel dm = (DefaultTableModel)table.getModel();
				int rowIndex = table.getSelectedRow();
				if(rowIndex == -1){
					JOptionPane.showMessageDialog(null, "Please select a table to delete", "Error", JOptionPane.ERROR_MESSAGE);	
				}else{
					
					String tableName = GlobalData.allTables.get(rowIndex);
					
					try {
						GlobalData.deleteTableFile(tableName);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					dm.removeRow(rowIndex);
					//GlobalData.allTables.remove(rowIndex); 
				}
			}
		});

		btnNewButton.setBounds(245, 116, 113, 25);
		contentPane.add(btnNewButton);		
		
		JButton btnSelectAttributes = new JButton("Select Attributes");
		btnSelectAttributes.setBounds(103, 116, 131, 25);
		btnSelectAttributes.setFont(new Font("Perpetua", Font.PLAIN, 13));
		btnSelectAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int rowIndex = table.getSelectedRow();
				String tableName = GlobalData.allTables.get(rowIndex);
				System.out.println(tableName + rowIndex);
				SelectAtt display = new SelectAtt(tableName);
			}
		});

		contentPane.add(btnSelectAttributes);
	}	

}



