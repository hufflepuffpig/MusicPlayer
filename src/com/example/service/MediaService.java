package com.example.service;

import java.util.List;

import com.example.aidl.IMediaService;
import com.example.model.MusicInfo;
import com.example.music.MusicAPP;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.RemoteViews;

public class MediaService extends Service
{
	private static MediaServer mediaServer;
	private ForNotificationReceiver myMusicReceiver=null;
	@Override
	public IBinder onBind(Intent arg0)
	{
		// TODO Auto-generated method stub
		if(mediaServer==null)
		{
			mediaServer=new MediaServer();
		}
		return mediaServer;
	}

	class MediaServer extends IMediaService.Stub //真正的服务端
	{
		private MusicControl mMc=new MusicControl(getApplicationContext());
		@Override
		public void refreshMusicList(List<MusicInfo> infos)
				throws RemoteException
		{
			// TODO Auto-generated method stub
			mMc.refreshMusicList(infos);
		}

		@Override
		public boolean playById(int id) throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.playById(id);
		}

		@Override
		public boolean prepare() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.prepare();
		}

		@Override
		public boolean replay() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.replay();
		}

		@Override
		public boolean pause() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.pause();
		}

		@Override
		public boolean prev() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.prev();
		}

		@Override
		public boolean next() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.next();
		}
		
		public void updateNotification(String musicname,String artist)
		{
			MediaService.this.updateNotification(musicname, artist);
		}

		
		@Override
		public void cancelNotification() throws RemoteException
		{
			// TODO Auto-generated method stub
			MediaService.this.cancelNotification();
		}

		
		@Override
		public int getPlayState() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getPlayState();
		}

		@Override
		public int getPalyMode() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getPalyMode();
		}

		@Override
		public MusicInfo getCurMusic() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getCurMusic();
		}

		@Override
		public List<MusicInfo> getCurMusicList() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getCurMusicList();
		}

		@Override
		public int getPosition() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getPosition();
		}

		@Override
		public int getDuration() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getDuration();
		}

		@Override
		public boolean seekTo(int progress) throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.seekTo(progress);
		}

		@Override
		public void sendMyMusicBroadcast() throws RemoteException
		{
			// TODO Auto-generated method stub
			mMc.sendMyMusicBroadcast();
		}

		@Override
		public void exit() throws RemoteException
		{
			// TODO Auto-generated method stub
			mMc.exit();
		}

		@Override
		public int getCurMusicIndex() throws RemoteException
		{
			// TODO Auto-generated method stub
			return mMc.getCurMusicIndex();
		}

		@Override
		public void setPlayMode(int mode) throws RemoteException
		{
			// TODO Auto-generated method stub
			mMc.setPlayMode(mode);
		}
	}

	public void updateNotification(String musicname,String artist)
	{
		Notification notification=new Notification();
		//设置Notification的status的内容
		notification.icon=R.drawable.ic_launcher;
		notification.tickerText=musicname;
		//设置Notification的下拉下来展示的内容
		RemoteViews rv=new RemoteViews(getPackageName(), R.layout.notification);
		rv.setTextViewText(R.id.notification_musicname_tv, "正在播放: "+musicname);
		rv.setTextViewText(R.id.notification_artist_tv, artist);
		notification.contentView=rv;
		//设置按Notification时的跳转
		Intent intent=new Intent(getApplicationContext(), MainActivity.class);
		PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);
		notification.contentIntent=pendingIntent;
		
		startForeground(1, notification);
	}
	public void cancelNotification()
	{
		stopForeground(true);
	}


	private class ForNotificationReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			// TODO Auto-generated method stub
			if(arg1.getAction().equals(IConstants.BROADCAST_NAME))
			{
				int playstate=arg1.getIntExtra("playstate", IConstants.MPS_NOFILE);
				MusicInfo curMusic=null;
				if(playstate!=IConstants.MPS_NOFILE)
				{
					curMusic=arg1.getParcelableExtra("curMusic");
				}
				
				if(playstate==IConstants.MPS_PLAYING)
				{
					try
					{
						MusicAPP.serviceManager.updateNotification(curMusic.musicname, curMusic.artist);
					} catch (RemoteException e){e.printStackTrace();}
				}
				else if(playstate==IConstants.MPS_PAUSE)
				{
					try
					{
						MusicAPP.serviceManager.cancelNotification();
					} catch (RemoteException e){e.printStackTrace();}
				}
			}
		}
		
	}
	@Override
	public void onCreate()
	{
		// TODO Auto-generated method stub
		super.onCreate();
		myMusicReceiver=new ForNotificationReceiver();
		IntentFilter intentFilter=new IntentFilter(IConstants.BROADCAST_NAME);
		this.registerReceiver(myMusicReceiver, intentFilter);
	}
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(myMusicReceiver);
	}

}
