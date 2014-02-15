package my.app.client;

import my.app.client.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LauncherActivity extends Activity {
    /** Called when the activity is first created. */
	
	Intent Client, ClientAlt;
	Button btnStart, btnStop;
	EditText ipfield, portfield;
	String myIp = "192.168.137.248"; //Put your IP in these quotes.
	int myPort = 9999; //Put your port there, notice that there are no quotes here.
	
	@Override
	public void onStart() {
		super.onStart();
		onResume();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		setContentView(R.layout.main);

        Client = new Intent(this, Client.class);
        Client.setAction(LauncherActivity.class.getName());
        
        btnStart = (Button) findViewById(R.id.buttonstart);
        btnStop = (Button) findViewById(R.id.buttonstop);
        ipfield = (EditText) findViewById(R.id.ipfield);
        portfield = (EditText) findViewById(R.id.portfield);
        
		if ( myIp == "" ) {
			ipfield.setText("192.168.137.1");
			portfield.setText("9999");
			Client.putExtra("IP", ipfield.getText().toString());
			Client.putExtra("PORT", Integer.parseInt(portfield.getText().toString()) );  
        } else {
			ipfield.setText(myIp);
			portfield.setText(String.valueOf(myPort));
			Client.putExtra("IP", myIp);
			Client.putExtra("PORT", myPort );  
		}
        
        
        startService(Client);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        moveTaskToBack(false);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Client = new Intent(this, Client.class);
        Client.setAction(LauncherActivity.class.getName());
        
        btnStart = (Button) findViewById(R.id.buttonstart);
        btnStop = (Button) findViewById(R.id.buttonstop);
        ipfield = (EditText) findViewById(R.id.ipfield);
        portfield = (EditText) findViewById(R.id.portfield);
        
		if ( myIp == "" ) {
			ipfield.setText("192.168.137.1");
			portfield.setText("9999");
			Client.putExtra("IP", ipfield.getText().toString());
			Client.putExtra("PORT", Integer.parseInt(portfield.getText().toString()) );  
        } else {
			ipfield.setText(myIp);
			portfield.setText(String.valueOf(myPort));
			Client.putExtra("IP", myIp);
			Client.putExtra("PORT", myPort );  
		}
        
        
        startService(Client);
        btnStart.setEnabled(false);
        btnStop.setEnabled(true);
        //moveTaskToBack(true);
    }
}
