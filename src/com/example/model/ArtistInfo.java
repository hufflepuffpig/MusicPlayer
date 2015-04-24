package com.example.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class ArtistInfo implements Parcelable
{
	public static final String KEY_ARTIST_NAME = "artist_name";
	public static final String KEY_NUMBER_OF_TRACKS = "number_of_tracks";
	
	public String artist_name;
	public int number_of_tracks;
	
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
		bundle.putString(KEY_ARTIST_NAME, artist_name);
		bundle.putInt(KEY_NUMBER_OF_TRACKS, number_of_tracks);
		arg0.writeBundle(bundle);
	}
	public static final Parcelable.Creator<ArtistInfo> CREATOR=new Parcelable.Creator<ArtistInfo>()
	{

		@Override
		public ArtistInfo createFromParcel(Parcel arg0)
		{
			// TODO Auto-generated method stub
			ArtistInfo info=new ArtistInfo();
			Bundle bundle=arg0.readBundle();
			info.artist_name=bundle.getString(KEY_ARTIST_NAME);
			info.number_of_tracks=bundle.getInt(KEY_NUMBER_OF_TRACKS);
			return info;
		}

		@Override
		public ArtistInfo[] newArray(int arg0)
		{
			// TODO Auto-generated method stub
			return new ArtistInfo[arg0];
		}
	};
	
}
