package my.app.client;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class AlarmListener extends BroadcastReceiver { 
	
	public final String TAG = AlarmListener.class.getSimpleName();
	
    @Override
    public void onReceive(Context context, Intent intent) {
    	Log.d(TAG, "Alarm received !");
	   try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("alarm_message");
			if(message != null) {
				Log.i(TAG, "Message received: "+message);
				
				Intent serviceIntent = new Intent(context, Client.class);
				serviceIntent.setAction(AlarmListener.class.getSimpleName());//By this way the Client will know that it was AlarmListener that launched it
				context.startService(serviceIntent);
				
			}
			//Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e(TAG, "Error in Alarm received !"+ e.getMessage());
	   }
    }
}