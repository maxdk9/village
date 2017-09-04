package tann.village.gameplay.island.event;

import com.badlogic.gdx.utils.Array;

import tann.village.gameplay.effect.Cost;
import tann.village.gameplay.effect.Eff;
import tann.village.gameplay.village.Village;

public class Event {


    public String title;
    public String description;
    public Array<Outcome> outcomes = new Array<>();
    public Array<Eff> effects = new Array<>();
    public Array<Eff> requirements = new Array<>();
    public float chance = 1;
	boolean story;
	public int storyTurn = -1;
	private int maxUses=999;
	public int uses = 0;
	public int minTurn = -1, maxTurn = -1;
    public float joel=-123456789;

    public Event(String title){
        this(title,null);
    }

    public Event(String title, String description){
        this.title=title;
        this.description=description;
    }

    public boolean isPotential() {
        if(this.uses>=maxUses) return false;
        float joel = Village.get().getJoel() + this.joel;
        if(joel>1||joel<-1) return false;
        int currentTurn = Village.get().getDayNum();
        if((minTurn!=-1&&currentTurn<minTurn) || (maxTurn!=-1&&currentTurn>maxTurn)) return false;
        for(Eff e: requirements){
            if(!Village.getInventory().isEffectValid(e)) return false;
        }
		return true;
	}
	
	public int getGoodness(){
        return -(int)(Math.signum(joel));
    }

    public void initialAction(){
        Village.get().addJoel(joel);
        Village.get().activate(effects, true, false);
        this.uses++;
    }

	public String toString(){
	    return title;
    }

    public boolean isStory() {
	    return story;
    }

    public void eff(Eff eff) {
	    effects.add(eff);
    }

    public void effR(Eff eff) {
	    eff(eff);
	    req(eff);
    }

    public void req(Eff eff) {
	    requirements.add(eff);
    }

    public void joel(double joel) {
	    if(Math.abs(joel)>2){
            System.err.println("too much joel: "+this);
        }
	    this.joel=(float)joel;
    }

    public void chance(float chance){
	    chance(chance,999);
    }

    public void chance(double chance, int amount){
        this.chance=(float)chance; this.maxUses =amount;
    }

    public void turn(int min, int max){
        this.minTurn = min; this.maxTurn = max;
    }

    public void storyTurn(int turn) {
        this.storyTurn=turn;
        this.story=true;
        this.joel=0;
    }

    public void addOutcome(String description) {
        addOutcome(description,0);
    }

    public void addOutcome(String description, int fateCost) {
        addOutcome(description, 0,0, fateCost);
    }

    public void addOutcome(String description, int foodCost, int woodCost, int fateCost) {
        Cost c = null;
        if(foodCost!=0 || woodCost!= 0 || fateCost!=0){
            c = new Cost().food(foodCost).wood(woodCost).fate(fateCost);
        }
        Outcome o = new Outcome(description, effects, c);
        effects = new Array<>();
        outcomes.add(o);
        if(outcomes.size==3){
            for(Outcome oc:outcomes){
                oc.setTriple();
            }
        }
    }

    public void validate(){
        if(joel<=-2||joel>=2) System.err.println("joel out of bounds for "+this);
        if(storyTurn==-1 && isStory()) System.err.println("story with no turn for "+this);
        if(title==null || description==null) System.err.println("no title or desc for "+this);
        if(chance<=0 && !isStory()) System.err.println("no chance for "+this);
        if(maxUses <=0 && !isStory()) System.err.println("no uses for "+this);
    }

}
