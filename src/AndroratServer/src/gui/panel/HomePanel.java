package gui.panel;

import gui.UserGUI;

import javax.swing.JPanel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JTextArea;

import Packet.AdvancedInformationPacket;
import javax.swing.JList;
import javax.swing.JCheckBox;

import sun.security.krb5.internal.crypto.CksumType;
import javax.swing.JScrollPane;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class HomePanel extends JPanel {
	
	private UserGUI gui;
	private JTextField ipField;
	private JTextField portField;
	private JTextArea textArea;
	private JTextArea areaPhones;
	private JTextArea areaSMS;
	private JTextField textField;
	private JCheckBox chckbxWaitEventTo;
	private JTextField toastField;
	private JTextField durationField;
	private JTextField urlField;

	/**
	 * Create the panel.
	 */
	public HomePanel(UserGUI gui) {
		this.gui = gui;
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Quick actions", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Client options", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel, GroupLayout.PREFERRED_SIZE, 327, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE)
						.addComponent(panel_2, GroupLayout.DEFAULT_SIZE, 279, Short.MAX_VALUE))
					.addGap(18))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 399, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 251, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(panel_1, GroupLayout.PREFERRED_SIZE, 142, Short.MAX_VALUE)))
					.addContainerGap())
		);
		
		JLabel lblWhitephones = new JLabel("Phones :");
		
		JLabel lblWhitesms = new JLabel("SMS :");
		
		JLabel lblNeededKeyword = new JLabel("Needed keywords :");
		
		textField = new JTextField();
		textField.setColumns(10);
		
		areaPhones = new JTextArea();
		
		areaSMS = new JTextArea();
		
		JLabel lblServerIp = new JLabel("Server IP :");
		
		ipField = new JTextField();
		ipField.setHorizontalAlignment(SwingConstants.LEFT);
		ipField.setText("192.168.0.10");
		ipField.setColumns(10);
		
		JLabel lblServerPort = new JLabel("Server Port :");
		
		portField = new JTextField();
		portField.setText("5555");
		portField.setHorizontalAlignment(SwingConstants.LEFT);
		portField.setColumns(10);
		
		chckbxWaitEventTo = new JCheckBox("Wait event to connect");
		
		JButton btnSaveConnectionInfo = new JButton("Save configuration");
		btnSaveConnectionInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonSaveConnectionConfig();
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblWhitephones, GroupLayout.PREFERRED_SIZE, 70, GroupLayout.PREFERRED_SIZE)
									.addGap(4)
									.addComponent(areaPhones, GroupLayout.DEFAULT_SIZE, 171, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblNeededKeyword)
									.addPreferredGap(ComponentPlacement.UNRELATED)
									.addComponent(textField, GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblWhitesms)
									.addGap(18)
									.addComponent(areaSMS, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblServerIp)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(ipField, GroupLayout.DEFAULT_SIZE, 161, Short.MAX_VALUE))
								.addGroup(gl_panel_2.createSequentialGroup()
									.addComponent(lblServerPort)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(portField, GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)))
							.addContainerGap())
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(40)
							.addComponent(btnSaveConnectionInfo)
							.addGap(50))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(chckbxWaitEventTo)
							.addContainerGap(76, Short.MAX_VALUE))))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(lblWhitephones, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE)
						.addComponent(areaPhones, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addGap(18)
							.addComponent(lblWhitesms)
							.addGap(12))
						.addGroup(gl_panel_2.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(areaSMS, GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)))
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblNeededKeyword)
						.addComponent(textField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(ipField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServerIp))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(portField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblServerPort))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxWaitEventTo)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSaveConnectionInfo)
					.addGap(31))
		);
		panel_2.setLayout(gl_panel_2);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnNewButton = new JButton("Refresh");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fireButtonRefreshAdv();
			}
		});
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addContainerGap()
							.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE))
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(113)
							.addComponent(btnNewButton)))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 356, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnNewButton))
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		panel.setLayout(gl_panel);
		
		toastField = new JTextField();
		toastField.setColumns(10);
		
		JButton btnToastIt = new JButton("Toast it");
		btnToastIt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonToast();
			}
		});
		
		JButton btnVibrate = new JButton("Vibrate");
		btnVibrate.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonVibrate();
			}
		});
		
		durationField = new JTextField();
		durationField.setColumns(10);
		
		JLabel lblDuration = new JLabel("Duration: ");
		
		JLabel lblOpenUrl = new JLabel("Open url:");
		
		urlField = new JTextField();
		urlField.setColumns(10);
		
		JButton btnBrowseIt = new JButton("Browse it");
		btnBrowseIt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				fireButtonBrowse();
			}
		});
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel_1.createSequentialGroup()
									.addComponent(toastField, GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnToastIt))
								.addGroup(gl_panel_1.createSequentialGroup()
									.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
										.addComponent(lblOpenUrl)
										.addComponent(lblDuration))
									.addPreferredGap(ComponentPlacement.RELATED)
									.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
										.addGroup(gl_panel_1.createSequentialGroup()
											.addComponent(durationField, GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(btnVibrate))
										.addComponent(urlField, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)))))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addGap(156)
							.addComponent(btnBrowseIt, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(toastField, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnToastIt, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnVibrate)
						.addComponent(durationField, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
						.addComponent(lblDuration))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblOpenUrl)
						.addComponent(urlField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnBrowseIt)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		setLayout(groupLayout);
	}
	
	private void fireButtonSaveConnectionConfig() {
		ArrayList<String> phones = new ArrayList<String>();
		ArrayList<String> sms = new ArrayList<String>();
		ArrayList<String> kw = new ArrayList<String>();
		
		for(String phone : areaPhones.getText().split("\n")) phones.add(phone);
		for(String s : areaSMS.getText().split("\n")) sms.add(s); 
		for(String key : textField.getText().split(" ")) kw.add(key);
		
		gui.fireSaveConnectConfigurations(ipField.getText(), Integer.valueOf(portField.getText()), chckbxWaitEventTo.isSelected(), phones, sms, kw);
	}
	
	private void fireButtonToast() {
		String mess = toastField.getText();
		gui.getGUI().fireToastMessage(gui.getImei(), mess);
	}
	
	private void fireButtonVibrate() {
		String value = durationField.getText();
		long l = Long.valueOf(value);
		gui.getGUI().fireVibrate(gui.getImei(), l);
	}
	
	private void fireButtonBrowse() {
		gui.getGUI().fireBrowseUrl(gui.getImei(), urlField.getText());
	}
	
	public void updatePreferences(String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
		String temp = "";
		if(phones != null) {
			for(String s : phones) temp += s + "\n";
		}
		areaPhones.setText(temp);
		
		temp = "";
		if(sms != null) {
			for(String s : sms) temp += s + "\n";
		}
		areaSMS.setText(temp);
		
		temp = "";
		if(kw != null) {
			for(String s : kw) temp += s+" ";
			temp = temp.substring(0, temp.length() - 2);
		}
		textField.setText(temp);
		
		ipField.setText(ip);
		portField.setText(""+port);
		chckbxWaitEventTo.setSelected(wait);
	}
	
	private void fireButtonRefreshAdv() {
		textArea.setText("");
		gui.fireGetAdvancedInformations();
	}
		
	public void updateInformations(AdvancedInformationPacket packet) {
		String txt = "";
		
		txt += " - General informations :\n";
		txt += "Phone number = "+packet.getPhoneNumber()+"\n";
		txt += "IMEI = "+packet.getIMEI()+"\n";
		//txt += "Software version = "+packet.getSoftwareVersion()+"\n";
		txt += "Country = "+packet.getCountryCode()+"\n";
		txt += "Operator (name) = "+packet.getOperatorName()+"\n";
		txt += "Operator (code) = "+packet.getOperatorCode()+"\n";
		txt += "SIM operator name = "+packet.getSimOperatorName()+"\n";
		txt += "SIM operator code = "+packet.getSimOperatorCode()+"\n";
		txt += "SIM country ="+packet.getSimCountryCode()+"\n";
		txt += "SIM serial ="+packet.getSimSerial()+"\n";
		
		txt += "\n ----------------------------\n\n";
		
		txt += " - Wifi informations :\n";
		txt += "Is available = "+packet.isWifiAvailable()+"\n";
		txt += "Connected / connecting = "+packet.isWifiConnectedOrConnecting()+"\n";
		txt += "Extra info ="+packet.getWifiExtraInfos()+"\n";
		txt += "Reason = "+packet.getWifiReason()+"\n";
		
		txt += "\n ----------------------------\n\n";
		
		txt += " - Mobile network informations :\n";
		txt += "Name = "+packet.getMobileNetworkName()+"\n";
		txt += "Is available = "+packet.isMobileNetworkAvailable()+"\n";
		txt += "Connected / connecting = "+packet.isMobileNetworkConnectedOrConnecting()+"\n";
		txt += "Extra info = "+packet.getMobileNetworkExtraInfos()+"\n";
		txt += "Reason = "+packet.getMobileNetworkReason()+"\n";
		
		txt += "\n ----------------------------\n\n";
		
		txt += " - Android informations :\n";
		txt += "Android version = "+packet.getAndroidVersion()+"\n";
		txt += "SDK Android version = "+packet.getAndroidSdk()+"\n";
		
		txt += "\n ----------------------------\n\n";
		
		txt += " - Devices :\n";
		txt += "Number of devices = "+packet.getSensors().size()+"\n";
		for(String s : packet.getSensors()) {
			txt += " --> "+s+"\n";
		}
		
		txt += "\n ----------------------------\n\n";
		
		txt += " - Battery informations :\n";
		txt += "Is present = "+packet.isBatteryPresent();
		String[] health = {"", "unknown", "good", "overheat", "dead", "over voltage", "unspecified failure", "cold"};
		if(packet.getBatteryHealth() >= 0 && packet.getBatteryHealth() < 9) txt += "Health = "+ health[packet.getBatteryHealth()] +"\n";
		else txt += "Health = n/a\n";
		txt += "Level = "+packet.getBatteryLevel()+"\n";
		if(packet.getBatteryPlugged() == 1) txt += "Plugged = AC\n";
		else if(packet.getBatteryPlugged() == 2) txt += "Plugged = USB\n";
		else txt += "Plugged = n/a\n";
		txt += "Scale = "+packet.getBatteryScale()+"\n";
		String[] status = {"", "unknown", "charging", "discharging", "not charging", "full"};
		if(packet.getBatteryStatus() >= 0 && packet.getBatteryStatus() < 6) txt += "Status = "+status[packet.getBatteryStatus()]+"\n";
		else txt += "Status = n/a \n";
		txt += "Technologie = "+packet.getBatteryTechnology()+"\n";
		txt += "Temperature = "+packet.getBatteryTemperature()+"\n";
		txt += "Voltage = "+packet.getBatteryVoltage()+"\n";
		
		textArea.setText(txt);
	}
}
