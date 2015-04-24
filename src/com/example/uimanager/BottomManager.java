package com.example.uimanager;

import com.example.music.MusicAPP;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.service.ServiceManager;
import com.example.utils.MusicUtils;
import com.example.views.AlwaysMarqueeView;

import android.app.Activity;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

public class BottomManager
{
	private Activity mActivity;
	private View mBottomView;
	private SeekBar mSeekBar;
	private ImageButton mPlayButton,mPauseButton,mNextButton,mMenuButton;
	private AlwaysMarqueeView musicname_tv2,artist_tv2,duration_tv2;
	private ImageView headicon;
	private ServiceManager serviceManager;
	
	public BottomManager(Activity activity,View view)
	{
		this.mActivity=activity;
		this.mBottomView=view;
		this.serviceManager=MusicAPP.serviceManager;
		
		this.mSeekBar=(SeekBar) mBottomView.findViewById(R.id.playback_SeekBar2);
		mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
		{
			
			@Override
			public void onStopTrackingTouch(SeekBar arg0)
			{
				// TODO Auto-generated method stub
				try
				{
					if(serviceManager.getPlayState()==IConstants.MPS_PLAYING || serviceManager.getPlayState()==IConstants.MPS_PAUSE)
						MusicAPP.serviceManager.seekTo(arg0.getProgress());
					
				} catch (RemoteException e){e.printStackTrace();}
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar arg0)
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2)
			{

			}
		});
		
		this.mPlayButton=(ImageButton) mBottomView.findViewById(R.id.btn_play2);
		mPlayButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				try
				{
					if(serviceManager.getPlayState()==IConstants.MPS_PAUSE)
					{
						serviceManager.replay();
					}
				} catch (RemoteException e){e.printStackTrace();}
			}
		});
		
		this.mPauseButton=(ImageButton) mBottomView.findViewById(R.id.btn_pause2);
		mPauseButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				try
				{
					if(serviceManager.getPlayState()==IConstants.MPS_PLAYING)
					{
						serviceManager.pause();
					}
				} catch (RemoteException e){e.printStackTrace();}				
			}
		});
		
		this.mNextButton=(ImageButton) mBottomView.findViewById(R.id.btn_playnNext2);
		mNextButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				try
				{
					if(serviceManager.getPlayState()==IConstants.MPS_PLAYING || serviceManager.getPlayState()==IConstants.MPS_PAUSE)
					{
						serviceManager.next();
					}
				} catch (RemoteException e){e.printStackTrace();}	
			}
		});
	
		this.mMenuButton=(ImageButton) mBottomView.findViewById(R.id.btn_menu2);
		mMenuButton.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				if(((MainActivity)mActivity).slidingMenu.isMenuShowing())
				{
					((MainActivity)mActivity).slidingMenu.showContent();
				}else {
					((MainActivity)mActivity).slidingMenu.showMenu();
				}
			}
		});
		
		this.musicname_tv2=(AlwaysMarqueeView) mBottomView.findViewById(R.id.musicname_tv2);
		this.artist_tv2=(AlwaysMarqueeView) mBottomView.findViewById(R.id.artist_tv2);
		this.duration_tv2=(AlwaysMarqueeView) mBottomView.findViewById(R.id.duration_tv2);
		this.headicon=(ImageView) mBottomView.findViewById(R.id.headicon_iv);
	}


	public void refreshUI() throws RemoteException
	{
		if(serviceManager.server!=null)
		{
			//更新暂停/播放按钮的状态
			if (serviceManager.getPlayState() == IConstants.MPS_PLAYING)
			{
				mPlayButton.setVisibility(View.INVISIBLE);
				mPauseButton.setVisibility(View.VISIBLE);
			} else
			{
				mPlayButton.setVisibility(View.VISIBLE);
				mPauseButton.setVisibility(View.INVISIBLE);
			}
			//更新Seekbar的状态
			int pos = serviceManager.getPosition() / 1000;
			int duration = serviceManager.getDuration() / 1000;
			if (serviceManager.getPlayState() == IConstants.MPS_PLAYING
					|| serviceManager.getPlayState() == IConstants.MPS_PAUSE)
			{
				mSeekBar.setProgress(pos * 100 / duration);
			}
			//更新时间
			duration_tv2.setText(MusicUtils.makeTimeString(serviceManager.getPosition()));
		}
		
	}
	public void refreshTV(String musicname,String artist,int albumid)
	{
		musicname_tv2.setText(musicname);
		artist_tv2.setText(artist);
		headicon.setImageBitmap(MusicUtils.getArtWorkFromCache(mActivity, albumid));
	}
	
}
