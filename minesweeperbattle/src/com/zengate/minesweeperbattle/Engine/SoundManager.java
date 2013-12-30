package com.zengate.minesweeperbattle.Engine;

import java.util.ArrayList;
import java.util.Hashtable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class SoundManager {
	private static Hashtable<String, Sound> sounds;
	private static Hashtable<String, Music> music;
	private static ArrayList<String> names;
	
	private static boolean sfxOn = true;
	private static boolean bgmOn = true;
	
	public static void Init()
	{
		sounds = new Hashtable<String, Sound>();
		music = new Hashtable<String, Music>();
		names = new ArrayList<String>();
	}
	
	public static void AddSound(String path, String soundName)
	{
		Sound newSound = Gdx.audio.newSound(Gdx.files.internal(path));
		newSound.stop();
		sounds.put(soundName, newSound);
		names.add(soundName);
	}
	
	public static void AddMusic(String path, String musicName)
	{
		Music newSound = Gdx.audio.newMusic(Gdx.files.internal(path));
		music.put(musicName, newSound);
		//names.add(soundName);
	}
	
	public static void PlaySound(String soundName, boolean loop)
	{
		if (sfxOn){
			if(sounds.containsKey(soundName))
			{
				if (!loop){
					sounds.get(soundName).play();
				}else{
					sounds.get(soundName).loop();
				}
				//long id = sounds.get(soundName).play();
				//sounds.get(soundName).setLooping(id, loop);
			}
		}
	}
	
	public static void PlayMusic(String soundName, boolean loop)
	{
		if (bgmOn){
			if(music.containsKey(soundName))
			{
				music.get(soundName).play();
				music.get(soundName).setLooping(loop);
			}	
		}
	}
	
	public static boolean MusicIsPlaying(String musicName){
		
		if(music.containsKey(musicName))
		{
			return music.get(musicName).isPlaying();
		}
		
		System.out.println("Trying to acces non existant musicIsPlaying:" +musicName);
		return false;
	}
	
	public static void StopSound(String soundName)
	{
		if(sounds.containsKey(soundName))
		{
			sounds.get(soundName).stop();
		}
	}
	
	public static void StopMusic(String musicName)
	{
		if(music.containsKey(musicName))
		{
			music.get(musicName).stop();
		}
	}
	
	public static void Dispose()
	{
		for(int i = 0; i < names.size(); i++)
		{
			sounds.get(names.get(i)).dispose();
		}
		
		sounds.clear();
		names.clear();
	}
}
