package model.world.components;

import javafx.scene.image.Image;
import model.Model;
import model.utility.LoaderUtilityTools;
import java.io.Serializable;

public abstract class WorldComponent extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    private int x,y;
    private transient Image image;
    private boolean collision;

    public WorldComponent(){}

    public WorldComponent(int y, int x) {
        this.y = y;
        this.x = x;
    }
    //GETTERS:

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public Image getImage() {
        return image;
    }
    public boolean isCollision() {
        return collision;
    }

    //SETTERS:
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public void setImage(Image image) {
        this.image = image;
    }
    public Image setImage(String imagePath)
    {
        return image = LoaderUtilityTools.createImage(imagePath);
    }
    public void setCollision(boolean collision) {
        this.collision = collision;
    }
}
