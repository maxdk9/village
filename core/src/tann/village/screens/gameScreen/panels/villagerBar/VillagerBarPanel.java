package tann.village.screens.gameScreen.panels.villagerBar;

import com.badlogic.gdx.graphics.g2d.Batch;
import tann.village.Main;
import tann.village.gameplay.village.villager.Villager;
import tann.village.screens.gameScreen.GameScreen;
import tann.village.screens.gameScreen.panels.InventoryItemPanel;
import tann.village.util.Lay;
import tann.village.util.Layoo;

public class VillagerBarPanel extends Lay{

    @Override
    public void layout() {
        setSize(VillagerIcon.width(), Main.height-GameScreen.getConstructionCircleSize());
        clearChildren();
        Layoo l = new Layoo(this);
        for(Villager v: GameScreen.get().villagers){
            l.actor(v.getIcon());
            l.row(1);
        }
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}