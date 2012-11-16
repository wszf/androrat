package my.app.Library;

import java.io.IOException;
import java.io.InputStream;

import my.app.activityclient.ClientListener;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

public class VideoStreaming implements SurfaceHolder.Callback {

	private boolean isRecording = false;
	private static final String TAG = "VideoTest";
	private Camera camera;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder holder;
	private Button captureButton;

	public static String SOCKET_ADDRESS = "myLocalSocket";

	LocalSocket ls;
	
	ClientListener ctx;
	Thread th;
	int channel;
	byte[] tmp;
	
	public VideoStreaming(ClientListener c, int chan, SurfaceView view) {
        
		ctx = c;
		this.channel = chan;
		
        th = new Thread(new Runnable() {
        	
        	public void run() { listen(); }
        });
		th.start();
        
		ls = new LocalSocket();
        try {
			ls.connect(new LocalSocketAddress(SOCKET_ADDRESS));
		} catch (IOException e) {
			stop();
		}
        camera = Camera.open();
        
        SurfaceView cameraView = view;
        holder = cameraView.getHolder();
        holder.addCallback(this);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}
	
    public void listen() {
        try {
            LocalServerSocket server = new LocalServerSocket(SOCKET_ADDRESS);

            LocalSocket receiver = server.accept();
            if (receiver != null) {
                InputStream input = receiver.getInputStream();
                
                byte[] buffer = new byte[4096];
                // simply for java.util.ArrayList
                while(true) {
	                int read = input.read(buffer);
	                if(read == -1)
	                	break;

	                tmp = new byte[read];
	                System.arraycopy(buffer, 0, tmp, 0, read);
	                ctx.handleData(channel, tmp);
	                //out.write(buffer, 0, readed);
                }
            }
        } catch (IOException e) {
            Log.e(getClass().getName(), e.getMessage());
        }
    }
	
    public void start() {
    	if (prepareVideoRecorder()) {
            mMediaRecorder.start();
            captureButton.setText("Stop");
            isRecording = true;
        } else {
            releaseMediaRecorder();
            releaseCamera();
        }
    }
    
    public void stop() {
        mMediaRecorder.stop();
        releaseMediaRecorder();
        camera.lock();
        isRecording = false;
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
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
    
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
	}

}