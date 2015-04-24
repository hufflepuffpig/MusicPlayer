package com.example.fragment;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.example.musicplayer.IConstants;
import com.example.musicplayer.R;
import com.example.storage.SPStorage;
import com.example.storage.BmpAndPath;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MenuBackgroundFragment extends Fragment
{

	private GridView mGridView;
	private BkgGridAdapter mAdapter;
	private List<BmpAndPath> list;
	private SPStorage spStorage;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.menubackgroundfragment_main, container, false);
		getBmpAndPaths();

		mGridView=(GridView) view.findViewById(R.id.bkg_fragment_gridView);
		mAdapter=new BkgGridAdapter();
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				if(arg2>=9)
					spStorage.setBgPath("bkgs/0"+(arg2+1)+".jpg");
				else {
					spStorage.setBgPath("bkgs/00"+(arg2+1)+".jpg");
				}
				Intent intent=new Intent(IConstants.BROADCAST_CHANGEBG);
				getActivity().sendBroadcast(intent);
				getActivity().finish();
			}
		});
		mAdapter.setData(list);
		
		
		spStorage=new SPStorage(getActivity());
		return view;
	}

	private class BkgGridAdapter extends BaseAdapter
	{
		List<BmpAndPath> nodes=new ArrayList<>();
		@Override
		public int getCount()
		{
			// TODO Auto-generated method stub
			return nodes.size();
		}

		@Override
		public Object getItem(int arg0)
		{
			// TODO Auto-generated method stub
			return nodes.get(arg0);
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
				arg1=LayoutInflater.from(getActivity()).inflate(R.layout.background_gridview_item, null);
				holder=new Holder();
				holder.item_iv=(ImageView) arg1.findViewById(R.id.gridview_item_iv);
				holder.checked_iv=(ImageView) arg1.findViewById(R.id.gridview_item_checked_iv);
				arg1.setTag(holder);
			}else {
				holder=(Holder) arg1.getTag();
			}
			holder.item_iv.setImageBitmap(nodes.get(arg0).bmp);
			if(spStorage.getBgPath().equals(nodes.get(arg0).Path))
			{
				holder.checked_iv.setVisibility(View.VISIBLE);
			}else {
				holder.checked_iv.setVisibility(View.INVISIBLE);
			}
			return arg1;
		}
		
		private class Holder
		{
			ImageView item_iv,checked_iv;
		}
		
		public void setData(List<BmpAndPath> nodes)
		{
			this.nodes=nodes;
			notifyDataSetChanged();
		}
	}

	private void getBmpAndPaths()
	{
		AssetManager assetManager=getActivity().getAssets();
		try
		{
			list=new ArrayList<>();
			String[] names=assetManager.list("bkgs");
			for(int i=0;i<names.length;i++)
			{
				BmpAndPath node=new BmpAndPath();
				node.Path="bkgs/"+names[i];
				InputStream is=assetManager.open(node.Path);
				node.bmp=BitmapFactory.decodeStream(is);
				list.add(node);
				is.close();
			}
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
