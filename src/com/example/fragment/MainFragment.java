package com.example.fragment;

import java.net.ServerSocket;

import com.example.db.AlbumInfoDao;
import com.example.db.ArtistInfoDao;
import com.example.db.FavoriteInfoDao;
import com.example.db.FolderInfoDao;
import com.example.db.MusicInfoDao;
import com.example.model.MusicInfo;
import com.example.music.MusicAPP;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.MainActivity;
import com.example.musicplayer.R;
import com.example.storage.SPStorage;
import com.example.uimanager.BottomManager;
import com.example.uimanager.UIManager;
import com.example.utils.MusicTimer;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;


public class MainFragment extends Fragment
{

	private GridView mGridView;
	private MyGridViewAdapter mAdapter;
	
	private UIManager mUiManager;
	private BottomManager bottomManager;
	private MusicTimer mMusicTimer;
	private ForMainFragmentRecevier recevier;
	private ForBackGroundChangeReceiver receiver2;
	
	private MusicInfoDao mMusicInfoDao;
	private FavoriteInfoDao mFavoriteInfoDao;
	private FolderInfoDao mFolderInfoDao;
	private ArtistInfoDao mArtistInfoDao;
	private AlbumInfoDao mAlbumInfoDao;
	private View view;
	
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

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mMusicInfoDao=new MusicInfoDao(getActivity());
		mFavoriteInfoDao=new FavoriteInfoDao(getActivity());
		mFolderInfoDao=new FolderInfoDao(getActivity());
		mArtistInfoDao=new ArtistInfoDao(getActivity());
		mAlbumInfoDao=new AlbumInfoDao(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		view=inflater.inflate(R.layout.frame_main1, container, false);
		initMainFragmentBg(view);
		
		mGridView=(GridView) view.findViewById(R.id.gridview);
		mAdapter=new MyGridViewAdapter();
		mGridView.setAdapter(mAdapter);
		
		mUiManager=new UIManager(getActivity(), view);
		bottomManager=new BottomManager(getActivity(), view);
		mMusicTimer=new MusicTimer(mHandler);
		
		
		recevier=new ForMainFragmentRecevier();
		IntentFilter filter=new IntentFilter(IConstants.BROADCAST_NAME);
		getActivity().registerReceiver(recevier, filter);
		receiver2=new ForBackGroundChangeReceiver();
		IntentFilter filter2=new IntentFilter(IConstants.BROADCAST_CHANGEBG);
		getActivity().registerReceiver(receiver2, filter2);
		
		Intent intent=new Intent(IConstants.BROADCAST_NAME);
		getActivity().sendBroadcast(intent);
		
		return view;
	}
	
	private class MyGridViewAdapter extends BaseAdapter
	{

		private int[] drawable={R.drawable.icon_local_music,R.drawable.icon_favorites,
				R.drawable.icon_folder_plus,R.drawable.icon_artist_plus,R.drawable.icon_album_plus};
		private String[] name={"我的音乐","我的最爱","文件夹","歌手","专辑"};
		private int musicNum=0,artistNum=0,albumNum=0,floderNum=0,favoriteNum=0; 
		
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return 5;
		}

		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0)
		{
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2)
		{
			// TODO Auto-generated method stub
			Holder holder;
			if(arg1==null)
			{
				holder=new Holder();
				arg1=getActivity().getLayoutInflater().inflate(R.layout.main_gridview_item, null);
				holder.iv=(ImageView) arg1.findViewById(R.id.gridview_item_iv);
				holder.nameTv=(TextView) arg1.findViewById(R.id.gridview_item_name);
				holder.numTv=(TextView) arg1.findViewById(R.id.gridview_item_num);
				arg1.setTag(holder);
			}else {
				holder=(Holder) arg1.getTag();
			}
			arg1.setOnClickListener(new View.OnClickListener()
			{
				
				@Override
				public void onClick(View view)
				{
					// TODO Auto-generated method stub
					int from=-1;
					switch (arg0)
					{
					case 0:
						from=IConstants.START_FROM_LOCAL;break;
					case 1:
						from=IConstants.START_FROM_FAVORITE;break;
					case 2:
						from=IConstants.START_FROM_FOLDER;break;
					case 3:
						from=IConstants.START_FROM_ARTIST;break;
					case 4:
						from=IConstants.START_FROM_ALBUM;break;
					default:
						break;
					}
					try
					{
						mUiManager.setContent(from);
					} catch (RemoteException e)
					{
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			switch (arg0)
			{
			case 0:
				holder.numTv.setText(""+musicNum);
				break;
			case 1:
				holder.numTv.setText(""+favoriteNum);
				break;
			case 2:
				holder.numTv.setText(""+floderNum);
				break;
			case 3:
				holder.numTv.setText(""+artistNum);
				break;
			case 4:
				holder.numTv.setText(""+albumNum);
				break;

			default:
				break;
			}
			holder.iv.setImageResource(drawable[arg0]);
			holder.nameTv.setText(name[arg0]);
			return arg1;
		}
		
		public void setNum()
		{
			musicNum=mMusicInfoDao.getDataCount();
			artistNum=mArtistInfoDao.getDataCount();
			albumNum=mAlbumInfoDao.getDataCount();
			floderNum=mFolderInfoDao.getDataCount();
			favoriteNum=mFavoriteInfoDao.getDataCount();
			notifyDataSetChanged();
		}
		
 		class Holder
		{
			ImageView iv;
			TextView nameTv,numTv;
		}
		
	}

	private class ForMainFragmentRecevier extends BroadcastReceiver
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
						mAdapter.setNum();
						if(MusicAPP.serviceManager.server!=null)
						{	
							bottomManager.refreshUI();
							if(MusicAPP.serviceManager.getCurMusic()!=null)
								bottomManager.refreshTV(MusicAPP.serviceManager.getCurMusic().musicname, MusicAPP.serviceManager.getCurMusic().artist,
										MusicAPP.serviceManager.getCurMusic().albumid);
							if (MusicAPP.serviceManager.getPlayState() == IConstants.MPS_PLAYING)
							{
								mMusicTimer.startTimer();
							} else if (MusicAPP.serviceManager.getPlayState() == IConstants.MPS_PAUSE)
							{
								mMusicTimer.stopTimer();
							}
						}
					}
					else//响应按音乐时有信息传来的情况
					{
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

	private class ForBackGroundChangeReceiver extends BroadcastReceiver
	{

		@Override
		public void onReceive(Context arg0, Intent arg1)
		{
			// TODO Auto-generated method stub
			if(arg1.getAction().equals(IConstants.BROADCAST_CHANGEBG))
			{
				initMainFragmentBg(view);
			}
		}
		
	}
	
	@Override
	public void onDestroy()
	{
		// TODO Auto-generated method stub
		super.onDestroy();
		getActivity().unregisterReceiver(recevier);
		getActivity().unregisterReceiver(receiver2);
		mUiManager.unregisterReceiver();
	}

	public void initMainFragmentBg(View view)
	{
		SPStorage spStorage=new SPStorage(getActivity());
		view.setBackgroundDrawable(new BitmapDrawable(spStorage.getBgBitmap()));
	}

	public void refreshNum()
	{
		mAdapter.setNum();
	}
}
