package model.world.components.entity.character;

import javafx.animation.Animation;
import javafx.scene.image.Image;
import model.engine.Animated;
import model.world.components.entity.Entity;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.character.behavior.Behavior;


public abstract class Character extends Entity implements Animated {
    //CAMPI:////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;
    protected Animation animation,timeline;
    protected transient Image[] imageFrames;
    private int speed;

    protected Behavior behavior = Behavior.DOWN;
    protected int spriteCounter = 0;
    protected int spriteNum = 0;
    protected int frameCount;
    public Character() {
        entityBox = new CharaterBox(this);
    }
    //METODI://////////////////////////////////////////////////////
    public abstract void movement();
    public abstract void animation();
    public int getSpeed() {
        return speed;
    }

    public Behavior getBehavior() {
        return behavior;
    }
    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setBehavior(Behavior behavior) {
        this.behavior = behavior;
    }

    public void updateByGameLoop(){
        if(((CharaterBox)entityBox).DamageDetection()) this.destroy();
    }
    
    @Override
    public void stop() {
    	
    	if(animation!=null) animation.stop();
    	if(timeline!=null) timeline.stop();
    	animation = null;
    	timeline = null;
    }

}
