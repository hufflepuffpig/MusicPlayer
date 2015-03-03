package com.example.musicplayer;

import com.example.utils.SplashScreen;

import android.R.integer;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity
{

	private SplashScreen splashScreen;
	private Handler mhandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			// TODO Auto-generated method stub
			if(msg.what==1)
			{
				splashScreen.remove();
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		splashScreen=new SplashScreen(this);
		splashScreen.show(R.drawable.image_splash_background, SplashScreen.SLIDE_UP);
		getData();
	}

	public void getData()
	{
		
		mhandler.sendEmptyMessageDelayed(1, 3000);
	}
}
