package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.model.AlbumInfo;
import com.example.model.ArtistInfo;
import com.example.model.FolderInfo;
import com.example.model.MusicInfo;
import com.example.musicplayer.IConstants;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MusicInfoDao
{
	private static final String TABLE_MUSIC="music_info";
	private Context mContext;
	
	public MusicInfoDao(Context context)
	{
		this.mContext=context;
	}
	
	
	//把MusicInfo列表写进数据库
	public void saveMusicInfo(List<MusicInfo> list)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		for(int i=0;i<list.size();i++)
		{
			MusicInfo musicInfo=list.get(i);
			ContentValues values=new ContentValues();
			values.put(MusicInfo.KEY_SONG_ID, musicInfo.songid);
			values.put(MusicInfo.KEY_ALBUM_ID, musicInfo.albumid);
			values.put(MusicInfo.KEY_DURATION, musicInfo.duration);
			values.put(MusicInfo.KEY_MUSIC_NAME, musicInfo.musicname);
			values.put(MusicInfo.KEY_ARTIST, musicInfo.artist);
			values.put(MusicInfo.KEY_DATA, musicInfo.data);
			values.put(MusicInfo.KEY_FOLDER, musicInfo.folder);
			values.put(MusicInfo.KEY_MUSIC_NAME_KEY, musicInfo.musicnamekey);
			values.put(MusicInfo.KEY_ARTIST_KEY, musicInfo.artistkey);
			values.put(MusicInfo.KEY_FAVORITE, musicInfo.favorite);
			db.insert(TABLE_MUSIC, null, values);
		}
	}
	public void setFavoriteById(int id,int favorite)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="update "+TABLE_MUSIC+" set favorite="+favorite+" where _id="+id;
		db.execSQL(sql);
	}
	
	//获取数据库中的MusicInfo，并组装成列表返回
	public List<MusicInfo> getMusicInfos()
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select * from "+TABLE_MUSIC;
		return parseCursor(db.rawQuery(sql, null));
	}
	public List<MusicInfo> parseCursor(Cursor cursor)
	{
		List<MusicInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			MusicInfo info=new MusicInfo();
			info._id=cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_ID));
			info.songid=cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_SONG_ID));
			info.albumid=cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_ALBUM_ID));
			info.duration=cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_DURATION));
			info.musicname=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_MUSIC_NAME));
			info.artist=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_ARTIST));
			info.data=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_DATA));
			info.folder=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_FOLDER));
			info.musicnamekey=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_MUSIC_NAME_KEY));
			info.artistkey=cursor.getString(cursor.getColumnIndex(MusicInfo.KEY_ARTIST_KEY));
			info.favorite=cursor.getInt(cursor.getColumnIndex(MusicInfo.KEY_FAVORITE));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	//有条件地查询Music列表，获取适合的Music信息，例如获取指定文件夹的，指定歌手的，指定专辑的
	public List<MusicInfo> getMusicInfoByType(int type,Object obj)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		if(type==IConstants.START_FROM_FOLDER)
		{
			String sql="select * from "+TABLE_MUSIC+" where folder = ?";
			Cursor cursor=db.rawQuery(sql, new String[]{((FolderInfo)obj).folder_path});
			return parseCursor(cursor);
		}
		else if(type==IConstants.START_FROM_ARTIST)
		{
			String sql="select * from "+TABLE_MUSIC+" where artist = ?";
			Cursor cursor=db.rawQuery(sql, new String[]{((ArtistInfo)obj).artist_name});
			return parseCursor(cursor);
		}
		else if(type==IConstants.START_FROM_ALBUM)
		{
			String sql="select * from "+TABLE_MUSIC+" where albumid = ?";
			Cursor cursor=db.rawQuery(sql, new String[]{((AlbumInfo)obj).album_id+""});
			return parseCursor(cursor);
		}
		return null;
		
	}
	//查看TABLE_MUSIC表有多少行，表示有没有音乐存在数据库
 	public boolean hasData()
	{
		boolean has=false;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_MUSIC;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			int num=cursor.getInt(0);
			if(num>0)
			{
				has=true;
			}
		}
		cursor.close();
		return has;
	}
 	//获取TABLE_MUSIC表有多少行
 	public int getDataCount()
 	{
 		int ret=0;
 		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
 		String sql="select count(*) from "+TABLE_MUSIC;
 		Cursor cursor=db.rawQuery(sql, null);
 		if(cursor.moveToFirst())
 		{
 			ret=cursor.getInt(0);
 		}
 		return ret;
 	}

}
