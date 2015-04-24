package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.model.ArtistInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ArtistInfoDao
{
	private static final String TABLE_ARTIST="artist_info";
	private Context mContext;
	public ArtistInfoDao(Context context)
	{
		this.mContext=context;
	}
	
	public void saveArtistInfo(List<ArtistInfo> list)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		for(int i=0;i<list.size();i++)
		{
			ArtistInfo info=list.get(i);
			ContentValues cv=new ContentValues();
			cv.put("artist_name", info.artist_name);
			cv.put("number_of_tracks", info.number_of_tracks);
			db.insert(TABLE_ARTIST, null, cv);
		}
	}
	public List<ArtistInfo> getArtistInfo()
	{
		List<ArtistInfo> list=new ArrayList<>();
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select * from "+TABLE_ARTIST;
		Cursor cursor=db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			ArtistInfo info=new ArtistInfo();
			info.artist_name=cursor.getString(cursor.getColumnIndex("artist_name"));
			info.number_of_tracks=cursor.getInt(cursor.getColumnIndex("number_of_tracks"));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	
	public boolean hasData()
	{
		boolean has=false;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_ARTIST;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			if(cursor.getInt(0)>0)
				has=true;
		}
		cursor.close();
		return has;
	}
	public int getDataCount()
	{
		int count=0;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_ARTIST;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			count=cursor.getInt(0);
		}
		return count;
	}
}
