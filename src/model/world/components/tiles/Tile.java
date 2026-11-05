package model.world.components.tiles;

import javafx.scene.image.Image;
import java.io.*;


import model.world.components.Theme;
import model.world.components.WorldComponent;
import model.world.components.entity.objects.Objects;


public abstract class Tile extends WorldComponent  {
    private static final long serialVersionUID = 1L;

    //CAMPI:
    private Objects obj;

    protected String imagePath;

    //COSTRUTTORE
    public Tile(String imagePath) {
        this.imagePath = imagePath;
    }
    //METODI:

    //GETTERS:
    public Objects getObj() {
        return obj;
    }

    public String getImagePath() {
        return imagePath;
    }
    public boolean hasAnObject(){
        return obj!=null;
    }
    //SETTERS:
    public void setObject(Objects obj)
    {
        this.obj = obj;
    }

    //CUSTOM SERIALIZED METHOD
    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        setImage(imagePath);
    }

}










