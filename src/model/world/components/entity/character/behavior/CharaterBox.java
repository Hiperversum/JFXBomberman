package model.world.components.entity.character.behavior;

import model.managers.EntityManager;
import model.world.components.entity.Entity;
import model.world.components.entity.character.Character;
import model.managers.MapManager;
import model.world.components.entity.character.Player;
import model.world.components.entity.EntityBox;
import model.world.components.entity.character.npc.NPC;
import model.world.components.entity.objects.bomb.Bomb;
import model.world.components.tiles.Tile;
import java.util.List;

public class CharaterBox extends EntityBox  {
    //FIELDS:
	private static final long serialVersionUID = -8106993229978383198L;
    private Character character;

    private boolean Damageable = true;
    //CONSTRUCTOR:
    public CharaterBox(Character character){
        super(character);
        this.character = character;
    }
    //METHODS:

    //GETTERS:

    public boolean isDamageable() {
        return Damageable;
    }

    //SETTERS:
    public void setDamageable(boolean damageable) {
        Damageable = damageable;
    }

    //COLLISION DETECTION:
    public boolean CollisionDetection()
    {

        Tile[][] mappa = MapManager.getInstance().getLoadedMap().getMap();
        int speed = character.getSpeed();

        int entityLeftCol = entityRectangle.x/tileSize;
        int entityRightCol = (entityRectangle.x + entityRectangle.width)/tileSize;
        int entityTopRow = entityRectangle.y/tileSize;
        int entityBottomRow = (entityRectangle.y + entityRectangle.height)/tileSize;

        Tile tileNum1,tileNum2;
        switch (character.getBehavior())
        {
            case UP:
                entityTopRow = (entityRectangle.y-speed)/tileSize;
                tileNum1 = mappa[entityTopRow][entityLeftCol];
                tileNum2 = mappa[entityTopRow][entityRightCol];
                if(tileNum1.isCollision() || tileNum2.isCollision() || ObjectIsCollision (tileNum1,tileNum2)) return true;
                break;
            case LEFT:
                entityLeftCol = (entityRectangle.x -speed)/tileSize;
                tileNum1 = mappa[entityTopRow][entityLeftCol];
                tileNum2 = mappa[entityBottomRow][entityLeftCol];
                if(tileNum1.isCollision() || tileNum2.isCollision() || ObjectIsCollision (tileNum1,tileNum2))  return true;
                break;
            case DOWN:
                entityBottomRow = (entityRectangle.y + entityRectangle.height +speed)/tileSize;
                tileNum1 = mappa[entityBottomRow][entityLeftCol];
                tileNum2 = mappa[entityBottomRow][entityRightCol];
                if(tileNum1.isCollision() || tileNum2.isCollision() || ObjectIsCollision (tileNum1,tileNum2))   return true;
                break;
            case RIGHT:
                entityRightCol = (entityRectangle.x + entityRectangle.width + speed)/tileSize;
                tileNum1 = mappa[entityTopRow][entityRightCol];
                tileNum2 = mappa[entityBottomRow][entityRightCol];
                if(tileNum1.isCollision() || tileNum2.isCollision() || ObjectIsCollision (tileNum1,tileNum2)) return true;
                break;
        }
        return false;
    }
    private boolean ObjectIsCollision(Tile tileNum1, Tile tileNum2){
        if (character instanceof Player){
            if((tileNum1.getObj()!= null && !(tileNum1.getObj() instanceof Bomb) && tileNum1.getObj().isCollision()) || (tileNum2.getObj()!= null && !(tileNum1.getObj() instanceof Bomb) && tileNum2.getObj().isCollision()))
                return true;
        }
        else if((tileNum1.getObj()!= null && tileNum1.getObj().isCollision()) || (tileNum2.getObj()!= null && tileNum2.getObj().isCollision()))
            return true;
        return false;
    }


    //DAMAGE DETECTION:
    public boolean DamageDetection(){
        List<Entity> entityList = EntityManager.getInstance().getEntityList();
        for (Entity entity: entityList){
            if(isDamageable() && entity!=character && entity.getDamageBox()!=null
                    && (!(entity.getDamageBox().getCharacter() instanceof NPC && character instanceof NPC))
                    && !(entity instanceof NPC && character instanceof NPC)
                    && entityRectangle.intersects(entity.getDamageBox().getDamageRectangle())
            ){
                return true;
            }
        }
        return false;
    }


}
