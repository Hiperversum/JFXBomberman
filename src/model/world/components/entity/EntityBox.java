package model.world.components.entity;

import model.Model;

import java.awt.*;
import java.io.Serializable;

public class EntityBox extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    protected Rectangle entityRectangle;
    protected Point entityRectangleCenter;
    private Entity entity;
    public EntityBox(Entity entity){
        this.entity = entity;
        entityRectangle = new Rectangle();
    }
    public Point getEntityRectangleCenter() {
        return entityRectangleCenter;
    }
    public Rectangle getEntityRectangle() {
        return entityRectangle;
    }

    public void setEntityRectangleCoordinatesDefault(){
        if(entity.getImage()!= null) {
            entityRectangle.y = entity.getY() + (int) (entity.getImage().getHeight() - entityRectangle.getHeight()) / 2;
            entityRectangle.x = entity.getX() + (int) (entity.getImage().getWidth() - entityRectangle.getWidth()) / 2;
            entityRectangleCenter = new Point((int) entityRectangle.getCenterX(), (int) entityRectangle.getCenterY());
        } else {
            entityRectangle.y = entity.getY();
            entityRectangle.x = entity.getX();
        }

        }
    public void setEntityRectangleCoordinates(){
        int posY = entity.getY() + (tileSize - entityRectangle.height)/2;
        int posX = entity.getX() + (tileSize - entityRectangle.width)/2;
        entityRectangle.y = posY;
        entityRectangle.x = posX;
        entityRectangleCenter = new Point((int) entityRectangle.getCenterX(),(int) entityRectangle.getCenterY());
    }
    public void setEntityRectangleDimension(int width, int height){
        entityRectangle.width = width;
        entityRectangle.height = height;
    }
}
