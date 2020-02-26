package Packet;

import java.nio.ByteBuffer;

public class ShortSMSPacket implements Packet{

	private int address_size;
	private String address;
	private long date;
	private int body_size;
	private String body;
	
	public ShortSMSPacket() {
		
	}
	
	public ShortSMSPacket(String ad, long dat, String body) {
		this.address = ad;
		this.address_size = ad.length();
		this.date = dat;
		this.body = body;
		this.body_size = this.body.length();
	}
	
	
	public byte[] build() {
		ByteBuffer b = ByteBuffer.allocate(4+4+address.length()+4+4+8+4+body.length()+4);
		b.putInt(address_size);
		b.put(address.getBytes());
		b.putLong(date);
		b.putInt(body_size);
		b.put(body.getBytes());
		return b.array();
	}

	public void parse(byte[] packet) {
		ByteBuffer b = ByteBuffer.wrap(packet);
		this.address_size = b.getInt();
		byte[] tmp = new byte[address_size];
		b.get(tmp);
		this.address = new String(tmp);
		this.date = b.getLong();
		this.body_size = b.getInt();
		tmp = new byte[body_size];
		b.get(tmp);
		this.body = new String(tmp);
	}

	public String getAddress() {
		return address;
	}

	public long getDate() {
		return date;
	}

	public String getBody() {
		return body;
	}
}