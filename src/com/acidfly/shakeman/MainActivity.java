package com.acidfly.shakeman;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acidfly.shakeman.ShakeDetector.OnShakeListener;

public class MainActivity extends Activity implements SensorEventListener {
private ImageButton Silent;
public Camera camera= Camera.open();
public AudioManager mymanager;
//The following are used for the shake detection
private SensorManager mSensorManager;
private Sensor mAccelerometer;
public ContentResolver cResolver;
private ShakeDetector mShakeDetector;
public int check;
public boolean torchoff=true;
public boolean wifioff=true;
public boolean isdataoff=true;
public boolean isbrightoff=true;
private Object context;
public boolean a;
public long timeout= 300000;
public static boolean isService = false; 
PowerManager.WakeLock wl;
public View view2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
    	
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		TextView tv = (TextView) findViewById(R.id.textView1);
		Typeface face = Typeface.createFromAsset(getAssets(),
		            "fontm/gothic.ttf");
		tv.setTypeface(face);
		tv.setTextColor(Color.parseColor("#2171f6"));
		Silent=(ImageButton)findViewById(R.id.imageButton1);
		mymanager= (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		// ShakeDetector initialization		
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mShakeDetector = new ShakeDetector(); // SHAKE DETECTOR IS ANOTHER CLASS
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) { // IF PPHONE IS SHAKEN, CALL THESE FUNC ACCORDING TO VALUE OF CHECK INTEGER
                /*do things here*/
            
				if(check==1) { sil(count); }
				else if(check==2) { 
					if(isbrightoff==true){
						bright(count);
						isbrightoff=false;}
					else{
						nobright();
						isbrightoff=true;
					}
					}
				else if(check==3) { 
					if(isdataoff==true){
						data(count); 
						isdataoff=false;}
					else
					{
						dataoff();
						isdataoff=true;
					}
					}
				else if(check==4) { 
					if(wifioff==true)
				    {
					wifi(count); 
					wifioff=false;
					}
				    else
					{
					  wifiturnoff();
					  wifioff=true;}
					}
				else if(check==5) { 
					if(torchoff==true)
					{
					hotspot(count);
					torchoff=false;
					}
				else{
					turnoff();
					torchoff=true;
				}
					
				}
					
				
            }
            
        });
       
	}
	
	
	// INDIVIDUAL FUNCTIONS OF EACH BUTTON
	public void sil(int view){
		mymanager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
	}
	public void data(int view){
        ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(dataManager, true);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
    }
	public void dataoff(){
		ConnectivityManager dataManager;
        dataManager  = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        Method dataMtd = null;
        try {
            dataMtd = ConnectivityManager.class.getDeclaredMethod("setMobileDataEnabled", boolean.class);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        dataMtd.setAccessible(true);
        try {
            dataMtd.invoke(dataManager, false);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }   
	}
	public void hotspot(int view) 
	   {
		 //THIS IS ACTUALLY THE FLASH CODE! NEVERMIND THE METHOD NAME!!
		Parameters p = camera.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_TORCH);
	    camera.setParameters(p);
	    camera.startPreview();
	      
	   }
	public void turnoff(){ //turn the flashlight off!
		Parameters p = camera.getParameters();
		p.setFlashMode(Parameters.FLASH_MODE_OFF);
	    camera.setParameters(p);
	    camera.stopPreview();
	}
	public void wifi(int view) {
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(true);
	}
	public void wifiturnoff(){
		WifiManager wifiManager = (WifiManager) this
				.getSystemService(Context.WIFI_SERVICE);
		wifiManager.setWifiEnabled(false);
	}
	public void bright(int view){
		WindowManager.LayoutParams layout = getWindow().getAttributes();
	    layout.screenBrightness = 1F;
	    getWindow().setAttributes(layout);
	    cResolver = getContentResolver();
 		Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
	}
	
	public void nobright(){
		WindowManager.LayoutParams layout = getWindow().getAttributes();
	    layout.screenBrightness = 0F;
	    getWindow().setAttributes(layout);
	    cResolver = getContentResolver();
 		Settings.System.putInt(cResolver, Settings.System.SCREEN_BRIGHTNESS, 1);
	}
	
	// IF USER CLICKS ON A BUTTON, CHECK INTEGER IS SET TO A VALUE. IT IS USED TO COMPARE IN THE ONCREATE
	public void setsil(View view){
		Toast.makeText(getApplicationContext(), "Shake for silent is triggered", Toast.LENGTH_SHORT).show();
		 check=1;
	}
	public void setdat(View view){
		Toast.makeText(getApplicationContext(), "Shake for data is triggered", Toast.LENGTH_SHORT).show();
		 check=3;
	}
	public void setbright(View view){
		Toast.makeText(getApplicationContext(), "Shake for brightness is triggered", Toast.LENGTH_SHORT).show();
		 check=2;
	}
	public void setwifi(View view){
		Toast.makeText(getApplicationContext(), "Shake for wifi is triggered", Toast.LENGTH_SHORT).show();
		 check=4;
	}
	public void sethotspot(View view){
		Toast.makeText(getApplicationContext(), "Shake for flash light is triggered", Toast.LENGTH_SHORT).show();
		 check=5;
	}
	public void sethelp(View view){
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Your wish is my command.");
		alertDialog.setCancelable(false);
		alertDialog.setMessage("Greetings, human.\n\nI am glad that you have found a need for an entity like myself. I am multifaceted. I am capable of catering to your various needs, so choose the blue circle of your choice and vigourously (yet elegantly) shake the mechanation that you hold at this moment.\n\nI am omnipresent, but I advise you to keep me away from vicious killers(aka. Task Killer apps).\nI bid you fair tidings.\n\nAcidFly ©\n\n NOTE:\n\n(1) Works everywhere except when the screen is off. This includes the lockscreen too!\n\n(2) WiFi and mobile data take some time to turn on/off. Patience is the key.\n\n(3) Toggling is disabled for silent mode. You have to change it back manually.\n");
		alertDialog.setButton("That was not wierd.", new DialogInterface.OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
		      // TODO Add your code for the button here.
				dialog.cancel();
		   }
		});
		// Set the Icon for the Dialog
		alertDialog.show();
		TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
		Typeface face = Typeface.createFromAsset(getAssets(),
	            "fontm/gothic.ttf");
		textView.setTypeface(face);
		
		
	}

	@Override
    public void onResume() {
        super.onResume();
        // Add the following line to register the Session Manager Listener onResume
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_NORMAL);
        
        
    }
	@Override
	public void onBackPressed() {
	    // Do Here what ever you want do on back press;
	}
    @Override
    public void onPause() {
        // Add the following line to unregister the Sensor Manager onPause
        super.onPause();
        mSensorManager.registerListener(mShakeDetector, mAccelerometer,    SensorManager.SENSOR_DELAY_NORMAL);
    }
    
public void onAccuracyChanged(Sensor arg0, int arg1) {
		
		// TODO Auto-generated method stub
		
	}

//SAME FUNCTION THAT IS INSIDE ONCREATE IS PUT HERE.
    public void onSensorChanged(SensorEvent event) {
    	mShakeDetector = new ShakeDetector();
        mShakeDetector.setOnShakeListener(new OnShakeListener() {
            @Override
            public void onShake(int count) {
                /*do things here*/
            
				if(check==1) { sil(count); }
				else if(check==2) { bright(count); }
				else if(check==3) { data(count); }
				else if(check==4) { wifi(count); }
				else if(check==5) { hotspot(count); }
				
            }
            
        });
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}	
	
}
