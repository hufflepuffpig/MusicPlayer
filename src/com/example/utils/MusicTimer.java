package com.example.utils;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Handler;
import android.os.Message;

public class MusicTimer
{
	private Handler[] handlers=null;
	private Timer mTimer;
	private boolean isStartTimer=false;
	MyTimerTask mTimerTask=null;
	
	public MusicTimer(Handler...handlers)
	{
		this.handlers=handlers;
		this.mTimer=new Timer();
	}
	
	public void startTimer()
	{
		if(handlers==null || isStartTimer)
		{
			return;
		}
		mTimerTask=new MyTimerTask();
		mTimer.schedule(mTimerTask, 1000, 1000);
		isStartTimer=true;
	}
	public void stopTimer()
	{
		if(!isStartTimer)
		{
			return;
		}
		if(mTimerTask!=null)
		{
			mTimerTask.cancel();
			mTimerTask=null;
			isStartTimer=false;
		}
	}
	
	private class MyTimerTask extends TimerTask
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			for(Handler handler:handlers)
			{
				Message msg=handler.obtainMessage(0);
				msg.sendToTarget();
			}
		}
		
	}
	
}
