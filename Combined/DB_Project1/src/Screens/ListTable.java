package cse.buffalo.edu.swing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ListTable extends JFrame {

	private JPanel contentPane;
	private final JButton btnDisplayRecords = new JButton("Display Records");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ListTable frame = new ListTable();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ListTable() {
		
		setTitle("List of Tables");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 558, 275);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		JTable table = new JTable();
		table.setFont(new Font("Perpetua", Font.PLAIN, 13));
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {"No", "Table Name"}
		   ){
				
			@Override
			public boolean isCellEditable(int row, int col){
				
				if(col > 0){
					 return false;
				}
				
				return true;
			}
				
		});
        
		table.setBackground(Color.WHITE);
		//String[] columnNames  = {"No","Table Name"};
		
		int rowCount = GlobalData.allTables.size();
		
        GlobalData.allTables.add("Moviefsssfdsgdss");
        GlobalData.allTables.add("Cast");
        GlobalData.allTables.add("Cast1");
        GlobalData.allTables.add("Cast2");
        GlobalData.allTables.add("Cast3");
        GlobalData.allTables.add("Cast1");
        GlobalData.allTables.add("Cast2");
        GlobalData.allTables.add("Cast3");
        
		DefaultTableModel tableModel = (DefaultTableModel)table.getModel();

		int count = 1;
		
		for(String tableName: GlobalData.allTables){
			
			System.out.println("TableName:"+tableName);
			//JRadioButton radioButton = new JRadioButton("count"+count);
			Object [] data = {count,tableName};
			count++;
			tableModel.addRow(data);
		
		}
		
		
		//System.out.println(tableModel.getRowCount());
		//contentPane.setLayout(null);
		
		//table.setPreferredScrollableViewportSize(new Dimension(400, 30));
		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(37, 5, 468, 100);
		scrollPane.setViewportView(table);
		scrollPane.setPreferredSize(new Dimension(468,100));
		contentPane.add(scrollPane);
				
		btnDisplayRecords.setBounds(376, 116, 131, 25);
		btnDisplayRecords.setFont(new Font("Perpetua", Font.PLAIN, 13));
		
		btnDisplayRecords.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel)table.getModel();
				int rowIndex = table.getSelectedRow();
				String tableName = GlobalData.allTables.get(rowIndex);
				System.out.println(tableName);
				DisplayRecords display = new DisplayRecords();
				display.displayRecords(tableName);
				
			}
		});
		
		contentPane.add(btnDisplayRecords);
		
	}
	

}

	

