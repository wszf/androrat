package handler;

import gui.GUI;
import server.Server;
import Packet.Packet;
import Packet.RawPacket;

public class PictureHandler implements PacketHandler {
	
	private GUI gui;
	private int channel;
	private String imei;
	
	public PictureHandler(int chan, String imei, GUI gui) {
		channel = chan;
		this.imei = imei;
		this.gui = gui;
	}

	@Override
	public void receive(Packet p, String imei) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePacket(Packet p, String imei, Server c) 
	{
		gui.logTxt("Image data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		RawPacket packet = (RawPacket) p;
		gui.updateUserPicture(imei, packet.getData());
	}
}
