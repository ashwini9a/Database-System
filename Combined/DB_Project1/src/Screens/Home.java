package Screens;


import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;


public class Home extends JFrame {

	private JPanel contentPane;
	public static void main(String[] args) throws Exception {
		GlobalData.initTableArray();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home("Welcome to Database Systems!");
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
	public Home(String S) {
		super(S);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JButton btnCreate = new JButton("Create Table");
		btnCreate.setBounds(160, 74, 126, 23);
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
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
		contentPane.setLayout(null);
		
		JLabel lblMenu = new JLabel("   MENU ");
		lblMenu.setFont(new Font("Times New Roman", Font.BOLD, 19));
		lblMenu.setHorizontalAlignment(SwingConstants.CENTER);
		lblMenu.setBackground(Color.LIGHT_GRAY);
		lblMenu.setBounds(169, 28, 106, 14);
		contentPane.add(lblMenu);
		contentPane.add(btnCreate);
		
		JButton btnListselect = new JButton("Browse Tables");
		btnListselect.setBounds(160, 132, 126, 23);
		btnListselect.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					ListTable frame = new ListTable();
					frame.setVisible(true);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		contentPane.add(btnListselect);
		
		JButton btnOperations = new JButton(" Tuple Operations");
		btnOperations.setBounds(153, 190, 142, 23);
		btnOperations.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					
					Ops frame1 = new Ops("Operations");
					frame1.setVisible(true);
					
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		
		contentPane.add(btnOperations);
		
		/*JButton btnDelete = new JButton("Delete");
		btnDelete.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				TableName Tfm = new TableName("Delete Table");
				Tfm.setVisible(true);
			}
		});
		GridBagConstraints gbc_btnDelete = new GridBagConstraints();
		gbc_btnDelete.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnDelete.insets = new Insets(0, 0, 5, 5);
		gbc_btnDelete.gridx = 2;
		gbc_btnDelete.gridy = 4;
		contentPane.add(btnDelete, gbc_btnDelete);*/
	}

}
