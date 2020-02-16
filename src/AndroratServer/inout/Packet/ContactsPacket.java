package Packet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import utils.Contact;

public class ContactsPacket implements Packet{

	ArrayList<Contact> list;

	public ContactsPacket() {
		
	}
	
	public ContactsPacket(ArrayList<Contact> ar) {
		list = ar;
	}
	
	public byte[] build() {
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bos);
			out.writeObject(list);
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
			list = (ArrayList<Contact>) in.readObject();
		} catch (Exception e) {
		}
	}
	
	public ArrayList<Contact> getList() {
		return list;
	}
}