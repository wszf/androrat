package gui.panel;

import gui.UserGUI;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EtchedBorder;

import utils.MyFile;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JSplitPane;
import javax.swing.JTextField;

public class FileTreePanel extends JPanel {

	private JTree tree;
	private DefaultMutableTreeNode trunk;
	private DefaultTreeModel treeModel;
	
	private JLabel lblValname;
	private JLabel lblValsize;
	private JLabel lblValhidden;
	private JLabel lblValaccess;
	private JLabel lblVallastmodif;
	private JButton btnDownload;

	private UserGUI gui;
	private HashMap<String, MyFile> fileMap = new HashMap<String, MyFile>();
	private String selectedAbsolutePath;
	private String selectedName;
	private JTextField txtDir;

	/**
	 * Create the panel.
	 */
	public FileTreePanel(UserGUI gui) {
		this.gui = gui;
		trunk = new DefaultMutableTreeNode("sdcard");

		JLabel lblLeftclicToDownload = new JLabel(
				"Left-clic to download a file :");

		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(0.9);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								groupLayout
										.createParallelGroup(Alignment.LEADING)
										.addComponent(splitPane,
												GroupLayout.DEFAULT_SIZE, 533,
												Short.MAX_VALUE)
										.addComponent(lblLeftclicToDownload))
						.addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.TRAILING).addGroup(
				Alignment.LEADING,
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(lblLeftclicToDownload)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 377,
								Short.MAX_VALUE).addContainerGap()));

		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setBorder(new TitledBorder(null, "Informations",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton btnTreeRequest = new JButton("Get FileTree");
		btnTreeRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonRequestTree();
			}
		});

		btnDownload = new JButton("Download File");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonDownload();
			}
		});
		btnDownload.setEnabled(false);

		JLabel lblName = new JLabel("Name :");

		lblValname = new JLabel("val_name");

		JLabel lblSize = new JLabel("Size :");

		lblValsize = new JLabel("val_size");

		JLabel lblHidden = new JLabel("Hidden :");

		lblValhidden = new JLabel("val_hidden");

		JLabel lblAccess = new JLabel("Access :");

		lblValaccess = new JLabel("val_access");

		JLabel lblLastModification = new JLabel("Last modification :");

		lblVallastmodif = new JLabel("val_last_modif");
		
		txtDir = new JTextField();
		txtDir.setText("download/");
		txtDir.setColumns(10);
		
		JLabel lblDownloadDirectory = new JLabel("Download directory :");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnDownload, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
						.addComponent(btnTreeRequest, GroupLayout.DEFAULT_SIZE, 158, Short.MAX_VALUE)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblName)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValname))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblSize)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValsize))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblHidden)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValhidden))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblAccess)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValaccess))
						.addComponent(lblLastModification)
						.addComponent(lblVallastmodif)
						.addComponent(txtDir, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
						.addComponent(lblDownloadDirectory))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblName)
						.addComponent(lblValname))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblSize)
						.addComponent(lblValsize))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblHidden)
						.addComponent(lblValhidden))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAccess)
						.addComponent(lblValaccess))
					.addGap(32)
					.addComponent(lblLastModification)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblVallastmodif)
					.addPreferredGap(ComponentPlacement.RELATED, 77, Short.MAX_VALUE)
					.addComponent(lblDownloadDirectory)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(txtDir, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnDownload)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnTreeRequest)
					.addContainerGap())
		);
		panel.setLayout(gl_panel);

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		tree = new JTree();
		scrollPane.setViewportView(tree);
		tree.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireClickNode(e);
			}
		});

		treeModel = new DefaultTreeModel(trunk);
		tree.setModel(treeModel);
		setLayout(groupLayout);

		this.init();
	}

	public void init() {
		MyFile f0 = new MyFile(new File("src"));
		MyFile f1 = new MyFile(new File("src/handler"));
		MyFile f2 = new MyFile(new File("src/server/ClientHandler.java"));
		MyFile f3 = new MyFile(new File("src/server"));
		MyFile f4 = new MyFile(new File("src/server/Server.java"));
		ArrayList<MyFile> listFile = new ArrayList<MyFile>();
		listFile.add(f0);
		listFile.add(f1);
		listFile.add(f2);
		listFile.add(f3);
		listFile.add(f4);

		this.updateFileTree(listFile);
	}

	public void updateFileTree(ArrayList<MyFile> fileList) {

		File dir = null;
		/*
		for (MyFile file : fileList) {
			if(file.getPath().equals("src")) dir = file.getFile();
		}
		*/
		//dir = fileList.get(0).getFile();
		
		treeModel = new DefaultTreeModel(this.addNodes(null, fileList.get(0)));
		tree.setModel(treeModel);
		repaint();
	}
	
	private DefaultMutableTreeNode addNodes(TreePath parentPath, MyFile cur) {
		
		DefaultMutableTreeNode curNode = new DefaultMutableTreeNode(cur.getName());
		TreePath path = new TreePath(curNode.getPath());
		
		if(parentPath != null) {
			parentPath = parentPath.pathByAddingChild(cur.getName());
			fileMap.put(parentPath.toString(), cur);
		} else {
			fileMap.put(path.toString(), cur);
			parentPath = new TreePath(curNode.getPath());
		}
		
		if(cur.getList() != null) {
			for(MyFile child : cur.getList()) {
				curNode.add(addNodes(parentPath, child));
			}
		}
		
		return curNode;
	}


	private void fireClickNode(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			TreePath path = tree.getPathForLocation(e.getX(), e.getY());
			if (path != null) {
				/*
				Object[] sPath = path.getPath();
				String completePath = "";
				for(int i = 0; i < sPath.length; i++) completePath += sPath[i]+"";
				*/
				
				MyFile f = fileMap.get(path.toString());
				if(f != null) {
					selectedAbsolutePath = f.getPath();
					selectedName = f.getName();
					lblValname.setText(f.getName());
					lblValhidden.setText(""+f.isHidden());
					lblVallastmodif.setText(""+(new Date(f.getLastModif())));
					
					String sLength = "";
					String temp = String.valueOf(f.getLength());
					if(f.getLength() > 1024) sLength = String.valueOf(f.getLength()).substring(0, temp.length() - 3) + "Kb";
					else if(f.getLength() > 1024000) sLength = String.valueOf(f.getLength()).substring(0, temp.length() - 6) + "Mb";
					else if(f.getLength() > 1024000000) sLength = String.valueOf(f.getLength()).substring(0, temp.length() - 9) + "Tb";
					else sLength = temp + " bytes";
					lblValsize.setText(sLength);
					
					String sAccess = "";
					if(f.isR() && f.isW()) sAccess = "read & write";
					else if(f.isR()) sAccess = "read";
					else if(f.isW()) sAccess = "write";
					lblValaccess.setText(sAccess);
					
					btnDownload.setEnabled(true);
				} else {
					System.out.println("MyFile null => anormal");
					selectedAbsolutePath = null;
					lblValname.setText("n/a");
					lblValhidden.setText("n/a");
					lblVallastmodif.setText("n/a");
					lblValsize.setText("n/a");
					lblValaccess.setText("n/a");
					btnDownload.setEnabled(false);
				}

			} else {
				selectedAbsolutePath = null;
				lblValname.setText("n/a");
				lblValhidden.setText("n/a");
				lblVallastmodif.setText("n/a");
				lblValsize.setText("n/a");
				lblValaccess.setText("n/a");
				btnDownload.setEnabled(false);
			}
		}
	}
	
	private void fireButtonDownload() {
		gui.fireFileDownload(selectedAbsolutePath, txtDir.getText(), selectedName);
	}

	private void fireButtonRequestTree() {
		gui.fireTreeFile();
	}
}
