package Screens;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import bptree.*;

public class displayJTree extends JFrame {
	protected final static int BTREEORDER = 5;

	public displayJTree(String att) {
		this.setBounds(100, 100, 600, 800);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.getContentPane().setLayout(null);
		this.setTitle(att);

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("BTree");
		createNodes(top, att);
		JTree tree = new JTree(top);

		JScrollPane scrollPane = new JScrollPane(tree);
		scrollPane.setBounds(6, 6, 588, 766);
		getContentPane().add(scrollPane);

		this.setVisible(true);

	}

	private void addAllChildNode(DefaultMutableTreeNode jnode, BTreeNode node) {
		DefaultMutableTreeNode temp1 = null;
		DefaultMutableTreeNode temp2 = null;

		if (!node.IsLeaf()) {
			for (int i = 0; i < node.getKeyCount()+1; i++) {
				temp1 = new DefaultMutableTreeNode(node.getChild(i).printRange());
				jnode.add(temp1);
				addAllChildNode(temp1, node.getChild(i));
			}
		} else {
			for (int i = 0; i < node.getKeyCount(); i++) {
				temp1 = new DefaultMutableTreeNode(node.getNodeKey(i));
				temp2 = new DefaultMutableTreeNode(node.getLeafValue(i));
				temp1.add(temp2);
				jnode.add(temp1);
			}
		}
	}

	private void createNodes(DefaultMutableTreeNode top, String att) {
		BPlusTreeIndexing btree = GlobalData.AttBTreeIndex.get(att);
		DefaultMutableTreeNode innernode = null;
		DefaultMutableTreeNode leafnode = null;
		DefaultMutableTreeNode temp1 = null;
		DefaultMutableTreeNode temp2 = null;

		BTreeNode node = btree.getRoot();
		innernode = new DefaultMutableTreeNode(node.printRange());
		addAllChildNode(innernode, node);
		// while (node.getKeyCount() > 0) {
		// if (!node.IsLeaf()) {
		// for (int i = 0; i < node.getKeyCount(); i++) {
		// temp1 = new DefaultMutableTreeNode(node.getChild(i).printRange());
		// inndernode.add(temp1);
		// }
		// } else {
		// for (int i = 0; i < node.getKeyCount(); i++) {
		// temp1 = new DefaultMutableTreeNode(node.getNodeKey(i));
		// temp2 = new DefaultMutableTreeNode(node.getLeafValue(i));
		// temp1.add(temp2);
		// inndernode.add(temp1);
		// }
		// break;
		// }
		// }

		top.add(innernode);

		// // original Tutorial
		// book = new DefaultMutableTreeNode(
		// new BookInfo("The Java Tutorial: A Short Course on the Basics",
		// "tutorial.html"));
		// category.add(book);
		//
		// category = new DefaultMutableTreeNode("Books for Java Implementers");
		// top.add(category);
		//
		// // VM
		// book = new DefaultMutableTreeNode(new BookInfo("The Java Virtual
		// Machine Specification", "vm.html"));
		// category.add(book);

	}
}
