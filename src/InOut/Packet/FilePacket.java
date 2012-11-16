package Packet;

import java.nio.ByteBuffer;

public class FilePacket implements Packet {

	byte[] data;
	byte mf;
	short numSeq;
	
	public FilePacket() {
		
	}
	
	public FilePacket(short num, byte mf, byte[] data) {
		this.data = data;
		this.numSeq = num;
		this.mf = mf;
	}
	
	public byte[] build() {
		ByteBuffer b = ByteBuffer.allocate(data.length+3);
		b.putShort(numSeq);
		b.put(mf);
		b.put(data);
		return b.array();
	}

	public void parse(byte[] packet) {
		ByteBuffer b = ByteBuffer.wrap(packet);
		
		numSeq = b.getShort();
		mf = b.get();
		this.data = new byte[b.remaining()];
		b.get(data, 0, b.remaining());
	}

	public byte[] getData() {
		return data;
	}

	public byte getMf() {
		return mf;
	}

	public short getNumSeq() {
		return numSeq;
	}

}
