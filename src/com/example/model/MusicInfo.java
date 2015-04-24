package com.example.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class MusicInfo implements Parcelable
{
	public static final String KEY_ID = "_id";
	public static final String KEY_SONG_ID = "songid";
	public static final String KEY_ALBUM_ID = "albumid";
	public static final String KEY_DURATION = "duration";
	public static final String KEY_MUSIC_NAME = "musicname";
	public static final String KEY_ARTIST = "artist";
	public static final String KEY_DATA = "data";
	public static final String KEY_FOLDER = "folder";
	public static final String KEY_MUSIC_NAME_KEY = "musicnamekey";
	public static final String KEY_ARTIST_KEY = "artistkey";
	public static final String KEY_FAVORITE = "favorite";

	//TABLE_MUSIC中各个列属性
	public int _id=-1;
	public int songid=-1;
	public int albumid=-1;
	public int duration;
	public String musicname;
	public String artist;
	public String data;
	public String folder;
	public String musicnamekey;
	public String artistkey;
	public int favorite=0;
	
	public void setFavorite(int favorite)
	{
		this.favorite=favorite;
	}
	public int getFavorite()
	{
		return this.favorite;
	}
	
	@Override
	public int describeContents()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1)
	{
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		bundle.putInt(KEY_ID, _id);
		bundle.putInt(KEY_SONG_ID, songid);
		bundle.putInt(KEY_ALBUM_ID, albumid);
		bundle.putInt(KEY_DURATION, duration);
		bundle.putString(KEY_MUSIC_NAME, musicname);
		bundle.putString(KEY_ARTIST, artist);
		bundle.putString(KEY_DATA, data);
		bundle.putString(KEY_FOLDER, folder);
		bundle.putString(KEY_MUSIC_NAME_KEY, musicnamekey);
		bundle.putString(KEY_ARTIST_KEY, artistkey);
		bundle.putInt(KEY_FAVORITE, favorite);
		arg0.writeBundle(bundle);
	}
	
	public static final Parcelable.Creator<MusicInfo> CREATOR=new Parcelable.Creator<MusicInfo>()
	{

		@Override
		public MusicInfo createFromParcel(Parcel arg0)
		{
			// TODO Auto-generated method stub
			Bundle bundle=arg0.readBundle();
			MusicInfo musicInfo=new MusicInfo();
			musicInfo._id=bundle.getInt(KEY_ID);
			musicInfo.songid=bundle.getInt(KEY_SONG_ID);
			musicInfo.albumid=bundle.getInt(KEY_ALBUM_ID);
			musicInfo.duration=bundle.getInt(KEY_DURATION);
			musicInfo.musicname=bundle.getString(KEY_MUSIC_NAME);
			musicInfo.artist=bundle.getString(KEY_ARTIST);
			musicInfo.data=bundle.getString(KEY_DATA);
			musicInfo.folder=bundle.getString(KEY_FOLDER);
			musicInfo.musicnamekey=bundle.getString(KEY_MUSIC_NAME_KEY);
			musicInfo.artistkey=bundle.getString(KEY_ARTIST_KEY);
			musicInfo.favorite=bundle.getInt(KEY_FAVORITE);
			return musicInfo;
		}

		@Override
		public MusicInfo[] newArray(int arg0)
		{
			// TODO Auto-generated method stub
			return new MusicInfo[arg0];
		}
	};

}
