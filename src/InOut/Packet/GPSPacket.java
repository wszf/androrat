package Packet;

import java.nio.ByteBuffer;

public class GPSPacket implements Packet
{

	private double longitude;
	private double latitude;
	private double altitude;
	private float speed;
	private float accuracy;
	

	public GPSPacket()
	{
		
	}
	
	public GPSPacket(double lat, double lon, double alt, float speed, float acc)
	{
		this.latitude = lat ;
		this.longitude = lon ;
		this.altitude = alt;
		this.speed = speed;
		this.accuracy = acc;
	}
	
	public byte[] build()
	{
		ByteBuffer b = ByteBuffer.allocate(32);
		System.out.println("Longitude : "+longitude);
		b.putDouble(this.longitude);
		b.putDouble(this.latitude);
		b.putDouble(this.altitude);
		b.putFloat(this.speed);
		b.putFloat(this.accuracy);
		return b.array();
	}

	public void parse(byte[] packet)
	{
		ByteBuffer b = ByteBuffer.wrap(packet);
		this.longitude = b.getDouble();
		this.latitude = b.getDouble();
		this.altitude = b.getDouble();
		this.speed = b.getFloat();
		this.accuracy = b.getFloat();
	}

	

	public double getLongitude()
	{
		return longitude;
	}

	public double getLatitude()
	{
		return latitude;
	}

	public double getAltitude()
	{
		return altitude;
	}

	public float getSpeed()
	{
		return speed;
	}

	public float getAccuracy()
	{
		return accuracy;
	}

}
