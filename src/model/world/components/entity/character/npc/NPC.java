package model.world.components.entity.character.npc;
import javafx.animation.Animation;
import javafx.scene.image.Image;
import model.engine.Animated;
import model.world.components.entity.character.Character;
import model.world.components.entity.character.behavior.DamageBox;
import model.world.components.entity.character.behavior.movement.MovementPath;
import model.world.components.entity.character.behavior.movement.RandomMovement;
import model.world.components.entity.character.behavior.Attacker;
import model.world.components.entity.objects.Destructible;
import java.awt.*;
import java.io.Serializable;

public abstract class NPC extends Character implements Serializable, Destructible, Attacker {
	private static final long serialVersionUID = 234061244690858931L;
    private String name;
    protected MovementPath movementPath;
    public NPC(){}
    public NPC(String name) {
        this.name = name;
        damageBox = new DamageBox();
    }
    //GETTERS:
    public String getName() {
        return name;
    }
    public Point getNextChangePoint() {
        if (movementPath instanceof RandomMovement)
            return ((RandomMovement) movementPath).getNextChangePoint();
        return null;
    }
    @Override
    public Image getImage()
    {
        changeImageFrames();
        if(spriteNum< imageFrames.length) return imageFrames[spriteNum];
        return null;
    }
    //SETTERS:
    public abstract void changeImageFrames();

}
