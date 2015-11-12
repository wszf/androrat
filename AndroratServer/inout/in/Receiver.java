package in;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import inout.Protocol;

public class Receiver {

	private Socket socket;
	private byte[] received_data;
	private ByteBuffer buffer;
	private InputStream is;

	public Receiver(Socket s) throws IOException {
		socket = s;
		is = socket.getInputStream();

		received_data = new byte[Protocol.MAX_PACKET_SIZE];
		buffer = ByteBuffer.allocate(Protocol.MAX_PACKET_SIZE);
	}

	public ByteBuffer read() throws IOException, SocketException { // A supprimer !
		int n = 0;

		n = is.read(received_data);

		buffer.clear();		
		buffer = ByteBuffer.wrap(received_data, 0, n);
		//System.out.println("data has been read:" + buffer.limit());

		return buffer;
	}
	
	public ByteBuffer read(ByteBuffer b) throws IOException, SocketException {
		int n = 0;
		
		byte[] theRest = null;
		
		if(b.position()>0 && b.position()<Protocol.HEADER_LENGTH_DATA)
		{
			theRest = new byte[b.position()];
			b.flip();
			b.get(theRest, 0, b.limit());
			System.arraycopy(theRest, 0, received_data, 0, theRest.length);
			//for(int i = 0; i<theRest.length;i++)
			//	received_data[i] = theRest[i];
			
			//System.out.println("theRest len = "+theRest.length);
			n = is.read(received_data,theRest.length,Protocol.MAX_PACKET_SIZE-theRest.length);
			n+=theRest.length;
		  }
		else
			n = is.read(received_data);
		
		
		//buffer.clear();		
		buffer = ByteBuffer.wrap(received_data, 0, n);
		//System.out.println("data has been read:" + n);

		return buffer;
	}

}
