package com.example.musicplayer;

import java.util.ArrayList;
import java.util.List;

import com.example.db.DatabaseHelper;
import com.example.db.MusicInfoDao;
import com.example.fragment.MainFragment;
import com.example.interfaces.OnBackListener;
import com.example.model.AlbumInfo;
import com.example.model.ArtistInfo;
import com.example.model.FolderInfo;
import com.example.model.MusicInfo;
import com.example.music.MusicAPP;
import com.example.slidingmenu.SlidingMenu;
import com.example.utils.MusicUtils;
import com.example.utils.SplashScreen;

import android.R.integer;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class MainActivity extends Activity
{

	private MainFragment mMainFragment;
	private SplashScreen splashScreen;
	private Handler mhandler;
	private List<OnBackListener> backListeners=new ArrayList<>();
	
	private MusicInfoDao musicInfoDao;
	//下面变量都是关于SlidingMenu的
	public SlidingMenu slidingMenu;
	private final static String[] playmode_name={"列表循环","顺序播放","随机播放","单曲循环"};
	private int mCurMode=0;
	private final static int[] playmode_resID={R.drawable.icon_list_reapeat,R.drawable.icon_sequence,
		R.drawable.icon_shuffle,R.drawable.icon_single_repeat};
	private ProgressDialog progressDialog=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//开启引导页面
		setContentView(R.layout.frame_main);
		splashScreen=new SplashScreen(this);
		splashScreen.show(R.drawable.image_splash_background, SplashScreen.FADE_OUT);
		//使用MainFragment代替主界面
		mMainFragment=new MainFragment();
		getFragmentManager().beginTransaction().replace(R.id.frame_main, mMainFragment).commit();
		//生成侧滑菜单
		slidingMenu=new SlidingMenu(this);
		slidingMenu.setMode(SlidingMenu.RIGHT);
		slidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		slidingMenu.setBehindOffset(150);
		slidingMenu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		slidingMenu.setMenu(R.layout.slidingmenumain1);
		initSlidingMenu();
		
		//注册Handler
		mhandler=new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				// TODO Auto-generated method stub
				if(msg.what==1)
				{
					splashScreen.remove();
				}else if(msg.what==2)
				{
					mMainFragment.refreshNum();
					progressDialog.dismiss();
					progressDialog=null;
				}
			}
			
		};
		
		musicInfoDao=new MusicInfoDao(MainActivity.this);
		getData();
	}

	public void getData()
	{
		//hasData()会getInstance()来获取数据库，Helper会在getWritableDatabase的时候运行onCreate，所以数据库4个表在这个事就创建了
		if(musicInfoDao.hasData())
		{
			mhandler.sendEmptyMessageDelayed(1, 2000);
		}else {
			MusicUtils.queryMusic(MainActivity.this);
			MusicUtils.queryFavorite(MainActivity.this);
			MusicUtils.queryFolder(MainActivity.this);
			MusicUtils.queryArtist(MainActivity.this);
			MusicUtils.queryAlbum(MainActivity.this);
			mhandler.sendEmptyMessage(1);
		}
		
			
	}

	public void registerOnBackListener(OnBackListener backListener)
	{
		if(!backListeners.contains(backListener))
			backListeners.add(backListener);
	}
	public void unregisterOnBackListener(OnBackListener backListener)
	{
		if(backListeners.contains(backListener))
			backListeners.remove(backListener);
	}
	
	@Override
	public void onBackPressed()
	{
		// TODO Auto-generated method stub
		if(slidingMenu.isMenuShowing())
		{
			slidingMenu.showContent();
			return;
		}
		if(backListeners.size()==0)
		{
			moveTaskToBack(true);
		}
		else {
			for(int i=0;i<backListeners.size();i++)
			{
				backListeners.get(i).onBack();
			}
		}
	}
	
	@Override
	protected void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		try
		{
			MusicAPP.serviceManager.exit();
			MusicAPP.serviceManager=null;
			MusicUtils.clearCache();
			System.exit(0);
		} catch (RemoteException e){e.printStackTrace();}
	}

	public void initSlidingMenu()
	{
		TextView scan_tv=(TextView) findViewById(R.id.menu_scan);
		scan_tv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// TODO Auto-generated method stub
				progressDialog=new ProgressDialog(MainActivity.this);
				progressDialog.setTitle("正在扫描中，请勿退出软件");
				progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				new ScanThread().start();
			}
		});
		
		final TextView playMode_tv=(TextView) findViewById(R.id.menu_playMode);
		playMode_tv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				if(MusicAPP.serviceManager.server!=null)
				{
					mCurMode=(mCurMode+1)%4;
					try
					{
						MusicAPP.serviceManager.setPlayMode(mCurMode);
						playMode_tv.setText(playmode_name[mCurMode]);
						Drawable drawable=getResources().getDrawable(playmode_resID[mCurMode]);
						drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
						playMode_tv.setCompoundDrawables(drawable, null, null, null);
					} catch (RemoteException e){e.printStackTrace();}	
				}
			}
		});
		
		TextView bg_tv=(TextView) findViewById(R.id.menu_changeBg);
		bg_tv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				Intent intent=new Intent(MainActivity.this, MenuActivity.class);
				startActivity(intent);
			}
		});
		
		TextView exit_tv=(TextView) findViewById(R.id.menu_exit);
		exit_tv.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View arg0)
			{
				MainActivity.this.finish();		
			}
		});
	}

	private class ScanThread extends Thread
	{

		@Override
		public void run()
		{
			// TODO Auto-generated method stub
			DatabaseHelper.getHelper(MainActivity.this).clearTable();
			MusicUtils.queryMusic(MainActivity.this);
			MusicUtils.queryFavorite(MainActivity.this);
			MusicUtils.queryFolder(MainActivity.this);
			MusicUtils.queryArtist(MainActivity.this);
			MusicUtils.queryAlbum(MainActivity.this);
			MainActivity.this.mhandler.sendEmptyMessage(2);
		}
		
	}
}
