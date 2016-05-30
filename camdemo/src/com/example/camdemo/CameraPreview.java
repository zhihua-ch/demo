package com.example.camdemo;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/** A basic Camera preview class */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private static final String TAG = "mydemo";
    private int cameraId = 0;
    private List<Camera.Size> sizes = null;
    
    public CameraPreview(Context context, Bundle savedInstanceState){
    	super(context);

		Log.d(TAG, "new Preview");

		// Install a SurfaceHolder.Callback so we get notified when the
		// underlying surface is created and destroyed.
		mHolder = getHolder();
		mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // deprecated
		
		if( savedInstanceState != null ) {    		
			Log.d(TAG, "have savedInstanceState");
		cameraId = savedInstanceState.getInt("cameraId", 0);		
			Log.d(TAG, "found cameraId: " + cameraId);
		if( cameraId < 0 || cameraId >= Camera.getNumberOfCameras() ) {			
			Log.d(TAG, "cameraID not valid for " + Camera.getNumberOfCameras() + " cameras!");
			cameraId = 0;
		}
    }

    }
    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        Log.d(TAG, "in camera preview");
     // get available sizes
        /*sizes = parameters.getSupportedPictureSizes();
		if( MyDebug.LOG ) {
			for(int i=0;i<sizes.size();i++) {
	        	Camera.Size size = sizes.get(i);
	        	Log.d(TAG, "supported picture size: " + size.width + " , " + size.height);
			}
		}*/
        
    }
    public void onResume(){
    	Log.d(TAG, "onResume in cameraPreview");
	
    	this.openCamera();
    }
    private boolean openCamera(){
    	boolean qOpened = false;
    	
    	
        try {
            /*releaseCameraAndPreview();
            Log.d(TAG, "released camera and preview");*/
            mCamera = Camera.open(cameraId);//?
            qOpened = (mCamera != null);
        } catch (Exception e) {
            Log.d(TAG, "failed to open Camera");
            e.printStackTrace();
        }
        Log.d(TAG, "open Camera");
        /*
        try {
			mCamera.setPreviewDisplay(mHolder);
		}
		catch(IOException e) {
			
			Log.e(TAG, "Failed to set preview display: " + e.getMessage());
			e.printStackTrace();
		}
		Log.d(TAG, "Had setPreviewDisplay");
		//if( start_preview ) {
        mCamera.startPreview();			//startCameraPreview();
		//}
        Log.d(TAG, "startPreview in camera Preview");*/
        Camera.Parameters parameters = mCamera.getParameters();
        sizes = parameters.getSupportedPictureSizes();
		
			for(int i=0;i<sizes.size();i++) {
	        	Camera.Size size = sizes.get(i);
	        	Log.d(TAG, "supported picture size: " + size.width + " , " + size.height);
			}
		
        return qOpened;    
    }

    private void releaseCameraAndPreview() {
       // mPreview.setCamera(null);
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }
    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
    	Log.d(TAG, "on surfaceCreate");
        try {
            mCamera.setPreviewDisplay(holder);
           // mCamera.startPreview(); no Skipping 33 frame warnning
        } catch (IOException e) {
            Log.d(TAG, "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    	Log.d(TAG, "on surfaceDestroyed");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.
    	Log.d(TAG, "on surfaceChanged");
        if (mHolder.getSurface() == null){
          // preview surface does not exist
        	Log.d(TAG, "mHolder getsurface null");
          return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e){
          // ignore: tried to stop a non-existent preview
        }
        
        // set preview size and make any resize, rotate or
        // reformatting changes here

        // start preview with new settings
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
        }
    }
}