package handler;

import gui.GUI;
import server.Server;
import Packet.GPSPacket;
import Packet.Packet;

public class GPSHandler implements PacketHandler
{

	private GUI gui;
	private int channel;
	private String imei;
	
	public GPSHandler(int chan, String imei, GUI gui) {
		channel = chan;
		this.imei = imei;
		this.gui = gui;
	}

	
	@Override
	public void handlePacket(Packet p,String imei,Server c) 
	{
		gui.logTxt("GPS data has been received");
		c.getChannelHandlerMap().get(imei).getStorage(channel).reset(); // Voir si faut le mettre ailleurs !
		
		GPSPacket gps = (GPSPacket)p;	
		gui.updateUserMap(imei, gps.getLongitude(), gps.getLatitude(), gps.getAltitude(), gps.getSpeed(), gps.getAccuracy());
	
	}
	
	
	@Override
	public void receive(Packet p, String temp_imei) {
		// TODO Auto-generated method stub
		
	}


	





	

}
