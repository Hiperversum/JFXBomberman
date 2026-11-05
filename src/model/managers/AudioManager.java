package model.managers;
import model.events.CustomEvent;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javax.sound.sampled.*;

public class AudioManager {
	public enum SoundType{
		MENU,EXPLOSION,ITEM,STAGE_CLEAR,STAGE_INTRO,VICTORY,GAME_OVER;

		public static SoundType fromString(String name) {
			return Arrays.stream(SoundType.values())
					.filter(e -> e.name().equalsIgnoreCase(name.replace(".wav","")))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("No SoundType constant with name: " + name));
		}
	}
	private static AudioManager instance;
	private Map<SoundType,String> soundMap;
	private List<String> musicList;
	private List<Clip> playingSounds = new ArrayList<>();
	private Clip musicClip;
	private int musicIndex = -1;
	private boolean blockSound;
	private AudioManager() {
		try {
			musicClip = AudioSystem.getClip();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	public static AudioManager getInstance() {
		if (instance == null)
			instance = new AudioManager();
		return instance;
	}

	//METHODS:///////////////////////////////////////////////////////////////////////////////////////////////////////
	//SETTERS:
	
	public void setSoundMap(Map<SoundType, String> soundMap) {
		this.soundMap = soundMap;
	}
	public void setMusicList(List<String> musicList) {
		this.musicList = musicList;
	}

	public void setBlockSound(boolean blockSound) {
		this.blockSound = blockSound;
	}
	//AUDIO MANAGING//////////////////////////////////////////////////////////////////////////////////////////////

	//SOUND
	public synchronized void playSound(SoundType soundType) {
		if(!blockSound) {
			String soundFileName = soundMap.get(soundType);
			try {
				InputStream in = new BufferedInputStream(new FileInputStream(soundFileName));
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
				Clip soundClip = AudioSystem.getClip();
				soundClip.open(audioIn);
				soundClip.start();
				playingSounds.add(soundClip);
				soundClip.addLineListener(event -> {
					if (event.getType() == LineEvent.Type.STOP) {
						soundClip.close();
						playingSounds.remove(soundClip);
					}
				});
				in.close();
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized void playSound(SoundType soundType, CustomEvent customEvent) {
		if(!blockSound) {
			String soundFileName = soundMap.get(soundType);
			try {
				InputStream in = new BufferedInputStream(new FileInputStream(soundFileName));
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
				Clip soundClip = AudioSystem.getClip();
				soundClip.open(audioIn);
				soundClip.start();
				playingSounds.add(soundClip);
				soundClip.addLineListener(event -> {
					if (event.getType() == LineEvent.Type.STOP) {
						soundClip.close();
						playingSounds.remove(soundClip);
						customEvent.customEvent();
					}
				});
				in.close();
			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void stopSound() {
	    if (playingSounds != null) {
	        Iterator<Clip> iterator = playingSounds.iterator();
	        while (iterator.hasNext()) {
	            Clip soundClip = iterator.next();
	            if (soundClip != null) {
	                soundClip.setFramePosition(soundClip.getFrameLength());
	                iterator.remove();
	            }
	        }
	    }
	}
	//MUSIC
	public void playMusic() {
		selectMusic();
		String musicFilename = musicList.get(musicIndex);
		if (musicFilename != null) {
			try {
				InputStream in = new BufferedInputStream(new FileInputStream(musicFilename));
				AudioInputStream audioIn = AudioSystem.getAudioInputStream(in);
				musicClip.open(audioIn);
				musicClip.start();
				musicClip.addLineListener(event -> {
					if (event.getType() == LineEvent.Type.STOP){
						musicClip.setFramePosition(0);
						musicClip.start();
					}
				});

			} catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
				e.printStackTrace();
			}
		}
	}
	private void selectMusic(){
		if (musicIndex < musicList.size()-1) musicIndex +=1;
		else musicIndex = 0;
	}
	
	public synchronized void stopMusic() {
	    if (musicClip != null) {
	        musicClip.close();
	    }
	}
	




}

