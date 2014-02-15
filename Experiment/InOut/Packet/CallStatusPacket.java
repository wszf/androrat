package Packet;

import java.nio.ByteBuffer;

public class CallStatusPacket implements Packet{

	int type;
	/*
	 * 1 -> Incoming call
	 * 2 -> Missed call
	 * 3 -> Call accepted
	 * 4 -> Call send
	 * 5 -> Hang Up
	 * 
	 */
	String phonenumber;
	
	public CallStatusPacket() {
		
	}
	
	public CallStatusPacket(int type, String phone) {
		this.type = type;
		this.phonenumber = phone;
	}
	
	public byte[] build() {
		ByteBuffer b;
		if(phonenumber == null) {
			b = ByteBuffer.allocate(4);
			b.putInt(type);
		}
		else {
			b = ByteBuffer.allocate(4+phonenumber.length());
			b.putInt(type);
			b.put(phonenumber.getBytes());
		}
		return b.array();
	}

	public void parse(byte[] packet) {
		ByteBuffer b= ByteBuffer.wrap(packet);
		this.type = b.getInt();
		if(b.hasRemaining()) {
			byte[] tmp = new byte[b.remaining()];
			b.get(tmp);
			this.phonenumber = new String(tmp);
		}
		else
			this.phonenumber = null;
	}

	public int getType() {
		return type;
	}

	public String getPhonenumber() {
		return phonenumber;
	}
}
