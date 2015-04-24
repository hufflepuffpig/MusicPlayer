package com.example.storage;

import java.io.IOException;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SPStorage
{
	private Context mContext;
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editor;
	public SPStorage(Context mContext)
	{
		this.mContext=mContext;
		sharedPreferences=mContext.getSharedPreferences("data", Context.MODE_PRIVATE);
		editor=sharedPreferences.edit();
	}
	
	public void setBgPath(String path)
	{
		editor.putString("cur_bg_path", path);
		editor.commit();
	}
	
	public String getBgPath()
	{
		return sharedPreferences.getString("cur_bg_path", null);
	}
	
	public Bitmap getBgBitmap()
	{
		String path=getBgPath();
		if(path==null)
			return null;
		else {
			AssetManager assetManager=mContext.getAssets();
			try
			{
				Bitmap bmp=BitmapFactory.decodeStream(assetManager.open(path));
				return bmp;
			} catch (IOException e)
			{
				e.printStackTrace();
				return null;
			}
		}
	}

}
