package model.world.components.entity.character.behavior.movement;

import model.Model;

import java.io.Serializable;

public abstract class MovementPath extends Model implements Serializable {
     public abstract void chooseDirection();

}
