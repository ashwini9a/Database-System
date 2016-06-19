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

	public ListTable() {

		setTitle("List of Tables");
		setBounds(100, 100, 558, 275);
		contentPane = new JPanel();
		contentPane.setLayout(null);
		setContentPane(contentPane);

		JTable table = new JTable() {

			public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
				repaint();
			}

		};

		table.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Table Name" }) {
			@Override
			public boolean isCellEditable(int row, int col) {
				return false;
			}
		});

		// it seems make no change
		// table.setRowSelectionAllowed(true);
		table.setBackground(Color.WHITE);

		DefaultTableModel tableModel = (DefaultTableModel) table.getModel();

		for (String tableName : GlobalData.allTables) {

			Object[] data = { tableName };
			tableModel.addRow(data);

		}

		table.setFillsViewportHeight(true);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(37, 5, 468, 100);
		scrollPane.setViewportView(table);
		scrollPane.setPreferredSize(new Dimension(468, 100));
		contentPane.add(scrollPane);

		btnDisplayRecords.setBounds(379, 116, 128, 25);
		btnDisplayRecords.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnDisplayRecords.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int[] selection = table.getSelectedRows();
				if (selection.length == 1) {
					String tableName = GlobalData.allTables.get(selection[0]);
					DisplayRecords display = new DisplayRecords(tableName, false);
					display.displayRecords(tableName);
				} else if (selection.length > 1) {
					JOptionPane.showMessageDialog(null, "Please select one table at a time", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Please select a table to display", "Error",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		contentPane.add(btnDisplayRecords);

		JButton btnNewButton = new JButton("Delete Table");
		btnNewButton.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// delete table
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int[] selection = table.getSelectedRows();
				if (selection.length <= 0) {
					JOptionPane.showMessageDialog(null, "Please select a table to delete", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int len = selection.length;
					for (int i = selection[len - 1]; i >= 0; i--) {
						String tableName = GlobalData.allTables.get(i);
						try {
							GlobalData.deleteTableFile(tableName);
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						dm.removeRow(i);
					}
				}
			}
		});

		btnNewButton.setBounds(245, 116, 113, 25);
		contentPane.add(btnNewButton);

		JButton btnSelectAttributes = new JButton("Projection");
		btnSelectAttributes.setBounds(128, 116, 94, 25);
		btnSelectAttributes.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		btnSelectAttributes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultTableModel dm = (DefaultTableModel) table.getModel();
				int[] selection = table.getSelectedRows();
				if (selection.length == 1) {
					String tableName = GlobalData.allTables.get(selection[0]);
					SelectAtt display = new SelectAtt(tableName);
				} else if (selection.length > 1) {
					JOptionPane.showMessageDialog(null, "Please select one table at a time", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					JOptionPane.showMessageDialog(null, "Please select a table to project", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		contentPane.add(btnSelectAttributes);
	}
}
