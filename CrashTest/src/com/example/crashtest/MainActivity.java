package com.example.crashtest;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.unity3d.player.UnityPlayer;



import android.R.integer;
import android.R.string;
import android.os.Bundle;
import android.os.Debug;
import android.os.Environment;
import android.os.StrictMode;
import android.app.Activity;
import android.support.v4.util.LogWriter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends Activity {

	string a;
	private UnityPlayer mUnityPlayer;
	private CrashException crashException;
	private Button buttonUpLoad = null;  
	private Button buttonDownLoad = null;  
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        crashException = CrashException.getInstance();
        crashException.init(this);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        mUnityPlayer = new UnityPlayer(this);
//        if (mUnityPlayer.getSettings ().getBoolean ("hide_status_bar", true))
//            getWindow ().setFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                                   WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
//        int glesMode = mUnityPlayer.getSettings().getInt("gles_mode", 1);
//        boolean trueColor8888 = false;
//        mUnityPlayer.init(glesMode, trueColor8888);
//
//        View playerView = mUnityPlayer.getView();
//        setContentView(playerView);
//        playerView.requestFocus();
        buttonUpLoad = (Button)findViewById(R.id.upLoad);
        buttonUpLoad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
		
			}
		});
      
        buttonDownLoad = (Button)findViewById(R.id.saveFile);
        buttonDownLoad.setOnClickListener(new OnClickListener() {
        	int m =0;
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				m++;
				ExceptionInfo(m+"AAAAAAAAAAAAAAAAAAAA");
			}
		}  );
        if (android.os.Build.VERSION.SDK_INT > 9) {  
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();  
            StrictMode.setThreadPolicy(policy);  
        }  
    }
    
    public void ExceptionInfo(String exceptionInfo) {
    	
    	 Log.e("AAAA",exceptionInfo);
    	// crashException.WriteFile(exceptionInfo);
    	// System.out.print(a.equals("ASa"));
    	List<String> array =new ArrayList<String>();
    	array.add("AAAA");
    	array.remove(0);
    	array = null;
    	String b= array.get(0);
	}

    public void ExceptionInfo1() {
    	
      System.out.print(a.equals("ASa"));
	}



    
}
