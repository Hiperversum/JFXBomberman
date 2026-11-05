package model.world.components.entity.character.behavior;

import model.world.components.entity.character.Character;
import java.awt.*;
import java.io.Serializable;

public class DamageBox implements Serializable {
    private Rectangle damageBox;
    private Character character;
    public DamageBox(){
        damageBox = new Rectangle();
    }
    public DamageBox(int y,int x,int width,int height,Character character){
        damageBox = new Rectangle();
        damageBox.y = y;
        damageBox.x = x;
        damageBox.width = width;
        damageBox.height = height;
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }
    public Rectangle getDamageRectangle() {
        return damageBox;
    }
    public void setDamageRectangle(Rectangle damageBox) {
        this.damageBox = damageBox;
    }
}
