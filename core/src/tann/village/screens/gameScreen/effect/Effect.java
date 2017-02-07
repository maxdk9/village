package tann.village.screens.gameScreen.effect;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import tann.village.Images;
import tann.village.screens.gameScreen.GameScreen;
import tann.village.screens.gameScreen.villager.die.Die;

public class Effect {

	public enum EffectType{
		Brain(Images.brain), 
		Food(Images.food), 
		Wood(Images.wood),
		Skull(Images.side_skull),
		LevelUp(Images.level_up),
		Morale(Images.morale), 
		Fate(Images.fate);

		public TextureRegion region;
		boolean special;
		EffectType(TextureRegion region){
			this.region=region;
		}
		EffectType(TextureRegion region, String extraText){
			this.region=region;
			this.special=true;
		}
	}
	
	public enum EffectSource{
		Dice, Upkeep, Event, Building
	}
	
	
	public final EffectType type;
	public final EffectSource source;
	public int value;
	public Die sourceDie;
	public Effect(EffectType type, int value, EffectSource source, Die sourceDie){
		this.type=type;
		this.value=value;
		this.source=source;
		this.sourceDie = sourceDie;
	}
	
	public Effect(EffectType type, int value, EffectSource source){
		this.type=type;
		this.value=value;
		this.source=source;
	}
	
	public Effect(EffectType type, EffectSource source){
		this.type=type;
		this.value=0;
		this.source=source;
	}
	
	public void activate(){
		switch(type){
		case Brain:
			sourceDie.villager.gainXP(value);
			break;
		}
		GameScreen.get().addEffect(this);
	}
	
	public String toString(){
		return type +": "+value+" from "+source; 
	}
	
	public Effect copy(){
		Effect result = new Effect(type, value, source);
		return result;
	}

	public String getValueString() {
		return (value>0?"+":"")+value;
	}
	
}
