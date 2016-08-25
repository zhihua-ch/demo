package com.example.camdemo;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class MainActivity extends Activity implements OnClickListener{
	private Camera mCamera = null;
	private CameraPreview mPreview = null;
	//private MediaRecorder mRecorder = null;
	private MediaRecorder mMediaRecorder = null;
	private int cameraId = 0;
	private Button startButton;
	private static final String TAG = "mydemo";
	private boolean isRecording = false;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
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
        	Log.d(TAG, "camera open failed");
        }
        return c; // returns null if camera is unavailable
    }
    public void onClick(View view){
    	if(isRecording){
    		mMediaRecorder.stop();
    		releaseMediaRecorder();
    		mCamera.lock();
    		/*inform user recording stop*/
    		Log.d(TAG, "stop record");
    		isRecording = false;
    	}
    	else{
    		if(prepareVideoRecorder()){
    			mMediaRecorder.start();
    			Log.d(TAG, "start recording");
    			isRecording = true;
    		}else{
    			releaseMediaRecorder();
    		}
    	}
    }
    private boolean prepareVideoRecorder(){

       // mCamera = getCameraInstance();
    	mCamera.unlock();
    	Log.d(TAG, "unlock camera");
        mMediaRecorder = new MediaRecorder();
        Log.d(TAG, "has new mediarecorder");
        // Step 1: Unlock and set camera to MediaRecorder
        //mCamera.unlock();
        //Log.d(TAG, "unlock camera");
        mMediaRecorder.setCamera(mCamera);
        Log.d(TAG, "set camera");
        // Step 2: Set sources
        //mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        Log.d(TAG, "set NO audio source");
        mMediaRecorder.setVideoSource( cameraId);//MediaRecorder.VideoSource.CAMERA

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
       // mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
        
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
      //  video_recorder.setVideoFrameRate(profile.videoFrameRate);
		mMediaRecorder.setVideoSize(720, 576);//temparary
	//	video_recorder.setVideoEncodingBitRate(profile.videoBitRate);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        
        // Step 4: Set output file
        mMediaRecorder.setOutputFile(getOutputMediaFile(MEDIA_TYPE_VIDEO).toString());

        // Step 5: Set the preview output
        mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
            releaseMediaRecorder();
            return false;
        }
        return true;
    }
    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }
    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                  Environment.DIRECTORY_PICTURES), "MyCameraApp");*/
    	//File mediaStorageDir = getImageFolder();
    	//SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		//String folder_name = sharedPreferences.getString("preference_save_location", "OpenCamera");
        //File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), folder_name);
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
		File mediaStorageDir = path;
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
            "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
 /*   public void recordvideo(){
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
