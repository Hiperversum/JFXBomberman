package model.managers;

import model.Model;
import model.engine.UpdateLoopListener;
import model.world.components.entity.Entity;
import model.world.components.entity.character.Player;


import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class EntityManager extends Model implements UpdateLoopListener {
    private static EntityManager instance;
    private ArrayList<Entity> entityList = new ArrayList<>();
    //CONSTRUCTOR:
    private EntityManager(){}

    public static EntityManager getInstance() {
        if (instance==null) instance = new EntityManager();
        return instance;
    }
    //METHODS:
    public ArrayList<Entity> getEntityList() {
        return entityList;
    }
    public void addEntity(Entity entity){
        ArrayList<Entity> newList = new ArrayList<>();
        newList.add(entity);
        // Aggiungi gli elementi esistenti dopo l'entità
        newList.addAll(entityList);
        entityList = newList;
        //entityList.forEach(System.out::println);
        //System.out.println();
    }
    public void removeEntity(Entity entity){
        entityList.remove(entity);

    }
    public void clear(){
        entityList.clear();
    }
    public void setEntityList(List<Entity> entityList){
        if (this.entityList!=null) this.entityList.clear();
        this.entityList = (ArrayList<Entity>) entityList;
        addEntity(Player.getInstance());
        updateEntity(0);
        setChanged();
        notifyObservers();
    }
    public void updateEntity(long time){
        try {
            for (Entity entity : entityList) {
                entity.updateByGameLoop(time);
            }
        }
        catch (ConcurrentModificationException e){
            updateEntity(time);
        }
    }
    @Override
    public void updateByGameLoop(long currentTime) {
        updateEntity(currentTime);
        setChanged();
        notifyObservers();
    }
}
