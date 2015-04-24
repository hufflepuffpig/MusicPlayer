package com.example.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class AlbumInfo implements Parcelable
{

	public static final String KEY_ALBUM_NAME = "album_name";
	public static final String KEY_ALBUM_ID = "album_id";
	public static final String KEY_NUMBER_OF_SONGS = "number_of_songs";
	public static final String KEY_ALBUM_ART = "album_art";
	
	
	public String album_name;
	public int album_id=-1;
	public int number_of_songs=0;
	public String album_art;
	
	
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
		bundle.putString(KEY_ALBUM_NAME, album_name);
		bundle.putInt(KEY_ALBUM_ID, album_id);
		bundle.putInt(KEY_NUMBER_OF_SONGS, number_of_songs);
		bundle.putString(KEY_ALBUM_ART, album_art);
		arg0.writeBundle(bundle);
	}

	public static final Parcelable.Creator<AlbumInfo> CREATOR=new Parcelable.Creator<AlbumInfo>()
	{

		@Override
		public AlbumInfo createFromParcel(Parcel arg0)
		{
			// TODO Auto-generated method stub
			Bundle bundle=arg0.readBundle();
			AlbumInfo info=new AlbumInfo();
			info.album_name=bundle.getString(KEY_ALBUM_NAME);
			info.album_id=bundle.getInt(KEY_ALBUM_ID);
			info.number_of_songs=bundle.getInt(KEY_NUMBER_OF_SONGS);
			info.album_art=bundle.getString(KEY_ALBUM_ART);
			return info;
		}

		@Override
		public AlbumInfo[] newArray(int arg0)
		{
			// TODO Auto-generated method stub
			return new AlbumInfo[arg0];
		}
	};
}
