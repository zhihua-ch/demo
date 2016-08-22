package com.example.camdemo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements OnClickListener{
	private Camera mCamera = null;
	private CameraPreview mPreview = null;
	private MediaRecorder mRecorder = null;
	private int cameraId = 0;
	private Button startButton;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }*/
        
        mCamera = getCameraInstance();
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        startButton = (Button)findViewById(R.id.start);
        startButton.setOnClickListener(this);
    }
    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    protected void onResume(){
    	//Log.d(TAG, "onResume");
    	super.onResume(); 
    	//mPreview.onResume();	
    }
    
    /** A safe way to get an instance of the Camera object. */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }
    public void onClick(View view){
    	
    }
    /*
    public void recordvideo(){
    	/**get a file first
    	this.mCamera.unlock();
    	mRecorder = new MediaRecorder();
    	mRecorder.setCamera(mCamera);
    	//if(audioOn) setAudioResource();
    	mRecorder.setVideoSource(cameraId);
    	CamcorderProfile profile = CamcorderProfile.get(this.cameraId, CamcorderProfile.QUALITY_HIGH);*/
		/*if( record_audio ) {
			mediarec.setProfile(profile);
		}
		else {*/
			// from http://stackoverflow.com/questions/5524672/is-it-possible-to-use-camcorderprofile-without-audio-source
		/*mRecorder.setOutputFormat(profile.fileFormat);
		mRecorder.setVideoFrameRate(profile.videoFrameRate);
		mRecorder.setVideoSize(profile.videoFrameWidth, profile.videoFrameHeight);
		mRecorder.setVideoEncodingBitRate(profile.videoBitRate);
		mRecorder.setVideoEncoder(profile.videoCodec);
		
		videofile = this.getOutputMediaFile(MEDIA_TYPE_VIDEO);
		
		String vpath = videofile.getAbsolutePath();
		Log.d(TAG, "save to" + vpath);
		mRecorder.setOutputFile(vpath);
		
		try {
    		mRecorder.setPreviewDisplay(mHolder.getSurface());
			mRecorder.prepare();
        	mRecorder.start();
        	/*video_start_time = System.currentTimeMillis();
        	video_start_time_set = true;
    		main_activity.runOnUiThread(new Runnable() {
    			public void run() {
    				stopstart_video_toast = showToast(stopstart_video_toast, "Started recording video");
    			}
  			});
            main_activity.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(videoFile)));*/
    /*	}
    	catch(IOException e) {    		
    			Log.d(TAG, "failed to save video");
			e.printStackTrace();
			mRecorder.reset();
    		mRecorder.release(); 
    		mRecorder = null;
			//is_taking_photo = false;
			//is_taking_photo_on_timer = false;
			this.reconnectCamera();
  		}
		catch(RuntimeException e) {
    		// needed for emulator at least - although MediaRecorder not meant to work with emulator, it's good to fail gracefully    		
    		Log.e(TAG, "runtime exception starting video recorder");
			e.printStackTrace();
    		/*main_activity.runOnUiThread(new Runnable() {
    			public void run() {
		    	    showToast(null, "Failed to record video");
    			}
  			});*/
    		/*mRecorder.reset();
    		mRecorder.release(); 
    		mRecorder = null;
			//is_taking_photo = false;
			//is_taking_photo_on_timer = false;
			this.reconnectCamera();
		}
		
		
    } */
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    /*public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }*/

}
