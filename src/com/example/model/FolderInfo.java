package com.example.model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

public class FolderInfo implements Parcelable
{
	public static final String KEY_FOLDER_NAME="folder_name";
	public static final String KEY_FOLDER_PATH="folder_path";
	
	public String folder_name;
	public String folder_path;
	
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
		bundle.putString(KEY_FOLDER_NAME, folder_name);
		bundle.putString(KEY_FOLDER_PATH, folder_path);
		arg0.writeBundle(bundle);
	}
	public static final Parcelable.Creator<FolderInfo> CREATOR=new Parcelable.Creator<FolderInfo>()
	{

		@Override
		public FolderInfo createFromParcel(Parcel arg0)
		{
			// TODO Auto-generated method stub
			FolderInfo info=new FolderInfo();
			Bundle bundle=arg0.readBundle();
			info.folder_name=bundle.getString(KEY_FOLDER_NAME);
			info.folder_path=bundle.getString(KEY_FOLDER_PATH);
			return info;
		}

		@Override
		public FolderInfo[] newArray(int arg0)
		{
			// TODO Auto-generated method stub
			return new FolderInfo[arg0];
		}
	};
}
