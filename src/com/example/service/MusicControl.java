package com.example.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

import com.example.model.MusicInfo;
import com.example.musicplayer.IConstants;
import com.example.utils.MusicUtils;
/*
 * 封装MediaPlayer
 */
public class MusicControl
{
	private List<MusicInfo> mMusicInfos;//播放音乐列表，所有有关的音乐信息全在里面
	private int mPlayState;//播放状态，没文件，准备好，正在播放，暂停之类
	private int mPlayMode;//单曲循环，顺序播放，随机播放之类
	private int mCurMusicId;
	private int mCurMusicIndex;
	private MediaPlayer mMediaPlayer;//主要播放器
	private MusicInfo mCurMusic;
	private Context mContext;
	
	public MusicControl(Context context)
	{
		mContext=context;
		mMediaPlayer=new MediaPlayer();
		mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
		{
			
			@Override
			public void onCompletion(MediaPlayer arg0)
			{
				// TODO Auto-generated method stub
				if(mPlayMode==IConstants.MPM_LIST_LOOP_PLAY)
					next();
				else if(mPlayMode==IConstants.MPM_SINGLE_LOOP_PLAY)
					replay();
				else if(mPlayMode==IConstants.MPM_ORDER_PLAY)
				{
					if(mCurMusicIndex==mMusicInfos.size()-1)
					{
						pause();
					}		
				}
				else if(mPlayMode==IConstants.MPM_RANDOM_PLAY)
				{
					Random random=new Random();
					playByIndex(random.nextInt(mMusicInfos.size()));
				}
			}
		});
		
		mMusicInfos=new ArrayList<>();
		mCurMusic=null;
		mPlayMode=IConstants.MPM_LIST_LOOP_PLAY;
		mPlayState=IConstants.MPS_NOFILE;
		mCurMusicId=-1;
		mCurMusicIndex=-1;
	}
	//基本的播放暂停功能
	public void refreshMusicList(List<MusicInfo> infos)
	{
		this.mMusicInfos.clear();
		mMusicInfos.addAll(infos);
		if(mMusicInfos.size()==0)
		{
			mPlayState=IConstants.MPS_NOFILE;
		}
	}
	
	public boolean playById(int id)
	{
		mCurMusicIndex=MusicUtils.seekPosInListById(mMusicInfos, id);
		if(mCurMusicId==id)
		{
			if(!mMediaPlayer.isPlaying())
			{
				mMediaPlayer.start();
				mPlayState=IConstants.MPS_PLAYING;
			}else {
				pause();
			}
			sendMyMusicBroadcast();
			return true;
		}
		else {
			mCurMusicId=id;
			prepare();
			replay();
			return true;
		}
		
	}
	private boolean playByIndex(int index)
	{
		int id=mMusicInfos.get(index).songid;
		mCurMusicIndex=index;
		if(mCurMusicId==id)
		{
			if(!mMediaPlayer.isPlaying())
			{
				mMediaPlayer.start();
				mPlayState=IConstants.MPS_PLAYING;
			}else {
				pause();
			}
			sendMyMusicBroadcast();
			return true;
		}
		else {
			mCurMusicId=id;
			prepare();
			replay();
			return true;
		}
	}
	
	public boolean prepare()
	{
		try
		{
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(mMusicInfos.get(mCurMusicIndex).data);
			mMediaPlayer.prepare();
			mCurMusic=mMusicInfos.get(mCurMusicIndex);
			mPlayState=IConstants.MPS_PREPARE;
		} catch (Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean replay()
	{
		if(mPlayState==IConstants.MPS_NOFILE || mPlayState==IConstants.MPS_INVALID)
			return false;
		mMediaPlayer.start();
		mPlayState=IConstants.MPS_PLAYING;
		sendMyMusicBroadcast();
		return true;
	}
	public boolean pause()
	{
		if(mPlayState!=IConstants.MPS_PLAYING)
			return false;
		mMediaPlayer.pause();
		mPlayState=IConstants.MPS_PAUSE;
		sendMyMusicBroadcast();
		return true;
	}
	public boolean prev()
	{
		if(mPlayState==IConstants.MPS_NOFILE)
			return false;
		mCurMusicIndex=(mCurMusicIndex-1)%mMusicInfos.size();
		if(!prepare())
			return false;
		return replay();
	}
	public boolean next()
	{
		if(mPlayState==IConstants.MPS_NOFILE)
			return false;
		mCurMusicIndex=(mCurMusicIndex+1)%mMusicInfos.size();
		if(!prepare())
			return false;
		return replay();
	}
	//广播，把service提升为前台服务，避免一段时间后被杀死
	public void sendMyMusicBroadcast()
	{
		Intent intent=new Intent(IConstants.BROADCAST_NAME);
		intent.putExtra("playstate", mPlayState);
		intent.putExtra("curMusicIndex", mCurMusicIndex);
		intent.putExtra("curMusic", mMusicInfos.get(mCurMusicIndex));
		mContext.sendBroadcast(intent);
	}
	//获取播放器各种状态信息
	public int getPlayState()
	{
		return mPlayState;
	}
	public int getPalyMode()
	{
		return mPlayMode;
	}
	public void setPlayMode(int mode)
	{
		mPlayMode=mode;
	}
	public int getCurMusicIndex()
	{
		return mCurMusicIndex;
	}
	public MusicInfo getCurMusic()
	{
		return mCurMusic;
	}
	public List<MusicInfo> getCurMusicList()
	{
		return mMusicInfos;
	}
	//和快进/后退有关
	public int getPosition()
	{
		if(mPlayState==IConstants.MPS_PLAYING || mPlayState==IConstants.MPS_PAUSE)
			return mMediaPlayer.getCurrentPosition();
		else {
			return 0;
		}
	}
	public int getDuration()
	{
		if(mPlayState==IConstants.MPS_INVALID || mPlayState==IConstants.MPS_NOFILE)
		{
			return 0;
		}else {
			return mMediaPlayer.getDuration();
		}
	}
	public boolean seekTo(int progress)
	{
		if(mPlayState==IConstants.MPS_NOFILE || mPlayState==IConstants.MPS_INVALID)
		{
			return false;
		}
		progress=progress%100;
		mMediaPlayer.seekTo(progress*mMediaPlayer.getDuration()/100);
		return true;
	}

	public void exit()
	{
		mMediaPlayer.pause();
		mMediaPlayer.release();
		mCurMusicIndex=-1;
		mMusicInfos.clear();
	}
}
