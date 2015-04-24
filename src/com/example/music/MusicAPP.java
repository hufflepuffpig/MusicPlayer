package com.example.music;

import com.example.service.MusicControl;
import com.example.service.ServiceManager;
import com.example.storage.SPStorage;

import android.app.Application;

public class MusicAPP extends Application
{

	public static ServiceManager serviceManager;
	private SPStorage spStorage;
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		//第一次初始化bg为004.jpg
		spStorage=new SPStorage(this);
		if(spStorage.getBgPath()==null)
			spStorage.setBgPath("bkgs/004.jpg");
		
		serviceManager=new ServiceManager(getApplicationContext());
		serviceManager.connectService();
	}

}
