package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.model.FolderInfo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class FolderInfoDao
{
	private static final String TABLE_FOLDER="folder_info";
	private Context mContext;
	public FolderInfoDao(Context context)
	{
		this.mContext=context;
	}
	
	public void saveFolderInfo(List<FolderInfo> list)
	{
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		for(int i=0;i<list.size();i++)
		{
			FolderInfo info=list.get(i);
			ContentValues cv=new ContentValues();
			cv.put("folder_name", info.folder_name);
			cv.put("folder_path", info.folder_path);
			db.insert(TABLE_FOLDER, null, cv);
		}
	}
	public List<FolderInfo> getFolderInfo()
	{
		List<FolderInfo> list=new ArrayList<>();
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select * from "+TABLE_FOLDER;
		Cursor cursor=db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			FolderInfo info=new FolderInfo();
			info.folder_name=cursor.getString(cursor.getColumnIndex("folder_name"));
			info.folder_path=cursor.getString(cursor.getColumnIndex("folder_path"));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	
	public boolean hasData()
	{
		boolean has=false;
		SQLiteDatabase db=DatabaseHelper.getInstance(mContext);
		String sql="select count(*) from "+TABLE_FOLDER;
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
		String sql="select count(*) from "+TABLE_FOLDER;
		Cursor cursor=db.rawQuery(sql, null);
		if(cursor.moveToFirst())
		{
			count=cursor.getInt(0);
		}
		cursor.close();
		return count;
	}
}
