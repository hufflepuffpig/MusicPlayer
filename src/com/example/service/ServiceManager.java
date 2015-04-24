package com.example.service;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;

import com.example.aidl.IMediaService;
import com.example.model.MusicInfo;

public class ServiceManager
{
	public IMediaService server=null;
	private Context mContext;
	private ServiceConnection connection=new ServiceConnection()
	{
		
		@Override
		public void onServiceDisconnected(ComponentName arg0)
		{
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1)
		{
			// TODO Auto-generated method stub
			server=IMediaService.Stub.asInterface(arg1);
		}
	};

	public ServiceManager(Context context)
	{
		this.mContext=context;
	}
	
	public void connectService()
	{
		Intent intent=new Intent("com.example.service.MediaService");
		mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
	}
	
	public void refreshMusicList(List<MusicInfo> infos) throws RemoteException
	{
		server.refreshMusicList(infos);
	}
	
	public void updateNotification(String musicname,String artist) throws RemoteException
	{
		server.updateNotification(musicname, artist);
	}
	public void cancelNotification() throws RemoteException
	{
		server.cancelNotification();
	}
	
	public boolean playById(int id) throws RemoteException
	{
		return server.playById(id);
	}
	public boolean prepare() throws RemoteException
	{
		return server.prepare();
	}
	public boolean replay() throws RemoteException
	{
		return server.replay();
	}
	public boolean pause() throws RemoteException
	{
		return server.pause();
	}
	public boolean prev() throws RemoteException
	{
		return server.prev();
	}
	public boolean next() throws RemoteException
	{
		return server.next();
	}
	public void sendMyMusicBroadcast() throws RemoteException
	{
		server.sendMyMusicBroadcast();
	}

	public int getPlayState() throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.getPlayState();
	}
	public int getPalyMode() throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.getPalyMode();
	}
	public void setPlayMode(int mode) throws RemoteException
	{
		// TODO Auto-generated method stub
		server.setPlayMode(mode);
	}
	public int getCurMusicIndex() throws RemoteException
	{
		return server.getCurMusicIndex();
	}
	public MusicInfo getCurMusic() throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.getCurMusic();
	}
	public List<MusicInfo> getCurMusicList() throws RemoteException
	{
		return server.getCurMusicList();
	}

	public int getPosition() throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.getPosition();
	}
	public int getDuration() throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.getDuration();
	}
	public boolean seekTo(int progress) throws RemoteException
	{
		// TODO Auto-generated method stub
		return server.seekTo(progress);
	}

	public void exit() throws RemoteException
	{
		server.exit();
		mContext.unbindService(connection);
	}
}
