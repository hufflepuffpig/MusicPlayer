package com.example.uimanager;

import java.util.ArrayList;
import java.util.List;

import com.example.model.AlbumInfo;
import com.example.musicplayer.IConstants;
import com.example.musicplayer.R;
import com.example.storage.SPStorage;
import com.example.utils.MusicUtils;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AlbumBrowserManager
{
	private Activity mActivity;
	private LayoutInflater mInflater;
	private UIManager mUiManager;
	public View contentView;
	
	public AlbumBrowserManager(Activity activity,UIManager uiManager)
	{
		this.mActivity=activity;
		this.mInflater=activity.getLayoutInflater();
		this.mUiManager=uiManager;
	}
	public View getView()
	{
		contentView=mInflater.inflate(R.layout.albumbrowser, null);
		ListView listView=(ListView) contentView.findViewById(R.id.album_listview);
		final AlbumAdapter myAdapter=new AlbumAdapter();
		listView.setAdapter(myAdapter);
		myAdapter.setData(MusicUtils.queryAlbum(mActivity));
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				try
				{
					mUiManager.setContentSub(IConstants.ALBUM_TO_MYMUSIC, myAdapter.getItem(arg2));
				} catch (RemoteException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		return contentView;
	}
	
	private class AlbumAdapter extends BaseAdapter
	{
		private List<AlbumInfo> list=new ArrayList<>();
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
			return list.get(arg0);
		}

		@Override
		public long getItemId(int arg0)
		{
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2)
		{
			// TODO Auto-generated method stub
			Holder holder;
			if(arg1==null)
			{
				arg1=mInflater.inflate(R.layout.albumbrowser_listitem, null);
				holder=new Holder();
				holder.album_name_tv=(TextView) arg1.findViewById(R.id.album_name_tv);
				holder.number_of_songs_tv=(TextView) arg1.findViewById(R.id.number_of_songs_tv);
				arg1.setTag(holder);
			}else {
				holder=(Holder) arg1.getTag();
			}
			holder.album_name_tv.setText(list.get(arg0).album_name);
			holder.number_of_songs_tv.setText(list.get(arg0).number_of_songs+"首歌");
			return arg1;
		}
		class Holder
		{
			TextView album_name_tv,number_of_songs_tv;
		}
	
		public void setData(List<AlbumInfo> list)
		{
			this.list=list;
			notifyDataSetChanged();
		}
	}

}
