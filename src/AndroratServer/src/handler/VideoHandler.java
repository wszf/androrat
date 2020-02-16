package handler;

import gui.GUI;
import server.Server;
import Packet.Packet;
import Packet.RawPacket;

public class VideoHandler implements PacketHandler {

	private GUI gui;
	private int channel;
	private String imei;
	
	public VideoHandler(int channel, String imei, GUI gui) {
		this.gui = gui;
		this.channel = channel;
		this.imei = imei;
	}
	
	@Override
	public void receive(Packet p, String imei) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void handlePacket(Packet p, String temp_imei, Server c) {
		gui.logTxt("Video data has been received");
		c.getChannelHandlerMap().get(imei).getStorage(channel).reset();
		RawPacket packet = (RawPacket) p;
		gui.addVideoBytes(imei, packet.getData());
	}

}
