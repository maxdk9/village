package tann.village.util;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class Sounds {


	public static AssetManager am= new AssetManager();

    public static String[] clacks;
    public static String[] clocks;
    public static String[] cancel;
    public static String buildPanel;
    public static String build;

    public static String unshake;
    public static String[] shake;
    public static String[] roll;
    public static String accept;

    public static String beach;
	public static void setup(){
		//sfx//
        clacks = makeSounds("clack", 4);
        clocks = makeSounds("clock", 4);
        cancel = makeSounds("drum/cancel", 1);
        buildPanel = makeSound("sfx/buildpanel.wav", Sound.class);
        build = makeSound("sfx/build.wav", Sound.class);
		beach = makeSound("music/beach.mp3", Music.class);
        shake = makeSounds("shake", 4);
        unshake = makeSound("sfx/unshake.wav", Sound.class);
        roll = makeSounds("roll", 6);
        accept = makeSound("sfx/accept.wav", Sound.class);
		//stuff to attempt to load sounds properly//
		am.finishLoading();
		Array<Sound> sounds = new Array<Sound>();
		am.getAll(Sound.class, sounds);
		for(Sound s:sounds)s.play(0);
		Array<Music> musics = new Array<Music>();
		am.getAll(Music.class, musics);
		for(Music m:musics){
			m.play();
			m.setVolume(1);
			m.stop();
		}
	}
	
	public static <T> T get(String name, Class<T> type){
		return am.get(name, type);

	}

    private static String makeSound(String path, Class type){
        am.load(path, type);
        return path;
    }

    private static String[] makeSounds(String path, int amount){
        String[] strings = new String[amount];
        for(int i=0;i<amount;i++){
            String s = "sfx/"+path+"_"+i+".wav";
            makeSound(s, Sound.class);
            strings[i]=s;
        }
        return strings;
    }
	
	private static ArrayList<Fader> faders = new ArrayList<Sounds.Fader>();
	
	public static void fade(Music m, float targetVolume, float duration){
		faders.add(new Fader(m, targetVolume, duration));
	}
	
	public static void tickFaders(float delta){
		for(int i=faders.size()-1;i>=0;i--){
			Fader f = faders.get(i);
			f.tick(delta);
			if(f.done)faders.remove(f);
		}
	}
	
	private static Music previousMusic;
	private static Music currentMusic;
	public static void playMusic(String path){
        Music m = Sounds.get(path, Music.class);
		previousMusic=currentMusic;
		if(previousMusic!=null)previousMusic.stop();
		currentMusic=m;
		currentMusic.play();
		currentMusic.setLooping(true);
		updateMusicVolume();
	}
	
	public static void updateMusicVolume(){
		if(currentMusic!=null)currentMusic.setVolume(Slider.music.getValue());
	}
	
	static class Fader{
		float startVolume;
		float targetVolume;
		Music music;
		boolean done;
		float duration;
		float ticks;
		public Fader(Music m, float targetVolume, float duration) {
			this.startVolume=m.getVolume();
			this.targetVolume=targetVolume;
			this.music=m;
			this.duration=duration;
		}
		public void tick(float delta){
			ticks+=delta;
			if(ticks>duration){
				ticks=duration;
				done=true;
			}
			float ratio = ticks/duration;
			float newVolume =startVolume+(targetVolume-startVolume)*ratio;
			music.setVolume(newVolume);
		}
	}

	static HashMap<String, Sound> soundMap = new HashMap<String, Sound>();
	public static void playSound(String string, float volume, float pitch) {
		Sound s = soundMap.get(string);
		if(s==null){
			s=get(string, Sound.class);
			soundMap.put(string, s);
		}
		s.play(Slider.SFX.getValue()*2*volume, pitch, 0);
	}

	public static void playSound(String[] strings, float volume, float pitch){
        playSound(strings[((int)(Math.random()* strings.length))], volume, pitch);
    }

}
