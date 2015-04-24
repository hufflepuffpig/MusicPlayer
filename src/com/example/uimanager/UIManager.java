package com.example.uimanager;

import java.util.ArrayList;
import java.util.List;

import com.example.interfaces.OnBackListener;
import com.example.model.FolderInfo;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.storage.SPStorage;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.RemoteException;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
 * 这个类负责管理MainFragment中View的Viewpager，根据选择不同的功能管理ViewPager显示不同的界面
 */
public class UIManager implements OnBackListener
{
	private Activity mActivity;
	private View mView;
	private LayoutInflater mInflater;
	private ForBackGroundChangeReceiver receiver;
	
	private ViewPager mViewPager,mViewPagerSub;
	private List<View> list_views,list_views_sub;
	
	private MyMusicManager myMusicManager=null;//如果第一ViewPager中出现了我的音乐页面，保存在这个对象中，在ViewPager remove views时利用该对象注销其中的广播，该广播用于接收Intent更新BottomManger画面
	private MyMusicManager myMusicManagersub=null;//同上
	
	private FolderBrowserManager folderBrowserManager=null;
	private ArtistBrowserManager artistBrowserManager=null;
	private AlbumBrowserManager albumBrowserManager=null;
	
	public UIManager(Activity activity,View view)
	{
		this.mActivity=activity;
		this.mView=view;
		this.mInflater=activity.getLayoutInflater();
		
		mViewPager=(ViewPager) mView.findViewById(R.id.viewPager);
		list_views=new ArrayList<View>();
		mViewPagerSub=(ViewPager) mView.findViewById(R.id.viewPagerSub);
		list_views_sub=new ArrayList<>();
		
		receiver=new ForBackGroundChangeReceiver();
		IntentFilter filter=new IntentFilter(IConstants.BROADCAST_CHANGEBG);
		mActivity.registerReceiver(receiver, filter);
	}
	
	public void setContent(int from) throws RemoteException
	{
		((MainActivity)mActivity).registerOnBackListener(this);
		if(from==IConstants.START_FROM_LOCAL)
		{
			View transView1=mInflater.inflate(R.layout.viewpager_trans_layout, null);//viewPager的第一个页面
			myMusicManager=new MyMusicManager(mActivity,from);
			View contentView1=myMusicManager.getView(null);
			initBg(contentView1);
			
			list_views.clear();
			list_views.add(transView1);
			list_views.add(contentView1);
		}
		else if(from==IConstants.START_FROM_FAVORITE)
		{
			View transView2=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			myMusicManager=new MyMusicManager(mActivity, from);
			View contentView2=myMusicManager.getView(null);
			initBg(contentView2);
			
			list_views.clear();
			list_views.add(transView2);
			list_views.add(contentView2);
		}
		else if(from==IConstants.START_FROM_FOLDER)
		{
			View transView3=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			folderBrowserManager=new FolderBrowserManager(mActivity,this);
			View contentView3=folderBrowserManager.getView();
			initBg(contentView3);
			
			list_views.clear();
			list_views.add(transView3);
			list_views.add(contentView3);
		}
		else if(from==IConstants.START_FROM_ARTIST)
		{
			View transView4=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			artistBrowserManager=new ArtistBrowserManager(mActivity,this);
			View contentView4=artistBrowserManager.getView();
			initBg(contentView4);
			
			list_views.clear();
			list_views.add(transView4);
			list_views.add(contentView4);
		}
		else if(from==IConstants.START_FROM_ALBUM)
		{
			View transView5=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			albumBrowserManager=new AlbumBrowserManager(mActivity,this);
			View contentView5=albumBrowserManager.getView();
			initBg(contentView5);
			
			list_views.clear();
			list_views.add(transView5);
			list_views.add(contentView5);
		}
		mViewPager.removeAllViews();
		MyPagerAdapter myPagerAdapter=new MyPagerAdapter(list_views);
		mViewPager.setAdapter(myPagerAdapter);
		mViewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPager.setCurrentItem(1);
		mViewPager.setVisibility(View.VISIBLE);
	}
	
	public void setContentSub(int from,Object obj) throws RemoteException
	{
		((MainActivity)mActivity).registerOnBackListener(this);
		if(from==IConstants.FOLDER_TO_MYMUSIC)
		{
			View transview=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			myMusicManagersub=new MyMusicManager(mActivity, IConstants.START_FROM_FOLDER);
			View contentView=myMusicManagersub.getView(obj);
			initBg(contentView);
			
			list_views_sub.clear();
			list_views_sub.add(transview);
			list_views_sub.add(contentView);
		}
		else if(from==IConstants.ARTIST_TO_MYMUSIC)
		{
			View transView=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			myMusicManagersub=new MyMusicManager(mActivity, IConstants.START_FROM_ARTIST);
			View contentView=myMusicManagersub.getView(obj);
			initBg(contentView);
			
			list_views_sub.clear();
			list_views_sub.add(transView);
			list_views_sub.add(contentView);
		}
		else if(from==IConstants.ALBUM_TO_MYMUSIC)
		{
			View transView=mInflater.inflate(R.layout.viewpager_trans_layout, null);
			myMusicManagersub=new MyMusicManager(mActivity, IConstants.START_FROM_ALBUM);
			View contentView=myMusicManagersub.getView(obj);
			initBg(contentView);
			
			list_views_sub.clear();
			list_views_sub.add(transView);
			list_views_sub.add(contentView);
		}
		mViewPagerSub.removeAllViews();
		MyPagerAdapter myPagerAdapter=new MyPagerAdapter(list_views_sub);
		mViewPagerSub.setAdapter(myPagerAdapter);
		mViewPagerSub.setOnPageChangeListener(new MyOnPageChangeListener());
		mViewPagerSub.setCurrentItem(1);
		mViewPagerSub.setVisibility(View.VISIBLE);
	}
	
	private class MyPagerAdapter extends PagerAdapter
	{
		private List<View> views;
		public MyPagerAdapter(List<View> views)
		{
			this.views=views;
		}
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return views.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			// TODO Auto-generated method stub
			return arg0==arg1;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView(views.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position)
		{
			container.addView(views.get(position));
			return views.get(position);
		}
		
	}
	private class MyOnPageChangeListener implements ViewPager.OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
			// TODO Auto-generated method stub
			if(arg0==0 && mViewPager.getCurrentItem()==0)
			{
				mViewPager.removeAllViews();
				mViewPager.setVisibility(View.INVISIBLE);
				if(myMusicManager!=null)
				{
					myMusicManager.unregisterReceiver();
					myMusicManager=null;
				}
				if(folderBrowserManager!=null)
					folderBrowserManager=null;
				if(artistBrowserManager!=null)
					artistBrowserManager=null;
				if(albumBrowserManager!=null)
					albumBrowserManager=null;
				((MainActivity)mActivity).unregisterOnBackListener(UIManager.this);
				
			}
			if(arg0==0 && mViewPagerSub.getCurrentItem()==0)
			{
				mViewPagerSub.removeAllViews();
				mViewPagerSub.setVisibility(View.INVISIBLE);
				if(myMusicManagersub!=null)
				{
					myMusicManagersub.unregisterReceiver();
					myMusicManagersub=null;
				}
			}
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0)
		{
			// TODO Auto-generated method stub
		}
		
	}
	
	@Override
	public void onBack()
	{
		// TODO Auto-generated method stub
		if(mViewPagerSub.isShown())
		{
			mViewPagerSub.setCurrentItem(0, true);
		}
		else if(mViewPager.isShown())
		{
			mViewPager.setCurrentItem(0, true);
		}
	}
	
	public void initBg(View view)
	{
		SPStorage spStorage=new SPStorage(mActivity);
		view.setBackgroundDrawable(new BitmapDrawable(spStorage.getBgBitmap()));
	}

	private class ForBackGroundChangeReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			// TODO Auto-generated method stub
			if(arg1.getAction().equals(IConstants.BROADCAST_CHANGEBG))
			{
				if(myMusicManager!=null)
					initBg(myMusicManager.contentView);
				if(myMusicManagersub!=null)
					initBg(myMusicManagersub.contentView);
				if(folderBrowserManager!=null)
					initBg(folderBrowserManager.contentView);
				if(artistBrowserManager!=null)
					initBg(artistBrowserManager.contentView);
				if(albumBrowserManager!=null)
					initBg(albumBrowserManager.contentView);
			}
		}
		
	} 

	public void unregisterReceiver()
	{
		mActivity.unregisterReceiver(receiver);
	}
}
