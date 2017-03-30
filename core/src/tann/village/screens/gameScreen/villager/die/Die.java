package tann.village.screens.gameScreen.villager.die;

import java.util.HashMap;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.utils.Array;

import tann.village.Images;
import tann.village.bullet.BulletStuff;
import tann.village.bullet.CollisionObject;
import tann.village.screens.gameScreen.effect.Effect;
import tann.village.screens.gameScreen.villager.Villager;
import tann.village.screens.gameScreen.villager.Villager.VillagerType;
import tann.village.util.Colours;
import tann.village.util.Particle;

public class Die {
	
	private static HashMap<Long, Die> identityMap = new HashMap<>();
	public Villager villager;
	public VillagerType type;
	public Die(Villager villager) {
		this.villager=villager;
		setup(villager.type);
		construct();
	}
	
	public Die(VillagerType type){
		setup(type);
		this.type=type;
		construct();
	}
	
	public void setup(VillagerType type){
		switch(type){
		
			// level 0
		case Villager:
			addSide(Side.food1);
			addSide(Side.food1);
			addSide(Side.wood1);
			addSide(Side.wood1);
			addSide(Side.brain);
			addSide(Side.skull);
			break;
			
			// level 1
		case Fisher:
			addSide(Side.food1);
			addSide(Side.food1);
			addSide(Side.food2);
			addSide(Side.food2);
			addSide(Side.wood1);
			addSide(Side.brain);
			break;
		case Musician:
			addSide(Side.morale1);
			addSide(Side.food1);
			addSide(Side.wood1);
			addSide(Side.food2);
			addSide(Side.wood1);
			addSide(Side.brain);
			break;
		case Mystic:
			addSide(Side.fateForFood);
			addSide(Side.fateForWood);
			addSide(Side.wood1);
			addSide(Side.food1);
			addSide(Side.skull);
			addSide(Side.brain);
			break;
		case Chopper:
			addSide(Side.food1);
			addSide(Side.wood1);
			addSide(Side.wood2);
			addSide(Side.wood3);
			addSide(Side.skull);
			addSide(Side.brain);
			break;
		case Gatherer:
			addSide(Side.food1);
			addSide(Side.food1);
			addSide(Side.food2);
			addSide(Side.wood1);
			addSide(Side.wood2);
			addSide(Side.brain);
			break;
			
		// level 2
		case Builder:
			addSide(Side.food1);
			addSide(Side.food2);
			addSide(Side.wood2);
			addSide(Side.wood3);
			addSide(Side.wood3);
			addSide(Side.brain);
			break;
		case Explorer:
			addSide(Side.food3);
			addSide(Side.wood3);
			addSide(Side.morale1);
			addSide(Side.food1wood1);
			addSide(Side.brain);
			addSide(Side.skull);
			break;
		case Farmer:
			addSide(Side.food1wood1);
			addSide(Side.food1wood1);
			addSide(Side.food2);
			addSide(Side.food3);
			addSide(Side.food3);
			addSide(Side.brain);
			break;
		case FateWeaver:
			addSide(Side.fate1);
			addSide(Side.fate1);
			addSide(Side.fate2ForWoodAndFood);
			addSide(Side.food2);
			addSide(Side.skull);
			addSide(Side.brain);
			break;
		case Leader:
			addSide(Side.morale2);
			addSide(Side.morale2);
			addSide(Side.food2);
			addSide(Side.food2);
			addSide(Side.wood2);
			addSide(Side.brain);
			break;
		default:
			break;
			
		}
	}

	public static Die getDie(long userValue){
		return identityMap.get(userValue);
	}
	
	public CollisionObject physical;
	
	
	public int getSide(){
		physical.update();
		physical.updateBounds();
		Quaternion rot = new Quaternion();
		physical.transform.getRotation(rot);
		Vector3 direction = new Vector3();
		
		direction.set(Vector3.Z);
		direction.mul(rot);
		float dot = Vector3.Y.dot(direction);
		
		if(dot>.9f){
			return 1;
		}
		else if (dot<-.9f){
			return 0;
		}
		direction.set(Vector3.X);
		direction.mul(rot);
		dot = Vector3.Y.dot(direction);
		
		if(dot>.9f){
			return 5;
		}
		else if (dot<-.9f){
			return 4;
		}
		
		direction.set(Vector3.Y);
		direction.mul(rot);
		dot = Vector3.Y.dot(direction);
		
		if(dot>.9f){
			return 3;
		}
		else if (dot<-.9f){
			return 2;
		}
		
		return -99;
	}
	
	public Array<Side> sides = new Array<>();
	
	public void addSide(Side side){
		Side copy = side.copy();
		sides.add(copy);
		for(Effect e:copy.effects) e.sourceDie=this;
	}
	
	public void construct(){
		ModelBuilder mb = new ModelBuilder();
		float amt = .5f;
		mb.begin();
		
		
		
		int attr = VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal
				| VertexAttributes.Usage.TextureCoordinates|VertexAttributes.Usage.ColorPacked;
		mb.node().id = "die";
		
		
		VertexAttribute va = new VertexAttribute(Usage.ColorPacked, 4, ShaderProgram.COLOR_ATTRIBUTE);
		
		VertexAttributes vas = new VertexAttributes(va);
		
		
		Material m =new Material(TextureAttribute.createDiffuse(sides.get(0).tr.getTexture()));
		
		
		MeshPartBuilder mpb = mb.part("die", GL20.GL_TRIANGLES, attr,
				m);
		for(int i=0;i<6;i++){
			Side side = sides.get(i);
			
			TextureRegion tr = side.tr;
			float x = (float)tr.getRegionX()/(float)tr.getTexture().getWidth();
			float y = (float)tr.getRegionY()/tr.getTexture().getHeight();
			mpb.setColor(i/255f, 1,  1, 1);	
			
			
			float normalX = x;
			float normalY = y;
			
			switch(i){
				case 0: mpb.rect(-amt, -amt, -amt, -amt, amt, -amt, amt, amt, -amt, amt, -amt, -amt, normalX, normalY, -1); break;
				case 1: mpb.rect(-amt, amt, amt, -amt, -amt, amt, amt, -amt, amt, amt, amt, amt, normalX, normalY, 1); break;
				case 2: mpb.rect(-amt, -amt, amt, -amt, -amt, -amt, amt, -amt, -amt, amt, -amt, amt, normalX, normalY, 0); break;
				case 3: mpb.rect(-amt, amt, -amt, -amt, amt, amt, amt, amt, amt, amt, amt, -amt, normalX, normalY, 0); break;
				case 4: mpb.rect(-amt, -amt, amt, -amt, amt, amt, -amt, amt, -amt, -amt, -amt, -amt, normalX, normalY, 0); break;
				case 5: mpb.rect(amt, -amt, -amt, amt, amt, -amt, amt, amt, amt, amt, -amt, amt, normalX, normalY, 0); break;
			}
			
		}
		Model model = mb.end(); 
		
		
		
		
		
		model.getNode("die").parts.get(0).setRenderable(BulletStuff.renderable);
		
		CollisionObject co = null;
		co = new CollisionObject(model, "die", new btBoxShape(new Vector3(0.5f, 0.5f, 0.5f)), BulletStuff.mass);
		co.transform.trn(MathUtils.random(-2.5f, 2.5f), 1.5f, MathUtils.random(-2.5f, 2.5f));
		co.body.setWorldTransform(co.transform);
		co.body.setUserValue(BulletStuff.instances.size);
		co.body.setCollisionFlags(	
				co.body.getCollisionFlags() | btCollisionObject.CollisionFlags.CF_CUSTOM_MATERIAL_CALLBACK);
		physical=co;
		physical.body.setActivationState(4);
		
	}
	
	public boolean isMoving(){
		return physical.isMoving();
	}
	
	static int count;
	public void roll() {
		float sideways = 7;
		float upwards = 12;
		physical.body.applyCentralImpulse(new Vector3(Particle.rand(-sideways, sideways), upwards, Particle.rand(-sideways, sideways)));
		float rotationalForce = 2.0f;
		physical.body.applyTorqueImpulse(new Vector3(Particle.rand(-rotationalForce, rotationalForce),Particle.rand(-rotationalForce, rotationalForce),Particle.rand(-rotationalForce, rotationalForce)));
	}

	public void activate() {
		for(Effect e:sides.get(getSide()).effects) e.activate();
	}

	public void addToScreen() {
		BulletStuff.instances.add(physical);
		BulletStuff.dynamicsWorld.addRigidBody(physical.body, BulletStuff.OBJECT_FLAG, BulletStuff.ALL_FLAG);
		
	}

	public void removeFromScreen() {
		BulletStuff.instances.removeValue(physical, true);
		BulletStuff.dynamicsWorld.removeRigidBody(physical.body);
	}

	public void destroy() {
		removeFromScreen();
	}
}
