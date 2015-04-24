package com.example.db;

import com.hp.hpl.sparta.xpath.ThisNodeTest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

	private static SQLiteDatabase mdb;
	private static DatabaseHelper mHelper;
	private static final int DB_VERSION = 3;
	private static final String DB_NAME="musicstore_new";
	private static final String TABLE_ALBUM = "album_info";
	private static final String TABLE_ARTIST = "artist_info";
	private static final String TABLE_MUSIC = "music_info";
	private static final String TABLE_FOLDER = "folder_info";
	private static final String TABLE_FAVORITE = "favorite_info";
	
	
	//获取Helper中的数据库
	public static SQLiteDatabase getInstance(Context context)
	{
		if(mdb==null)
		{
			mdb=getHelper(context).getWritableDatabase();
		}
		return mdb;
	}
	//获取Helper本身
	public static DatabaseHelper getHelper(Context context)
	{
		if(mHelper==null)
		{
			mHelper=new DatabaseHelper(context);
		}
		return mHelper;
	}
	//包装一下的构造函数
	public DatabaseHelper(Context context)
	{
		super(context, DB_NAME, null, DB_VERSION);
	}
	//第一次运行getWritableDatabase()时获取不到数据库文件，就会创建一个在data/data/<package name>/文件夹中，并回调该函数
	@Override
	public void onCreate(SQLiteDatabase arg0)
	{
		// TODO Auto-generated method stub
		arg0.execSQL("create table "
				+ TABLE_MUSIC
				+ " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ " songid integer, albumid integer, duration integer, musicname varchar(10), "
				+ "artist char, data char, folder char, musicnamekey char, artistkey char, favorite integer)");
		arg0.execSQL("create table "
				+ TABLE_ALBUM
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
				+ "album_name char, album_id integer, number_of_songs integer, album_art char)");
		arg0.execSQL("create table "
				+ TABLE_ARTIST
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, artist_name char, number_of_tracks integer)");
		arg0.execSQL("create table "
				+ TABLE_FOLDER
				+ "(_id INTEGER PRIMARY KEY AUTOINCREMENT, folder_name char, folder_path char)");
		arg0.execSQL("create table "
				+ TABLE_FAVORITE
				+ " (_id integer,"
				+ " songid integer, albumid integer, duration integer, musicname varchar(10), "
				+ "artist char, data char, folder char, musicnamekey char, artistkey char, favorite integer)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2)
	{
		// TODO Auto-generated method stub
		if(arg2>arg1)
		{
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTIST);
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_ALBUM);
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);
			arg0.execSQL("DROP TABLE IF EXISTS " + TABLE_FOLDER);
			onCreate(arg0);
		}
	}

	public void clearTable()
	{
		SQLiteDatabase db=this.getWritableDatabase();
		db.delete(TABLE_ALBUM, null, null);
		db.delete(TABLE_ARTIST, null, null);
		db.delete(TABLE_FAVORITE, null, null);
		db.delete(TABLE_FOLDER, null, null);
		db.delete(TABLE_MUSIC, null, null);
	}
}
