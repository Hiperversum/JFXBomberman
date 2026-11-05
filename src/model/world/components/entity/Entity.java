package model.world.components.entity;
import model.engine.UpdateLoopListener;
import model.world.components.WorldComponent;
import model.world.components.entity.character.behavior.DamageBox;
import model.world.components.entity.objects.Destructible;


public abstract class Entity extends WorldComponent implements UpdateLoopListener, Destructible {
    protected DamageBox damageBox;
    protected EntityBox entityBox;
    //CONSTRUCTOR
    public Entity(){
        super();
    }

    public Entity(int y,int x){
        super(y,x);
    }

    //GETTERS:
    public DamageBox getDamageBox() {
        return damageBox;
    }
    public EntityBox getEntityBox() {
        return entityBox;
    }

    //SETTERS:



}
