package Packet;

import java.nio.ByteBuffer;

public class LogPacket implements Packet {

	long date;
	byte type; //0 ok / 1 Error
	String message;
	
	public LogPacket() {
		
	}
	
	public LogPacket(long date, byte type, String message) {
		this.date = date;
		this.type = type;
		this.message = message;
	}
	
	public byte[] build() {
		ByteBuffer b = ByteBuffer.allocate(9+message.length());
		b.putLong(date);
		b.put(type);
		b.put(message.getBytes());
		return b.array();
	}

	public void parse(byte[] packet) {
		ByteBuffer b = ByteBuffer.wrap(packet);
		date = b.getLong();
		type = b.get();
		byte[] tmp = new byte[b.remaining()];
		b.get(tmp);
		message = new String(tmp);
	}

	public long getDate() {
		return date;
	}

	public byte getType() {
		return type;
	}

	public String getMessage() {
		return message;
	}

}
