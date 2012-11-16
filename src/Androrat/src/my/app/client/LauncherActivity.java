package my.app.client;

import my.app.client.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LauncherActivity extends Activity {
    /** Called when the activity is first created. */
	
	Intent Client, ClientAlt;
	Button btnStart, btnStop;
	EditText ipfield, portfield;
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
        
        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	Client.putExtra("IP", ipfield.getText().toString());
            	Client.putExtra("PORT", new Integer(portfield.getText().toString()));
                startService(Client);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                //finish();                
            }
        });

        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {             
                stopService(Client);  
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                //finish(); 
            }
        });
    }
}