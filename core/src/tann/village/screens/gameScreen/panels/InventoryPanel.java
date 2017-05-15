package tann.village.screens.gameScreen.panels;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import tann.village.Main;
import tann.village.gameplay.village.Inventory;
import tann.village.gameplay.village.InventoryItem;
import tann.village.gameplay.village.Village;
import tann.village.screens.gameScreen.InventoryItemPanel;
import tann.village.util.Layoo;

public class InventoryPanel extends Group {
    Inventory inventory;
    public static final int WIDTH = Main.width, HEIGHT=InventoryItemPanel.HEIGHT;
    public InventoryPanel(Inventory inventory){
        this.inventory = inventory;
        setSize(WIDTH, HEIGHT);
        Layoo l = new Layoo(this);
        l.gap(2);
        for(int i=0;i<inventory.items.size;i++){
            l.gap(1);
            InventoryItem item = inventory.items.get(i);
            InventoryItemPanel panel = item.getPanel();
            l.actor(panel);
        }
        l.gap(3);
        UpkeepPanel upkeepPanel = Village.get().getUpkeep().getPanel();
        l.actor(upkeepPanel);
        l.gap(1);
        l.layoo();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }
}
