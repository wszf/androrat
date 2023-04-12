package Packet;

import java.nio.ByteBuffer;

import inout.Protocol;

public class TransportPacket implements Packet {

	private int totalLength;
	private int awaitedLength;
	private int localLength;
	private boolean last;
	private short NumSeq;
	private int channel;
	private byte data[];

	private int fillingPosition;

	public TransportPacket() {
		awaitedLength = 0;
		fillingPosition = 0;
		
	}

	public TransportPacket(int tdl, int ll, int channel, boolean last,
			short nums, byte[] data) {
		this.totalLength = tdl;
		this.channel = channel;
		this.last = last;
		this.data = data;
		this.localLength = ll;
		this.NumSeq = nums;
	}

	public void parse(byte[] packet) {
		ByteBuffer b = ByteBuffer.wrap(packet);

		this.totalLength = b.getInt();
		this.localLength = b.getInt();

		byte checkLast = b.get();
		if (checkLast == (byte) 1)
			this.last = true;
		else
			this.last = false;

		this.NumSeq = b.getShort();
		this.channel = b.getInt();
		this.data = new byte[b.remaining()];
		b.get(data, 0, b.remaining());
	}

	public boolean parse(ByteBuffer buffer) throws Exception{
		

		totalLength = buffer.getInt();
		localLength = buffer.getInt();

		byte lst = buffer.get();
		if (lst == 1)
			last = true;
		else
			last = false;

		NumSeq = buffer.getShort();
		channel = buffer.getInt();
		/*
		System.out.println("Taille totale de la donn�e : " + totalLength);
		System.out.println("Taille des donn�es du paquet : " + localLength);
		System.out.println("Dernier paquet : " + last);
		System.out.println("Position du paquet : " + NumSeq);
		System.out.println("Canal:" + channel);
		System.out.println("Recuperation de la donnee");
		*/
		// si la place restante dans le buffer est insuffisante
		if ((buffer.limit() - buffer.position()) < localLength) {
			
			dataFilling(buffer, buffer.limit() - buffer.position());
			//System.out.println("une partie du packet a ete sauvegarde");
			return true;
			
		} 
		else 
		{
			// s'il y a assez de place, on sauvegarde tout le paquet
				data = new byte[localLength];
				buffer.get(data, 0, data.length);
				return false;
			
		}

	}

	public boolean parseCompleter(ByteBuffer buffer) throws Exception{
		//System.out.println("les donnees attendues sont de taille = " + awaitedLength);

		// si la taille des donnees attendues depasse celle du buffer
		if (buffer.limit() - buffer.position() < awaitedLength) {
			
			// on en recupere autant que l'on peut (taille du buffer)
			dataFilling(buffer, buffer.limit() - buffer.position());
			return true;
		} 
		else {
			
			// sinon on recupere la totalite
			dataFilling(buffer, awaitedLength);
			return false;
		}

	}

	public void dataFilling(ByteBuffer buffer, int length) {
		/*
		System.out.println("Taille buffer.remaining : "+buffer.remaining());
		System.out.println("Taille buffer.limit : "+buffer.limit());
		System.out.println("Taille buffer.pos : "+buffer.position());
		System.out.println("Taille fillig : "+fillingPosition);
		System.out.println("Taille partialData : "+partialData.length);
		System.out.println("Taille length : "+length);
		*/
		if( data == null) data = new byte[localLength];
		
		buffer.get(data, fillingPosition, length);
		fillingPosition += length;
		awaitedLength = localLength - fillingPosition;
		
	}

	public byte[] build() {
		byte[] cmdToSend = new byte[Protocol.HEADER_LENGTH_DATA + data.length];
		byte[] header = Protocol.dataHeaderGenerator(this.totalLength,
				this.localLength, this.last, this.NumSeq, this.channel);
		System.arraycopy(header, 0, cmdToSend, 0, header.length);
		System.arraycopy(data, 0, cmdToSend, header.length, data.length);

		return cmdToSend;
	}

	public int getTotalLength() {
		return totalLength;
	}

	public int getLocalLength() {
		return localLength;
	}

	public boolean isLast() {
		return last;
	}

	public short getNumSeq() {
		return NumSeq;
	}

	public int getChannel() {
		return channel;
	}

	public byte[] getData() {
		return data;
	}

}
