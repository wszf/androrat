package handler;

import server.Server;
import Packet.AdvancedInformationPacket;
import Packet.Packet;
import gui.GUI;

public class AdvInfoHandler implements PacketHandler {

	private GUI gui;
	private int channel;
	private String imei;
	
	public AdvInfoHandler(int chan, String imei, GUI gui) {
		channel = chan;
		this.imei = imei;
		this.gui = gui;
	}

	@Override
	public void receive(Packet p, String imei) {
		// TODO Auto-generated method stub
	}

	@Override
	public void handlePacket(Packet p, String temp_imei, Server c) {
		gui.logTxt("Information data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		AdvancedInformationPacket packet = (AdvancedInformationPacket) p;
		gui.updateAdvInformations(imei, packet);
	}
}
