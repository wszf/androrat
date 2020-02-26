package out;

import java.io.DataOutputStream;
import java.io.IOException;

public class Sender {

	DataOutputStream out;
	
	public Sender(DataOutputStream out)
	{
		this.out = out ;
	}

	public void send(byte[] data)
	{
		try
		{
			out.write(data);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
