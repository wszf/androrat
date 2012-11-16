package gui.panel;

import gui.UserGUI;

import javax.swing.JPanel;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.interfaces.MapMarker;

import javax.swing.BoxLayout;
import javax.swing.JLabel; 
import javax.swing.JOptionPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JSplitPane;

public class MapPanel extends JPanel {
	
	private boolean streaming = false;
	private JButton btnStopStreaming;
	private UserGUI gui;
	
	private JRadioButton rdbtnNetwork;
	private JRadioButton rdbtnGps;
	private JLabel lblVallongitude;
	private JLabel lblVallatitude;
	private JLabel lblValaltitude;
	private JLabel lblValvitesse;
	private JLabel lblValprecision;
	private JLabel lblVallastdata;
	private JMapViewer mapViewer;
	
	private double lastLongitude;
	private double lastLatitude;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	/**
	 * Create the panel.
	 */
	public MapPanel(UserGUI gui) {
		
		this.gui = gui;
		streaming = false;
		
		JSplitPane splitPane = new JSplitPane();
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(splitPane, GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(splitPane, GroupLayout.PREFERRED_SIZE, 545, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		mapViewer = new JMapViewer();
		splitPane.setLeftComponent(mapViewer);
		
		JPanel panel = new JPanel();
		splitPane.setRightComponent(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Informations", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		
		JButton btnCenterView = new JButton("Center view");
		btnCenterView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				centerMapView();
			}
		});
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Start group", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addComponent(panel_1, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
						.addComponent(panel_2, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
						.addComponent(btnCenterView, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(panel_1, GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(panel_2, GroupLayout.PREFERRED_SIZE, 151, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnCenterView)
					.addGap(161))
		);
		
		JLabel lblProvider = new JLabel("Location provider :");
		
		rdbtnNetwork = new JRadioButton("Network");
		rdbtnNetwork.setSelected(true);
		buttonGroup.add(rdbtnNetwork);
		
		rdbtnGps = new JRadioButton("GPS");
		buttonGroup.add(rdbtnGps);
		
		btnStopStreaming = new JButton("Start streaming");
		btnStopStreaming.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonStreaming();
			}
		});
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(btnStopStreaming, GroupLayout.DEFAULT_SIZE, 147, Short.MAX_VALUE)
						.addComponent(rdbtnGps)
						.addGroup(gl_panel_2.createSequentialGroup()
							.addComponent(lblProvider)
							.addGap(18))
						.addComponent(rdbtnNetwork))
					.addContainerGap())
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblProvider)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rdbtnNetwork)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(rdbtnGps)
					.addGap(18)
					.addComponent(btnStopStreaming)
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		panel_2.setLayout(gl_panel_2);
		
		JLabel lblLongitude = new JLabel("Longitude :");
		
		lblVallongitude = new JLabel("val_longitude");
		
		JLabel lblLatitude = new JLabel("Latitude :");
		
		lblVallatitude = new JLabel("val_latitude");
		
		JLabel lblAltitude = new JLabel("Altitude :");
		
		lblValaltitude = new JLabel("val_altitude");
		
		JLabel lblVitesse = new JLabel("Speed :");
		
		lblValvitesse = new JLabel("val_vitesse");
		
		JLabel lblPrcision = new JLabel("Accuracy :");
		
		lblValprecision = new JLabel("val_precision");
		
		JLabel lblLastData = new JLabel("Last received data :");
		
		lblVallastdata = new JLabel("val_last_data");
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblLongitude)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblVallongitude))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblLatitude)
							.addGap(6)
							.addComponent(lblVallatitude))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblAltitude)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblValaltitude))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblVitesse)
							.addGap(6)
							.addComponent(lblValvitesse))
						.addGroup(gl_panel_1.createSequentialGroup()
							.addComponent(lblPrcision)
							.addGap(6)
							.addComponent(lblValprecision))
						.addComponent(lblLastData)
						.addComponent(lblVallastdata))
					.addContainerGap(195, Short.MAX_VALUE))
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLongitude)
						.addComponent(lblVallongitude))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLatitude)
						.addComponent(lblVallatitude))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblAltitude)
						.addComponent(lblValaltitude))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblVitesse)
						.addComponent(lblValvitesse))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.LEADING)
						.addComponent(lblPrcision)
						.addComponent(lblValprecision))
					.addGap(18)
					.addComponent(lblLastData)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblVallastdata)
					.addContainerGap(58, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		panel.setLayout(gl_panel);
		setLayout(groupLayout);

	}
	
	private void centerMapView() {
		 mapViewer.setDisplayPositionByLatLon(lastLatitude, lastLongitude, mapViewer.getZoom());
	}
	
	private void fireButtonStreaming() {
		if(streaming) {
			btnStopStreaming.setText("Start streaming");
			streaming = false;
			gui.fireStopGPSStreaming();
		} else {
			String provider = null;
			if(rdbtnNetwork.isSelected()) provider = "network";
			else if(rdbtnGps.isSelected()) provider = "gps";
			btnStopStreaming.setText("Stop streaming");
			streaming = true;
			gui.fireStartGPSStreaming(provider);
		}
	}
	
	public void updateMap(double longitude, double latitude, double altitude, float speed, float accuracy) {
		lastLatitude = latitude;
		lastLongitude = longitude;
		
		MapMarkerDot marker = new MapMarkerDot(latitude, longitude);
		List<MapMarker> markerList = new ArrayList<MapMarker>();
		markerList.add(marker);
		mapViewer.setMapMarkerList(markerList);
		mapViewer.setMapMarkerVisible(true);
		
		lblVallongitude.setText(String.valueOf(longitude));
		lblVallatitude.setText(String.valueOf(latitude));
		lblValaltitude.setText(String.valueOf(altitude));
		lblValvitesse.setText(String.valueOf(speed));
		lblValprecision.setText(String.valueOf(accuracy));

		Date date = new Date(System.currentTimeMillis());
		lblVallastdata.setText(date.toString());
	}
	
	public boolean getStreaming() {
		return streaming;
	}
}
