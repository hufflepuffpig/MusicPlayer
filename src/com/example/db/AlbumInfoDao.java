package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.model.AlbumInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AlbumInfoDao
{
	private static final String TABLE_ALBUM="album_info";
	private Context mContext;
	
	public AlbumInfoDao(Context context)
	{
		this.mContext=context;
	}
	
	public void saveAlbumInfo(List<AlbumInfo> list)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		for(int i=0;i<list.size();i++)
		{
			AlbumInfo info=list.get(i);
			ContentValues values=new ContentValues();
			values.put(AlbumInfo.KEY_ALBUM_NAME, info.album_name);
			values.put(AlbumInfo.KEY_ALBUM_ID,info.album_id);
			values.put(AlbumInfo.KEY_NUMBER_OF_SONGS, info.number_of_songs);
			values.put(AlbumInfo.KEY_ALBUM_ART, info.album_art);
			db.insert(TABLE_ALBUM, null, values);
		}
	}

	public List<AlbumInfo> getAlbumInfo()
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select * from "+TABLE_ALBUM;
		Cursor cursor=db.rawQuery(sql, null);
		List<AlbumInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			AlbumInfo info=new AlbumInfo();
			info.album_name=cursor.getString(cursor.getColumnIndex("album_name"));
			info.album_id=cursor.getInt(cursor.getColumnIndex("album_id"));
			info.number_of_songs=cursor.getInt(cursor.getColumnIndex("number_of_songs"));
			info.album_art=cursor.getString(cursor.getColumnIndex("album_art"));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	public boolean hasData()
	{
		boolean has=false;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_ALBUM;
		Cursor cursor=db.rawQuery(sql, null);
		int count=0;
		if(cursor.moveToFirst())
		{
			count=cursor.getInt(0);
			if(count>0)
				has=true;
		}
		cursor.close();
		return has;
	}

	public int getDataCount()
	{
		int count=0;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_ALBUM;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			count=cursor.getInt(0);
		}
		return count;
	}
}
