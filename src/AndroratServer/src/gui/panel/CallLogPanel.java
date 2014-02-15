package gui.panel;

import gui.UserGUI;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.text.MaskFormatter;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextField;
import javax.swing.JFormattedTextField;
import javax.swing.JButton;
import javax.swing.UIManager;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import Packet.CallPacket;
import javax.swing.JSplitPane;

public class CallLogPanel extends JPanel {
	
	public static Color IN_CALL = new Color(14,92,7);
	public static Color MISSED_IN_CALL = Color.red;
	public static Color OUT_CALL = Color.blue;
	
	private JFormattedTextField formattedMinDate;
	private JFormattedTextField formattedMaxDate;
	private JComboBox comboBox;
	private ColorPane colorPane;
	private JTextField phoneNumberField;
	private JTextField minDurationField;
	private JTextField maxDurationField;
	
	private UserGUI gui;

	/**
	 * Create the panel.
	 */
	public CallLogPanel(UserGUI gui) {
		this.gui = gui;
		
		JLabel lblTypes = new JLabel("Types :");
		
		JLabel lblIncomingCall = new JLabel("received call");
		lblIncomingCall.setForeground(IN_CALL);
		
		JLabel lblOutgoingCall = new JLabel("outgoing call");
		lblOutgoingCall.setForeground(OUT_CALL);
		
		JLabel lblMissedIncomingCall = new JLabel("missed call");
		lblMissedIncomingCall.setForeground(MISSED_IN_CALL);
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setResizeWeight(1.0);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 512, Short.MAX_VALUE)
							.addContainerGap())
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTypes)
							.addGap(29)
							.addComponent(lblIncomingCall)
							.addGap(42)
							.addComponent(lblMissedIncomingCall)
							.addGap(37)
							.addComponent(lblOutgoingCall)
							.addGap(209))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblTypes)
						.addComponent(lblIncomingCall)
						.addComponent(lblMissedIncomingCall)
						.addComponent(lblOutgoingCall))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setLeftComponent(scrollPane);
		
		colorPane = new ColorPane();
		scrollPane.setViewportView(colorPane);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Optional filters", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JLabel lblTypeOfCall = new JLabel("Type of call :");
		
		comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"All calls", "Received calls", "Sent calls", "Missed calls"}));
		
		JLabel lblPhoneNumber = new JLabel("Phone number :");
		
		phoneNumberField = new JTextField();
		phoneNumberField.setColumns(10);
		
		JLabel lblMinDate = new JLabel("Not before (dd/mm/yyyy)  :");
		
		formattedMinDate = new JFormattedTextField(createFormatter("**/**/****"));
		
		JLabel lblNotAfter = new JLabel("Not after");
		
		formattedMaxDate = new JFormattedTextField(createFormatter("**/**/****"));
		
		JLabel lblDuration = new JLabel("Min duration :");
		
		JLabel lblD = new JLabel("d >");
		
		minDurationField = new JTextField();
		minDurationField.setColumns(10);
		
		JLabel lblD_1 = new JLabel("d <");
		
		maxDurationField = new JTextField();
		maxDurationField.setColumns(10);
		
		JButton btnGetCallLogs = new JButton("Get call logs");
		btnGetCallLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireGetCallLogs();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(btnGetCallLogs, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
						.addComponent(comboBox, 0, 125, Short.MAX_VALUE)
						.addComponent(lblTypeOfCall)
						.addComponent(lblPhoneNumber)
						.addComponent(phoneNumberField, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
						.addComponent(lblMinDate)
						.addComponent(formattedMinDate, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
						.addComponent(lblNotAfter)
						.addComponent(formattedMaxDate, GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
						.addComponent(lblDuration)
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblD)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(minDurationField, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblD_1)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(maxDurationField, GroupLayout.DEFAULT_SIZE, 104, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblTypeOfCall)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(comboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
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
					.addComponent(lblDuration)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblD)
						.addComponent(minDurationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblD_1)
						.addComponent(maxDurationField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addGap(18)
					.addComponent(btnGetCallLogs)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
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
	
	private void fireGetCallLogs() {
		String request = "";
		if(comboBox.getSelectedIndex() != 0) request += " type = "+comboBox.getSelectedIndex();
		if(!phoneNumberField.getText().equalsIgnoreCase("")) {
			if(request.equals("")) request += " number = '"+phoneNumberField.getText()+"'";
			else request += " and number = '"+phoneNumberField.getText()+"'";
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
		
		if(!minDurationField.getText().equalsIgnoreCase("")) {
			if(request.equals("")) request += " duration > "+minDurationField.getText();
			else request += " and duration > "+minDurationField.getText();
		}
		if(!maxDurationField.getText().equalsIgnoreCase("")) {
			if(request.equals("")) request += " duration < "+maxDurationField.getText();
			else request += " and duration < "+maxDurationField.getText();
		}
		
		gui.fireGetCallLogs(request);
	}
	
	public void updateCallLogs(ArrayList<CallPacket> logsList) {
		this.clearPanel();
		for(CallPacket packet : logsList) {
			String name = packet.getName();
			if(name == null)
				name ="/";
			int type = packet.getType();
			String messtype = "";
			if(type ==1) messtype = "Incoming Call";
			else if(type ==2) messtype = "Outgoing Call";
			else if(type ==3) messtype = "Missed Call";
			String line = String.valueOf(packet.getId());
			line += ". " + messtype+": "+packet.getPhoneNumber();
			line += "\n\tName: "+name;
			line += "\n\tDuration: " + packet.getDuration() + "s\n\t";
			line += "Date: " + (new Date(packet.getDate())).toString();
			line += "\n\n";
			
			if(packet.getType() == 1) colorPane.append(IN_CALL, line);
			else if(packet.getType() == 2) colorPane.append(OUT_CALL, line);
			else colorPane.append(MISSED_IN_CALL, line);
		}
	}
	
	public void addCall(String txt, Color color) {
		colorPane.append(color, txt);
	}
	
	public void clearPanel() {
		colorPane.setText("");
	}
}
