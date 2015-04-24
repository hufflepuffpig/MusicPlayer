package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.db.FavoriteInfoDao;
import com.example.db.MusicInfoDao;
import com.example.model.MusicInfo;
import com.example.music.MusicAPP;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.R;
import com.example.utils.MusicUtils;

import android.app.Activity;
import android.os.RemoteException;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
/*
 * 这个MyAdapter是为MyMusicManager维护的ListView服务的
 */
public class MyAdapter extends BaseAdapter
{

	private Activity mActivity;
	private List<MusicInfo> list;
	private MusicInfoDao mMusicInfoDao;
	private FavoriteInfoDao mFavoriteInfoDao;
	
	public boolean isForFavoriteBrowser=false;
	private int mCurMusicIndex=-1,mPlayState;
	
	public MyAdapter(Activity activity)
	{
		this.mActivity=activity;
		this.list=new ArrayList<>();
		
		mMusicInfoDao=new MusicInfoDao(mActivity);
		mFavoriteInfoDao=new FavoriteInfoDao(mActivity);
	}
	@Override
	public int getCount()
	{
		// TODO Auto-generated method stub
		return list.size();
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
	public View getView(int arg0, View arg1, ViewGroup arg2)
	{
		// TODO Auto-generated method stub
		Holder holder=new Holder();
		if(arg1==null)
		{
			arg1=mActivity.getLayoutInflater().inflate(R.layout.musiclist_item, null);
			holder.favoriteIv=(ImageView) arg1.findViewById(R.id.favorite_iv);
			holder.playStateIconIv=(ImageView) arg1.findViewById(R.id.playstate_iv);
			holder.musicnameTv=(TextView) arg1.findViewById(R.id.musicname_tv);
			holder.artistTv=(TextView) arg1.findViewById(R.id.artist_tv);
			holder.durationTv=(TextView) arg1.findViewById(R.id.duration_tv);
			arg1.setTag(holder);
		}else {
			holder=(Holder) arg1.getTag();
		}
		//处理playStateIconIv
		if(mCurMusicIndex==arg0)
		{
			if(mPlayState==IConstants.MPS_PLAYING)
				holder.playStateIconIv.setBackgroundResource(R.drawable.list_play_state);
			else if(mPlayState==IConstants.MPS_PAUSE)
				holder.playStateIconIv.setBackgroundResource(R.drawable.list_pause_state);
			holder.playStateIconIv.setVisibility(View.VISIBLE);
		}else {
			holder.playStateIconIv.setVisibility(View.GONE);
		}
		
		//处理favoriteIv
		final MusicInfo info=list.get(arg0);
		holder.favoriteIv.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View view)
			{
				if(info.favorite==0)
				{
					mMusicInfoDao.setFavoriteById(info._id, 1);
					mFavoriteInfoDao.saveMusicInfo(info);
					info.setFavorite(1);
					notifyDataSetChanged();
				}else {
					mMusicInfoDao.setFavoriteById(info._id, 0);
					mFavoriteInfoDao.deleteById(info._id);
					info.setFavorite(0);
					if(isForFavoriteBrowser)
					{
						list.remove(info);
					}
					notifyDataSetChanged();
				}
			}
		});
		if(info.favorite==1)
			holder.favoriteIv.setImageResource(R.drawable.icon_favourite_checked);
		else 
			holder.favoriteIv.setImageResource(R.drawable.icon_favourite_normal);
		
		
		holder.musicnameTv.setText(list.get(arg0).musicname);
		holder.artistTv.setText(list.get(arg0).artist);
		holder.durationTv.setText(MusicUtils.makeTimeString(list.get(arg0).duration));
		return arg1;
	}
	
	private class Holder
	{
		ImageView playStateIconIv,favoriteIv;
		TextView musicnameTv,artistTv,durationTv;
	}

	public void setData(List<MusicInfo> list)
	{
		this.list=list;
		notifyDataSetChanged();
	}
	public void refreshPlayingList() throws RemoteException
	{
		if(MusicAPP.serviceManager.server!=null)
		{
			MusicAPP.serviceManager.refreshMusicList(list);
		}
	}

	public List<MusicInfo> getData()
	{
		return list;
	}

	public void setCurMusicIndex(int index)
	{
		mCurMusicIndex=index;
	}
	public void setPlayState(int state)
	{
		mPlayState=state;
	}


}
