package handler;

import inout.Protocol;

import java.util.ArrayList;

import Packet.TransportPacket;


/*
 * Class servant d'espace de stockage temporaire 
 * aux donn�es re�ues
 * 
 * 
 * M�thode addByteData():
 * permet de compl�ter les donn�es en cours de r�ception
 * en les rajoutant dans un tableaux d'octets
 * 
 * M�thode setLastPacketInfo():
 * permet d'inscrire les informations sur le dernier paquet re�u
 */
public class TemporaryStorage
{
	//les donn�es
	private ArrayList<byte[]> data_temp;

	private byte[] final_data;
	
	//les informations sur le dernier paquet re�u
	private int total_length;
	private int last_packet_position;
	private int size_counter;
	private boolean end;
	
	private short data_type;
	
	
	//constructeur: en cours de reception(commande)
	public TemporaryStorage()
	{
		//initialisation de l'espace de stockage
		data_temp = new ArrayList<byte[]>();
		last_packet_position = -1;
		size_counter = 0;
		
	}

	public void reset() {
		data_temp = new ArrayList<byte[]>();
		last_packet_position = -1;
		size_counter = 0;
		end = false;
	}

	
	public short addPacket(TransportPacket packet)
	{

		
		if(packet.getNumSeq() != (last_packet_position+1))
			return Protocol.PACKET_LOST;
		
		
		//si la suite des donn�es est attendue
        if(!end)
		{	
        	//System.out.println("on rajoute des donn�es attendues");

        	total_length = packet.getTotalLength();
			end = packet.isLast();
			size_counter+= packet.getLocalLength();
			data_temp.add(packet.getData());
			
			//si on attend encore des donn�es
			if(!end)
			{
				System.out.println("Paquet "+packet.getNumSeq());
				last_packet_position ++;
			  return Protocol.PACKET_DONE;
			}
			else//sinon (si c'est la fin)
			{
	
				if(size_counter != total_length)
					return Protocol.SIZE_ERROR;
				
				int i = 0;
				final_data = new byte[total_length];
				for(int n = 0;n<data_temp.size();n++)
					for(int p = 0;p<data_temp.get(n).length;p++,i++)
						final_data[i] = data_temp.get(n)[p];
						
				return Protocol.ALL_DONE;
			}
			
		}
		else 
			//sinon erreur
		   return Protocol.NO_MORE;
		
	}

	
	public ArrayList<byte[]> getByteData()
	{
		return data_temp;
	}
	
	public byte[] getFinalData()
	{
		return final_data;
	}
	
	public int getLastPacketPositionReceived()
	{
		return last_packet_position;
	}
	
	public int getTotalSize()
	{
		return total_length;
	}
}
