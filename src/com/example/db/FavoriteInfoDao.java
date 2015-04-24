package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.model.MusicInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteInfoDao
{
	private static final String TABLE_FAVORITE="favorite_info";
	private Context mContext;
	public FavoriteInfoDao(Context context)
	{
		this.mContext=context;
	}
	
	public void saveMusicInfo(MusicInfo music)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		ContentValues cv=new ContentValues();
		cv.put("_id",music._id);
		cv.put("songid", music.songid);
		cv.put("albumid", music.albumid);
		cv.put("duration", music.duration);
		cv.put("musicname", music.musicname);
		cv.put("artist", music.artist);
		cv.put("data", music.data);
		cv.put("folder", music.folder);
		cv.put("musicnamekey", music.musicnamekey);
		cv.put("artistkey", music.artistkey);
		cv.put("favorite", 1);
		db.insert(TABLE_FAVORITE, null, cv);
	}
	public void deleteById(int _id)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		db.delete(TABLE_FAVORITE, "_id=?", new String[]{_id+""});
	}
	
	
	public List<MusicInfo> getMusicInfo()
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select * from "+TABLE_FAVORITE;
		Cursor cursor=db.rawQuery(sql, null);
		return parseCursor(cursor);
	}
	public List<MusicInfo> parseCursor(Cursor cursor)
	{
		List<MusicInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			MusicInfo music=new MusicInfo();
			music._id = cursor.getInt(cursor.getColumnIndex("_id"));
			music.songid = cursor.getInt(cursor.getColumnIndex("songid"));
			music.albumid = cursor.getInt(cursor.getColumnIndex("albumid"));
			music.duration = cursor.getInt(cursor.getColumnIndex("duration"));
			music.musicname = cursor.getString(cursor
					.getColumnIndex("musicname"));
			music.artist = cursor.getString(cursor.getColumnIndex("artist"));
			music.data = cursor.getString(cursor.getColumnIndex("data"));
			music.folder = cursor.getString(cursor.getColumnIndex("folder"));
			music.musicnamekey = cursor.getString(cursor
					.getColumnIndex("musicnamekey"));
			music.artistkey = cursor.getString(cursor
					.getColumnIndex("artistkey"));
			music.favorite = cursor.getInt(cursor.getColumnIndex("favorite"));
			list.add(music);
		}
		cursor.close();
		return list;
	}
	
	public boolean hasData()
	{
		boolean has=false;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_FAVORITE;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			int count=cursor.getInt(0);
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
		String sql="select count(*) from "+TABLE_FAVORITE;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			count=cursor.getInt(0);
		}
		cursor.close();
		return count;
	}
}
