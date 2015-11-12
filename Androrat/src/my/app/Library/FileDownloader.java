package my.app.Library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import my.app.client.ClientListener;

import Packet.FilePacket;

public class FileDownloader {

	ClientListener ctx;
	byte[] finalData;
	InputStream in;
	File f;
	int channel;
	FilePacket packet;
	byte[] buffer;
	short numseq = 0;
	int BUFF_SIZE = 4096;
	
	public FileDownloader(ClientListener c) {
		ctx = c;
	}
	
	public boolean downloadFile(String s, int chan) {
		channel = chan;
		f = new File(s);
		try {
			in = new FileInputStream(f);
		} catch (FileNotFoundException e) {
			return false;
		}

        Thread loadf = new Thread(new Runnable() {
        	public void run() {
        		load();
        	}
        });
        loadf.start();
        
		return true;
	}
	
	public void load() {
		try {
			while(true) {
				buffer = new byte[BUFF_SIZE];
				int read = in.read(buffer);
				if (read == -1) {
					break;
				}
				if (read == BUFF_SIZE) {
					packet = new FilePacket(numseq, (byte) 1, buffer);
					ctx.handleData(channel, packet.build());
					numseq ++;
				}
				else {//C'était le dernier paquet
					byte[] tmp = new byte[read];
					System.arraycopy(buffer, 0, tmp, 0, read);
					packet = new FilePacket(numseq, (byte) 0, tmp);
					ctx.handleData(channel, packet.build());
					break;
				}
			}
			in.close();
		}
		catch(IOException e) {
			ctx.sendError("IOException loading file");
		}
	}
}
