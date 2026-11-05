package model.world.components.entity.effects;

import javafx.scene.image.Image;
import model.engine.Animated;
import model.managers.AnimationManager;
import model.managers.EntityManager;
import model.world.components.entity.Entity;


public abstract class Effects extends Entity implements Animated {

    protected Image[] frames;
    public Effects(int y,int x)
    {
        setY(y);
        setX(x);
    }
    public static void addEffect(Effects effect){
        EntityManager entityManager = EntityManager.getInstance();
        entityManager.addEntity(effect);
    }
    public void removeEffect(Effects effect){
        EntityManager entityManager = EntityManager.getInstance();
        entityManager.removeEntity(effect);
    }

    @Override
    public void destroy() {
        AnimationManager.getInstance().removeAnimated(this);
        EntityManager.getInstance().removeEntity(this);
    }
    
    

}
