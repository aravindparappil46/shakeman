package com.acidfly.shakeman;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

public class splash extends Activity {

    private static int SPLASH_TIME_OUT = 5000;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
       // TODO Auto-generated method stub
       super.onCreate(savedInstanceState);
       setContentView(R.layout.startup);

       String localUrl ="file:///android_asset/purple.swf";

       WebView wv=(WebView) findViewById(R.id.webView);
       wv.getSettings().setJavaScriptEnabled(true);
      
       wv.getSettings().setAllowFileAccess(true);
       wv.getSettings().setPluginState(PluginState.ON);
       wv.loadUrl(localUrl); 

       new Handler().postDelayed(new Runnable() {

           @Override
           public void run() { 
               Intent yes = new Intent(splash.this, MainActivity.class);
               startActivity(yes);
               finish();
           }
       }, SPLASH_TIME_OUT);
   }   
   @Override
   public void onBackPressed() {
       // Do Here what ever you want do on back press;
   }
}
