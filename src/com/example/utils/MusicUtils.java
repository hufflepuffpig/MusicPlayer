package com.example.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Files.FileColumns;
import android.util.Log;

import com.example.db.AlbumInfoDao;
import com.example.db.ArtistInfoDao;
import com.example.db.FavoriteInfoDao;
import com.example.db.FolderInfoDao;
import com.example.db.MusicInfoDao;
import com.example.model.AlbumInfo;
import com.example.model.ArtistInfo;
import com.example.model.FolderInfo;
import com.example.model.MusicInfo;
import com.example.musicplayer.R;

public class MusicUtils
{
	//定义ContentReslover查询时查询的列
	private static String[] proj_music={MediaStore.Audio.Media._ID,MediaStore.Audio.Media.ALBUM_ID,
		MediaStore.Audio.Media.DURATION,MediaStore.Audio.Media.TITLE,MediaStore.Audio.Media.ARTIST,
		MediaStore.Audio.Media.DATA};
	private static String[] proj_album = new String[] { Albums.ALBUM,
		Albums.NUMBER_OF_SONGS, Albums._ID, Albums.ALBUM_ART };
	private static String[] proj_artist = new String[] {
		MediaStore.Audio.Artists.ARTIST,
		MediaStore.Audio.Artists.NUMBER_OF_TRACKS };
	private static String[] proj_folder = new String[] { FileColumns.DATA };
	//定义数据库接口Dao层的对象，全部使用单例模式
	private static MusicInfoDao mMusicInfoDao;
	private static AlbumInfoDao mAlbumInfoDao;
	private static ArtistInfoDao mArtistInfoDao;
	private static FolderInfoDao mFolderInfoDao;
	private static FavoriteInfoDao mFavoriteInfoDao;
	
	private static final HashMap<Long, Bitmap> mArtWorkCache=new HashMap<>();
	private static final Uri sArtWorkUri=Uri.parse("content://media/external/audio/albumart");
	private static Bitmap defaultBmp=null;
	
	public static List<MusicInfo> queryMusic(Context context)
	{
		if(mMusicInfoDao==null)
		{
			mMusicInfoDao=new MusicInfoDao(context);
		}
		
		if(mMusicInfoDao.hasData())
		{
			return mMusicInfoDao.getMusicInfos();
		}else {
			ContentResolver cr=context.getContentResolver();
			List<MusicInfo> list=getMusicList(cr.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj_music, null, null, MediaStore.Audio.Media.ALBUM_KEY));
			Log.e("Refresh", list.size()+"");
			mMusicInfoDao.saveMusicInfo(list);
			return list;
		}
	}
	public static List<MusicInfo> queryMusic(Context context,int from,Object obj)
	{
		if(mMusicInfoDao==null)
		{
			mMusicInfoDao=new MusicInfoDao(context);
		}
		if(mMusicInfoDao.hasData())
		{
			return mMusicInfoDao.getMusicInfoByType(from,obj);
		}else {
			return null;
		}
		
	}
	public static List<MusicInfo> getMusicList(Cursor cursor)
	{
		if(cursor==null)
		{
			return null;
		}
		List<MusicInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			MusicInfo info=new MusicInfo();
			info.songid=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
			info.albumid=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			info.duration=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));
			info.musicname=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
			info.artist=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
			String filepath=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
			info.data=filepath;
			info.folder=filepath.substring(0, filepath.lastIndexOf(File.separator));
			info.musicnamekey=StringHelper.getPingYin(info.musicname);
			info.artistkey=StringHelper.getPingYin(info.artist);
			list.add(info);
		}
		cursor.close();
		return list;
	}

	
	public static List<AlbumInfo> queryAlbum(Context context)
	{
		if(mAlbumInfoDao==null)
		{
			mAlbumInfoDao=new AlbumInfoDao(context);
		}
		if(mAlbumInfoDao.hasData())
		{
			return mAlbumInfoDao.getAlbumInfo();
		}else {
			ContentResolver cr=context.getContentResolver();
			List<AlbumInfo> list=getAlbumList(cr.query(Albums.EXTERNAL_CONTENT_URI, proj_album, null, null, Media.ALBUM_KEY));
			mAlbumInfoDao.saveAlbumInfo(list);
			return list;
		}
	}
	public static List<AlbumInfo> getAlbumList(Cursor cursor)
	{
		List<AlbumInfo> list=new ArrayList<AlbumInfo>();
		while(cursor.moveToNext())
		{
			AlbumInfo info=new AlbumInfo();
			info.album_name=cursor.getString(cursor.getColumnIndex(Albums.ALBUM));
			info.album_id=cursor.getInt(cursor.getColumnIndex(Albums._ID));
			info.number_of_songs=cursor.getInt(cursor.getColumnIndex(Albums.NUMBER_OF_SONGS));
			info.album_art=cursor.getString(cursor.getColumnIndex(Albums.ALBUM_ART));
			list.add(info);
		}
		cursor.close();
		return list;
	}
	

	public static List<ArtistInfo> queryArtist(Context context)
	{
		if(mArtistInfoDao==null)
		{
			mArtistInfoDao=new ArtistInfoDao(context);
		}
		if(mArtistInfoDao.hasData())
		{
			return mArtistInfoDao.getArtistInfo();
		}else {
			ContentResolver cr=context.getContentResolver();
			List<ArtistInfo> list=getArtistList(cr.query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI, proj_artist, null, null, MediaStore.Audio.Artists.NUMBER_OF_TRACKS+" desc"));
			mArtistInfoDao.saveArtistInfo(list);
			return list;
		}
	}
	public static List<ArtistInfo> getArtistList(Cursor cursor)
	{
		List<ArtistInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			ArtistInfo info=new ArtistInfo();
			info.artist_name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
			info.number_of_tracks=cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
			list.add(info);
		}
		cursor.close();
		return list;
	}

	public static List<FolderInfo> queryFolder(Context context)
	{
		if(mFolderInfoDao==null)
		{
			mFolderInfoDao=new FolderInfoDao(context);
		}
		if(mFolderInfoDao.hasData())
		{
			return mFolderInfoDao.getFolderInfo();
		}else {
			ContentResolver cr=context.getContentResolver();
			Uri uri=MediaStore.Files.getContentUri("external");
			StringBuilder mSelection = new StringBuilder(FileColumns.MEDIA_TYPE
					+ " = " + FileColumns.MEDIA_TYPE_AUDIO + " and " + "("
					+ FileColumns.DATA + " like'%.mp3' or " + Media.DATA
					+ " like'%.wma')");
			mSelection.append(") group by ( " + FileColumns.PARENT);//这句把同目录的所有音频文件合并了
			List<FolderInfo> list=getFolderList(cr.query(uri, proj_folder, mSelection.toString(), null, null));
			mFolderInfoDao.saveFolderInfo(list);
			return list;
		}
	}
	public static List<FolderInfo> getFolderList(Cursor cursor)
	{
		List<FolderInfo> list=new ArrayList<>();
		while(cursor.moveToNext())
		{
			FolderInfo info=new FolderInfo();
			String filepath=cursor.getString(cursor.getColumnIndex(FileColumns.DATA));
			info.folder_path=filepath.substring(0, filepath.lastIndexOf(File.separator));
			info.folder_name=info.folder_path.substring(info.folder_path.lastIndexOf(File.separator)+1);
			list.add(info);
		}
		cursor.close();
		return list;
	}

	public static List<MusicInfo> queryFavorite(Context context)
	{
		if(mFavoriteInfoDao==null)
		{
			mFavoriteInfoDao=new FavoriteInfoDao(context);
		}
		return mFavoriteInfoDao.getMusicInfo();
	}

	public static String makeTimeString(long millitime)
	{
		StringBuffer stringBuffer=new StringBuffer();
		int minute=(int) (millitime/(60*1000));
		if(minute<10)
		{
			stringBuffer.append("0"+minute+":");
		}else {
			stringBuffer.append(minute+":");
		}
		int second=(int) (millitime/1000-minute*60);
		if(second<10)
		{
			stringBuffer.append("0"+second);
		}else {
			stringBuffer.append(second);
		}
		return stringBuffer.toString();
	}

	public static int seekPosInListById(List<MusicInfo> infos,int id)
	{
		int ret=-1;
		for(int i=0;i<infos.size();i++)
		{
			if(infos.get(i).songid==id)
			{
				ret=i;
				break;
			}
		}
		return ret;
	}

	//处理专辑的封面
	public static Bitmap getArtWorkFromCache(Context context,long albumid)
	{
		if(mArtWorkCache.containsKey(albumid))
		{
			return mArtWorkCache.get(albumid);
		}else {
			Bitmap bmp=getArtWorkQuick(context, albumid);
			if(bmp!=null)
			{
				mArtWorkCache.put(albumid, bmp);
				return bmp;
			}else {
				if(defaultBmp==null)
					defaultBmp=BitmapFactory.decodeResource(context.getResources(), R.drawable.img_album_background);
				return defaultBmp;
			}
		}
	}
	public static Bitmap getArtWorkQuick(Context context,long albumid)
	{
		Uri uri=ContentUris.withAppendedId(sArtWorkUri, albumid);
		ContentResolver cr=context.getContentResolver();
		try
		{
			Bitmap bmp=BitmapFactory.decodeStream(cr.openInputStream(uri));
			return bmp;
		} catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	public static void clearCache()
	{
		mArtWorkCache.clear();
		if(defaultBmp!=null)
			defaultBmp.recycle();
	}
}
