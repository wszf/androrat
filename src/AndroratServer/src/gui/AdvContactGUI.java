package gui;

import java.awt.Image;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.EtchedBorder;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextArea;

import utils.Contact;
import javax.swing.JScrollPane;

public class AdvContactGUI extends JFrame {
	
	private JLabel lblValname;
	private JLabel lblValid;
	private JLabel lblValorga;
	private JLabel lblValstatus;
	private JLabel lblValtimes;
	private JLabel lblVallasttime;
	private JLabel lblPicture;
	private JTextArea areaPhones;
	private JTextArea areaAdress;
	private JTextArea areaEmails;
	private JTextArea areaMessaging;
	private JTextArea areaNotes;

	private JPanel contentPane;
	private Contact contact;


	/**
	 * Create the frame.
	 */
	public AdvContactGUI(Contact contact) {
		this.contact = contact;
		
		setVisible(true);
		setLocationRelativeTo(null);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 500, 502);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		
		JLabel lblName = new JLabel("Name :");
		
		lblValname = new JLabel("val_name");
		
		JLabel lblPhoneNumber = new JLabel("Phone number :");
		
		JLabel lblAdress = new JLabel("Adress :");
		
		lblValid = new JLabel("val_id");
		
		JLabel lblId = new JLabel("Id :");
		
		JLabel lblEmails = new JLabel("Emails :");
		
		JLabel lblMessaging = new JLabel("Messaging :");
		
		JLabel lblOrganisation = new JLabel("Organisation :");
		
		lblValorga = new JLabel("val_orga");
		
		JLabel lblOrganisationStatus = new JLabel("Organisation status :");
		
		lblValstatus = new JLabel("val_status");
		
		JLabel lblTimesContacted = new JLabel("Times contacted :");
		
		lblValtimes = new JLabel("val_times");
		
		JLabel lblLastTimeContacted = new JLabel("Last time contacted :");
		
		lblVallasttime = new JLabel("val_last_time");
		
		JLabel lblNotes = new JLabel("Notes :");
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		JScrollPane scrollPane_2 = new JScrollPane();
		
		JScrollPane scrollPane_3 = new JScrollPane();
		
		JScrollPane scrollPane_4 = new JScrollPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_4, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
						.addComponent(scrollPane_3, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
						.addComponent(scrollPane_2, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 454, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
								.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
								.addComponent(lblAdress)
								.addComponent(lblPhoneNumber)
								.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
									.addComponent(lblName)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblValname)
									.addPreferredGap(ComponentPlacement.RELATED, 162, Short.MAX_VALUE)
									.addComponent(lblId)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(lblValid))
								.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)))
						.addComponent(lblEmails, Alignment.LEADING)
						.addComponent(lblMessaging, Alignment.LEADING)
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(lblOrganisation)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValorga))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(lblOrganisationStatus)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValstatus))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(lblTimesContacted)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblValtimes))
						.addGroup(Alignment.LEADING, gl_contentPane.createSequentialGroup()
							.addComponent(lblLastTimeContacted)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblVallasttime))
						.addComponent(lblNotes, Alignment.LEADING))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 150, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblName)
								.addComponent(lblValname)
								.addComponent(lblValid)
								.addComponent(lblId))
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblPhoneNumber)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblAdress)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(scrollPane_1)))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblEmails)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblMessaging)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_3, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOrganisation)
						.addComponent(lblValorga))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOrganisationStatus)
						.addComponent(lblValstatus))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTimesContacted)
						.addComponent(lblValtimes))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLastTimeContacted)
						.addComponent(lblVallasttime))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNotes)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_4, GroupLayout.DEFAULT_SIZE, 37, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		areaNotes = new JTextArea();
		scrollPane_4.setViewportView(areaNotes);
		
		areaMessaging = new JTextArea();
		scrollPane_3.setViewportView(areaMessaging);
		
		areaEmails = new JTextArea();
		scrollPane_2.setViewportView(areaEmails);
		
		areaAdress = new JTextArea();
		scrollPane_1.setViewportView(areaAdress);
		
		areaPhones = new JTextArea();
		scrollPane.setViewportView(areaPhones);
		
		lblPicture = new JLabel("picture");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblPicture)
					.addContainerGap(113, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addComponent(lblPicture)
					.addContainerGap(132, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		contentPane.setLayout(gl_contentPane);
		
		this.initContactInfo();
	}
	
	private void initContactInfo() {
		lblValid.setText(""+contact.getId());
		lblVallasttime.setText(""+(new Date(contact.getLast_time_contacted())));
		lblValname.setText(contact.getDisplay_name());
		if(contact.getOrganisationName() != null) lblValorga.setText(contact.getOrganisationName());
		if(contact.getOrganisationStatus() != null) lblValstatus.setText(contact.getOrganisationStatus());
		lblValtimes.setText(""+contact.getTimes_contacted());
		
		String temp = "";
		if(contact.getEmails() != null) {
			for(String s : contact.getEmails()) temp += s+"\n";
		}
		areaEmails.setText(temp);
		
		temp = "";
		if(contact.getEmails() != null) {
			for(String s : contact.getNotes()) temp += s+"\n";
		}
		areaNotes.setText(temp);
		
		temp = "";
		if(contact.getEmails() != null) {
			for(String s : contact.getMessaging()) temp += s+"\n";
		}
		areaMessaging.setText(temp);
		
		temp = "";
		if(contact.getEmails() != null) {
			for(String s : contact.getPhones()) temp += s+"\n";
		}
		areaPhones.setText(temp);
		
		temp = "";
		if(contact.getStreet() != null) temp += contact.getStreet() + "\n";
		if(contact.getCity() != null) temp += contact.getCity() +"\n";
		if(contact.getRegion() != null) temp += contact.getRegion() +" ";
		if(contact.getCountry() != null) temp += contact.getCountry();
		areaAdress.setText(temp);
		
		if(contact.getPhoto() != null) {
			ImageIcon image = new ImageIcon(contact.getPhoto());
			Image img = image.getImage();
			Image newimg = img.getScaledInstance(145,145,  java.awt.Image.SCALE_SMOOTH);  
			lblPicture.setIcon(new ImageIcon(newimg));
			lblPicture.setBounds(lblPicture.getX(), lblPicture.getY(), 145, 145);
		}
	}
}
