package tann.village.screens.gameScreen.panels.review;

import com.badlogic.gdx.utils.Align;

import tann.village.gameplay.effect.Effect;
import tann.village.gameplay.effect.Effect.EffectSource;
import tann.village.gameplay.effect.Effect.EffectType;
import tann.village.gameplay.village.Village;
import tann.village.screens.gameScreen.panels.EffectPanel;
import tann.village.gameplay.village.Inventory;
import tann.village.util.Fonts;
import tann.village.util.Layoo;
import tann.village.util.TextBox;

public class StarvationPanel extends InfoPanel{
	
	private static float WIDTH=400, HEIGHT=500;
	
	public StarvationPanel(int amountMissing) {
		TextBox ranOut = new TextBox("You ran out of food!", Fonts.font, WIDTH, Align.center);
		TextBox missing = new TextBox("Missing "+amountMissing+" resources", Fonts.font, WIDTH, Align.center);
		int moraleLoss = 1 + amountMissing/2;
		TextBox morale = new TextBox("You lose "+moraleLoss+" morale.", Fonts.font, WIDTH, Align.center);
		Effect moraleLossEffect =new Effect(EffectType.Morale, -moraleLoss, EffectSource.Upkeep);
		EffectPanel moralePanel=new EffectPanel(moraleLossEffect);
		Village.getInventory().activate(moraleLossEffect);
		setSize(WIDTH, HEIGHT);
		Layoo l = new Layoo(this);
		l.row(1);
		l.actor(ranOut);
		l.row(1);
		l.actor(missing);
		l.row(1);
		l.actor(morale);
		l.row(1);
		l.actor(moralePanel);
		l.row(1);
		l.layoo();
	}

}
