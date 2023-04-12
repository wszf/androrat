package gui.panel;

import gui.UserGUI;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;

import Packet.SMSPacket;
import java.util.ResourceBundle;

public class SMSLogPanel extends JPanel {
	private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("gui.panel.messages"); //$NON-NLS-1$
	
	public static Color IN_SMS = new Color(14,92,7);
	public static Color OUT_SMS = Color.blue;
	
	private JTextArea areaKeyword;
	private JFormattedTextField formattedMinDate;
	private JFormattedTextField formattedMaxDate;
	private JComboBox sourceBox;
	private JComboBox typeBox;
	private ColorPane colorPane;
	private JTextField phoneNumberField;
	
	private UserGUI gui;

	/**
	 * Create the panel.
	 */
	public SMSLogPanel(UserGUI gui) {
		this.gui = gui;
		
		JLabel lblTypes = new JLabel(BUNDLE.getString("Types;")); //$NON-NLS-1$
		
		JLabel lblIncoming = new JLabel(BUNDLE.getString("received-SMS")); //$NON-NLS-1$
		lblIncoming.setForeground(IN_SMS);
		
		JLabel lblSent = new JLabel(BUNDLE.getString("sent-SMS")); //$NON-NLS-1$
		lblSent.setForeground(OUT_SMS);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTypes)
							.addGap(29)
							.addComponent(lblIncoming)
							.addGap(42)
							.addComponent(lblSent))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTypes)
						.addComponent(lblIncoming)
						.addComponent(lblSent))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane)
					.addContainerGap())
		);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		colorPane = new ColorPane();
		scrollPane.setViewportView(colorPane);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), BUNDLE.getString("sms-filters"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblTypeOfCall = new JLabel(BUNDLE.getString("Source-SMS")); //$NON-NLS-1$
		
		sourceBox = new JComboBox();
		sourceBox.setModel(new DefaultComboBoxModel(new String[] {BUNDLE.getString("All-sms"), BUNDLE.getString("received-SMS"), BUNDLE.getString("sent-SMS")}));
		
		JLabel lblPhoneNumber = new JLabel(BUNDLE.getString("Phone-number")); //$NON-NLS-1$
		
		phoneNumberField = new JTextField();
		phoneNumberField.setColumns(10);
		
		JLabel lblMinDate = new JLabel(BUNDLE.getString("Not-before")); //$NON-NLS-1$
		
		formattedMinDate = new JFormattedTextField(createFormatter("**/**/****"));
		
		JLabel lblNotAfter = new JLabel(BUNDLE.getString("Not-after")); //$NON-NLS-1$
		
		formattedMaxDate = new JFormattedTextField(createFormatter("**/**/****"));
		
		JButton btnGetSMSLogs = new JButton(BUNDLE.getString("Get-SMS")); //$NON-NLS-1$
		btnGetSMSLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireGetSMS();
			}
		});
		
		JLabel lblTypeOfSms = new JLabel(BUNDLE.getString("Type-of-SMS")); //$NON-NLS-1$
		
		typeBox = new JComboBox();
		typeBox.setModel(new DefaultComboBoxModel(new String[] {BUNDLE.getString("All"), BUNDLE.getString("Unread"),BUNDLE.getString("Read")}));
		
		JLabel lblBodyKeyword = new JLabel(BUNDLE.getString("Body-keyword")); //$NON-NLS-1$
		
		JScrollPane scrollPane_1 = new JScrollPane();
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane_1, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(btnGetSMSLogs, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(lblTypeOfCall, Alignment.LEADING)
						.addComponent(sourceBox, Alignment.LEADING, 0, 238, Short.MAX_VALUE)
						.addComponent(lblPhoneNumber, Alignment.LEADING)
						.addComponent(phoneNumberField, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(lblMinDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(formattedMinDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(lblNotAfter, Alignment.LEADING)
						.addComponent(formattedMaxDate, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
						.addComponent(lblTypeOfSms, Alignment.LEADING)
						.addComponent(typeBox, Alignment.LEADING, 0, 238, Short.MAX_VALUE)
						.addComponent(lblBodyKeyword, Alignment.LEADING))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(lblTypeOfCall)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(sourceBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblPhoneNumber)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(phoneNumberField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblMinDate)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(formattedMinDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblNotAfter)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(formattedMaxDate, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblTypeOfSms)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(typeBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblBodyKeyword)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane_1, GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGetSMSLogs)
					.addGap(5))
		);
		
		areaKeyword = new JTextArea();
		scrollPane_1.setViewportView(areaKeyword);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}
	
	protected MaskFormatter createFormatter(String s) {
	    MaskFormatter formatter = null;
	    try {
	        formatter = new MaskFormatter(s);
	    } catch (java.text.ParseException exc) {
	    }
	    return formatter;
	}
	
	private void fireGetSMS() {
		String request = "";
		///if(sourceBox.getSelectedIndex() != 0) request += " _id = "+sourceBox.getSelectedIndex();
		/*if(!personField.getText().equalsIgnoreCase("")) {
			if(request.equals("")) request += " person = '"+personField.getText()+"'";
			else request += " and person = '"+personField.getText()+"'";
		}*/
		if(!phoneNumberField.getText().equalsIgnoreCase("")) {
			if(request.equals("")) request += " address = '"+phoneNumberField.getText()+"'";
			else request += " and address = '"+phoneNumberField.getText()+"'";
		}
		
		if(formattedMinDate.getValue() != null) {
			if(!formattedMinDate.getValue().equals("  /  /    ")) {
				System.out.println("Valeur min date : "+formattedMinDate.getValue());
				String[] res = ((String) formattedMinDate.getValue()).split("/");
				//Date date = new Date(Integer.valueOf(res[0]), Integer.valueOf(res[1]), Integer.valueOf(res[2]));
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date;
				try {
					date = (Date)formatter.parse(formattedMinDate.getText());
					if(request.equals("")) request += " date > "+date.getTime();
					else request += " and date > "+date.getTime();
				} catch (ParseException e) {
					gui.errLogTxt(new Date().getTime(), "Bad format for minimum date");
				}
				
			}
		}
		if(formattedMaxDate.getValue() != null) {
			if(!formattedMaxDate.getValue().equals("  /  /    ")) {
				System.out.println("Valeur min date : "+formattedMaxDate.getValue());
				String[] res = ((String) formattedMaxDate.getValue()).split("/");
				//Date date = new Date(Integer.valueOf(res[0]), Integer.valueOf(res[1]), Integer.valueOf(res[2]));
				
				DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				Date date;
				try {
					date = (Date)formatter.parse(formattedMaxDate.getText());
					if(request.equals("")) request += " date < "+date.getTime();
					else request += " and date < "+date.getTime();
				} catch (ParseException e) {
					gui.errLogTxt(new Date().getTime(), "Bad format for maximum date");
				}

			}
		}
		
		if(typeBox.getSelectedIndex() != 0) {
			if(request.equals("")) request += " read = " + (typeBox.getSelectedIndex()-1);
			else request += " and read = " + (typeBox.getSelectedIndex()-1);
		}
		
		if(sourceBox.getSelectedIndex() != 0) {
			if(request.equals("")) request += " type = " + (sourceBox.getSelectedIndex());
			else request += " and type = " + (sourceBox.getSelectedIndex());
		}
		
		if(!areaKeyword.getText().equals("")) {
			if(request.equals("")) request += "body like '%" + areaKeyword.getText()+"%'";
			else request += " and body like '%" + areaKeyword.getText()+"%'";
		}
		
		gui.fireGetSMS(request);
	}
	
	public void updateSMS(ArrayList<SMSPacket> logsList) {
		this.clearPanel();
		for(SMSPacket p: logsList) {
			String mess = "";
			mess += p.getId()+"("+p.getThread_id()+"): ";
			if(p.getType() == 1) {
				String state;
				if(p.getRead()== 1) {
					state = "read";
				}
				else
					state = "unread";
				mess+="Received("+state+"): ";
			}
			else
				mess+="Sent: ";
			
			mess+=p.getAddress()+"\n";
			mess+="Body:\n";
			mess+=p.getBody()+"\n--\n";
			mess+=new Date(p.getDate()).toString()+"\n\n";
			
			if(p.getType() == 1)
				colorPane.append(IN_SMS, mess);
			else if(p.getType() == 2)
				colorPane.append(OUT_SMS, mess);
		}
	}
	
	public void addSMS(String txt, Color color) {
		colorPane.append(color, txt);
	}
	
	public void clearPanel() {
		colorPane.setText("");
	}
}
