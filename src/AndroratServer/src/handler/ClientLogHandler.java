package handler;

import server.Server;
import Packet.LogPacket;
import Packet.Packet;
import gui.GUI;

public class ClientLogHandler implements PacketHandler {
	
	private GUI gui;
	private int channel;
	private String imei;
	
	public ClientLogHandler(int channel, String imei, GUI gui) {
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
		c.getChannelHandlerMap().get(imei).getStorage(channel).reset();
		LogPacket packet = (LogPacket) p;
		if(packet.getType() == 0) gui.clientLogTxt(imei, packet.getDate(), packet.getMessage());
		else gui.clientErrLogTxt(imei, packet.getDate(), packet.getMessage());
	}

}
