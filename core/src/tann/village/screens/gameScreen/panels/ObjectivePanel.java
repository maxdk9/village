package tann.village.screens.gameScreen.panels;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

import com.badlogic.gdx.utils.Array;
import tann.village.Images;
import tann.village.gameplay.effect.Cost;
import tann.village.gameplay.island.objective.Objective;
import tann.village.screens.gameScreen.GameScreen;
import tann.village.gameplay.village.Inventory;
import tann.village.screens.gameScreen.panels.review.InfoPanel;
import tann.village.util.*;

public class ObjectivePanel extends Group{
	

	int WIDTH = 100, HEIGHT = 60, OBJHEIGHT= 140;
	
	Array<Objective> objectives = new Array<>();
	public ObjectivePanel() {
		setSize(WIDTH, HEIGHT);
        refresh();
	}

	public void addObject(Objective obj){
	    objectives.add(obj);
        setSize(WIDTH, HEIGHT + OBJHEIGHT *objectives.size);
	    refresh();
    }

	public void refresh(){
	    clearChildren();
		Layoo l = new Layoo(this);

		TextBox title  = new TextBox("Objective", Fonts.fontSmall, WIDTH, Align.center);
        l.row(1);
        l.actor(title);

		for(Objective obj:objectives){
            TextBox objText = new TextBox(obj.getTitleString(), Fonts.fontSmall, WIDTH, Align.center);
            TextBox progress = new TextBox(obj.getProgressString(), Fonts.fontSmall, WIDTH, Align.center);
            l.row(1);
            l.actor(objText);
            l.row(1);
            l.actor(progress);
        }

        l.row(1);
		l.layoo();
	}

    @Override
    public void draw(Batch batch, float parentAlpha) {
	    batch.setColor(Colours.brown_dark);
        Draw.fillActor(batch,this);
        super.draw(batch, parentAlpha);
    }

}
