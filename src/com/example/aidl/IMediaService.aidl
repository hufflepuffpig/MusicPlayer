package com.example.aidl;
import com.example.model.MusicInfo;

interface IMediaService
{
	void refreshMusicList(in List<MusicInfo> infos);
	void updateNotification(String musicname,String artist);
	void cancelNotification();
	
	boolean playById(int id);
	boolean prepare();
	boolean replay();
	boolean pause();
	boolean prev();
	boolean next();
	void sendMyMusicBroadcast();
	
	int getPlayState();
	int getPalyMode();
	void setPlayMode(int mode);
	int getCurMusicIndex();
	MusicInfo getCurMusic();
	List<MusicInfo> getCurMusicList();
	
	int getPosition();
	int getDuration();
	boolean seekTo(int progress);
	
	void exit();
}
