package handler;


import server.Server;
import Packet.Packet;

public interface PacketHandler 
{
   public void receive(Packet p,String imei);

   public void handlePacket(Packet p, String temp_imei, Server c);

}
