package model.world.components.entity.objects;
import javafx.scene.image.Image;
import model.world.components.entity.Entity;

import java.io.*;
import java.util.Arrays;


public abstract class Objects extends Entity {
    //FIELDS:
    protected boolean collision;
    //CONSTRUCTOR:
    public Objects(){}

    public Objects(int y, int x){
        super(y,x);
    }
    //GETTERS
    public boolean isCollision() {
        return collision;
    }

    //CUSTOM SERIALIZED METHOD
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
    }
}
