package com.example.uimanager;

import java.util.List;

import com.example.adapter.MyAdapter;
import com.example.interfaces.OnBackListener;
import com.example.model.FolderInfo;
import com.example.model.MusicInfo;
import com.example.music.MusicAPP;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.R;
import com.example.service.MusicControl;
import com.example.service.ServiceManager;
import com.example.storage.SPStorage;
import com.example.utils.MusicTimer;
import com.example.utils.MusicUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.test.IsolatedContext;
import android.util.Log;
import android.view.LayoutInflater;
/*
 * 这个类主要是用于管理Viewpager中第二个页面，歌曲列表的页面，也就是维护一个listView
 */
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyMusicManager
{
	private Activity mActivity;
	private LayoutInflater mInflater;
	private int mFrom;//记录这个音乐列表是从哪个界面请求的，可以冲我的音乐，文件夹，我的最爱3处地方打开音乐列表，ListView对于3处来的请求应该填充不同的内容
	private Object obj;//记录从文件夹，专辑，歌手进入的音乐列表时，传来的FolderInfo,ArtistInfo或者是AlbumInfo
	private ServiceManager serviceManager;
	private BottomManager bottomManager;
	private MusicTimer mMusicTimer;
	private ForMyMusicReceiver receiver;
	
	private MyAdapter myAdapter;
	public View contentView;
	
	private Handler mHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			try
			{
				bottomManager.refreshUI();
			} catch (RemoteException e){e.printStackTrace();}
		}	
	};
	
	
	public MyMusicManager(Activity activity,int from)
	{
		this.mActivity=activity;
		this.mInflater=activity.getLayoutInflater();
		this.mFrom=from;
		serviceManager=MusicAPP.serviceManager;
		
		receiver=new ForMyMusicReceiver();
		IntentFilter filter=new IntentFilter(IConstants.BROADCAST_NAME);
		mActivity.registerReceiver(receiver, filter);
		
	}
	public View getView(Object obj) throws RemoteException
	{
		this.obj=obj;
		contentView=mInflater.inflate(R.layout.mymusic, null);

		initView(contentView);
		bottomManager=new BottomManager(mActivity, contentView);
		mMusicTimer=new MusicTimer(mHandler);
		
		Intent intent=new Intent(IConstants.BROADCAST_NAME);
		mActivity.sendBroadcast(intent);
		
		return contentView;
	}
	
	
	public void initView(View view) throws RemoteException
	{
		
		ListView listView=(ListView) view.findViewById(R.id.music_listview);//initView的主角，就是要初始化它
		myAdapter=new MyAdapter(mActivity);
		listView.setAdapter(myAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				// TODO Auto-generated method stub
				try
				{
					myAdapter.refreshPlayingList();
					serviceManager.playById(myAdapter.getData().get(arg2).songid);
				} catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		if(mFrom==IConstants.START_FROM_LOCAL)
		{
			List<MusicInfo> list=MusicUtils.queryMusic(mActivity);//获取数据库中的音乐列表
			myAdapter.setData(list);
		}
		else if(mFrom==IConstants.START_FROM_FAVORITE)
		{
			List<MusicInfo> list=MusicUtils.queryFavorite(mActivity);
			myAdapter.isForFavoriteBrowser=true;
			myAdapter.setData(list);
		}
		else if(mFrom==IConstants.START_FROM_FOLDER)
		{
			List<MusicInfo> list=MusicUtils.queryMusic(mActivity, mFrom, obj);
			myAdapter.setData(list);
		}
		else if(mFrom==IConstants.START_FROM_ARTIST)
		{
			List<MusicInfo> list=MusicUtils.queryMusic(mActivity, mFrom, obj);
			myAdapter.setData(list);
		}
		else if(mFrom==IConstants.START_FROM_ALBUM)
		{
			List<MusicInfo> list=MusicUtils.queryMusic(mActivity, mFrom, obj);
			myAdapter.setData(list);
		}
	}

	private class ForMyMusicReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			try
			{
				if(arg1.getAction().equals(IConstants.BROADCAST_NAME))
				{
					int playState=arg1.getIntExtra("playstate", IConstants.MPS_NOFILE);
					int curMusicIndex=arg1.getIntExtra("curMusicIndex", -1);
					MusicInfo curMusic=arg1.getParcelableExtra("curMusic");
					if(curMusic==null)//这是MyMusic初始化时调用的，调用全局来获取更新
					{
						if(serviceManager.server!=null)
						{
							bottomManager.refreshUI();
							if(serviceManager.getCurMusic()!=null)
								bottomManager.refreshTV(serviceManager.getCurMusic().musicname, serviceManager.getCurMusic().artist,
										serviceManager.getCurMusic().albumid);
							myAdapter.setCurMusicIndex(serviceManager.getCurMusicIndex());
							myAdapter.setPlayState(serviceManager.getPlayState());
							myAdapter.notifyDataSetChanged();
							
							if (serviceManager.getPlayState() == IConstants.MPS_PLAYING)
							{
								mMusicTimer.startTimer();
							} else if (serviceManager.getPlayState() == IConstants.MPS_PAUSE)
							{
								mMusicTimer.stopTimer();
							}
						}
					}
					else//响应按音乐时有信息传来的情况
					{
						myAdapter.setCurMusicIndex(curMusicIndex);
						myAdapter.setPlayState(playState);
						myAdapter.notifyDataSetChanged();
						
						bottomManager.refreshUI();
						bottomManager.refreshTV(curMusic.musicname,curMusic.artist,curMusic.albumid);
						if (playState == IConstants.MPS_PLAYING)
						{
							mMusicTimer.startTimer();
						} else if (playState == IConstants.MPS_PAUSE)
						{
							mMusicTimer.stopTimer();
						}	
					}
				}
			} catch (RemoteException e){e.printStackTrace();}	
		}
		
	}

	public void unregisterReceiver()
	{
		// TODO Auto-generated method stub
		if(receiver!=null)
		{
			mActivity.unregisterReceiver(receiver);
		}
	}
	

}
