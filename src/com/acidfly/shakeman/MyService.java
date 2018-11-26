package com.acidfly.shakeman;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.FloatMath;
import android.view.View;
import android.widget.Toast;


public class MyService extends Service implements SensorEventListener {
  /* private final IBinder mBinder = new LocalBinder();
   public class LocalBinder extends Binder {
		MyService getService() {
			return MyService.this;
		}
	}
   private  SensorManager mSensorManager;
   private  Sensor mAccelerometer;
   PowerManager.WakeLock wl;
   
	
   @Override
   
   public int onStartCommand(Intent intent, int flags, int startId) {
      // Let it continue running until it is stopped.
	   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		 wl = pm.newWakeLock( PowerManager.FULL_WAKE_LOCK, "My wakelook");
		 wl.acquire();
	   mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
     mAccelerometer = mSensorManager
             .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);      
     mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_GAME);
     Toast.makeText(this, "I will run in background", Toast.LENGTH_LONG).show();
      return START_STICKY;
   }
   
   @Override
   public void onAccuracyChanged(Sensor sensor,int accuracy){
	   // TODO Auto-generated method stub
   }
   
   @Override
   public void onDestroy() {	
		stopSelf();
		wl.release();
		mSensorManager.unregisterListener(this);
      Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
 }@Override
   public IBinder onBind(Intent arg0) {
	      return mBinder;
	   }
}*/
	
	 private static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
	    private static final int SHAKE_SLOP_TIME_MS = 500;
	    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;
	    private  SensorManager mSensorManager;
	    private  Sensor mAccelerometer;
	    private MyService mShakeDetector;
	    PowerManager.WakeLock wl;
	    private SensorEvent event;
	 
	    private OnShakeListener mListener;
	    private long mShakeTimestamp;
	    private int mShakeCount;

	    public void setOnShakeListener(OnShakeListener listener) {
	        this.mListener = listener;
	    }
	 
	    public interface OnShakeListener {
	        public void onShake(int count);
	    }
	 
	    @Override
	    public void onAccuracyChanged(Sensor sensor, int accuracy) {
	        // ignore
	    }
	    @Override
	    
	    public int onStartCommand(Intent intent, int flags, int startId) {
	       // Let it continue running until it is stopped.
	 	   PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
	 		 wl = pm.newWakeLock( PowerManager.PARTIAL_WAKE_LOCK, "My wakelook");
	 		 wl.acquire();

	 	   mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	      mAccelerometer = mSensorManager
	              .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);      
	      mShakeDetector = new MyService();
	      mSensorManager.registerListener(this, mAccelerometer,SensorManager.SENSOR_DELAY_UI);
	      Toast.makeText(this, "I will run in background", Toast.LENGTH_LONG).show();
	      return START_STICKY;
	    }

	    
	    @Override
	    public void onDestroy() {	
	 		stopSelf();
	 		wl.release();
	 		mSensorManager.unregisterListener(this);
	       Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
	  }
	    @Override
	    public void onSensorChanged(SensorEvent event) {
	 
	        if (mListener != null) {
	            float x = event.values[0];
	            float y = event.values[1];
	            float z = event.values[2];
	 
	            float gX = x / SensorManager.GRAVITY_EARTH;
	            float gY = y / SensorManager.GRAVITY_EARTH;
	            float gZ = z / SensorManager.GRAVITY_EARTH;
	 
	            // gForce will be close to 1 when there is no movement.
	            float gForce = FloatMath.sqrt(gX * gX + gY * gY + gZ * gZ);
	 
	            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
	                final long now = System.currentTimeMillis();
	                // ignore shake events too close to each other (500ms)
	                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
	                    return;
	                }
	 
	                // reset the shake count after 3 seconds of no shakes
	                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
	                    mShakeCount = 0;
	                }
	 
	                mShakeTimestamp = now;
	                mShakeCount++;
	 
	                mListener.onShake(mShakeCount);
	            }
	        }
	    }
	    @Override
	    public IBinder onBind(Intent arg0) {
	 	      return null;
	 	   }
	}