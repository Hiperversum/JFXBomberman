package model.world.components.entity.character.behavior.movement;


import model.utility.RandomUtils;
import model.world.components.entity.character.Character;
import model.managers.MapManager;
import model.world.components.entity.character.behavior.Behavior;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.objects.Teleport;
import model.world.components.tiles.Tile;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMovement extends MovementPath {
	private static final long serialVersionUID = 2878635318208487162L;
    private int distanceNextChange = 3;
    private List<Behavior> possibleDirections = new ArrayList<>();
    private Character character;
    private Behavior direction;
    private Point nextChange;
    private Random random = new Random();
    public RandomMovement(Character character) {
        this.character = character;
        this.direction = character.getBehavior();
    }
    @Override
    public void chooseDirection() {
        if(nextChange == null)  setNextChange();
        else if(checkIsNextChange() || ((CharaterBox)character.getEntityBox()).CollisionDetection()) {
            Point collisionBoxCenter = character.getEntityBox().getEntityRectangleCenter();
            int y = collisionBoxCenter.y / tileSize;
            int x = collisionBoxCenter.x / tileSize;
            possibleDirections.clear();
            Tile[][] map = MapManager.getInstance().getMap();
            if ((map[y - 1][x].isCollision() || map[y - 1][x].hasAnObject()) == false || (map[y - 1][x].hasAnObject() && map[y - 1][x].getObj() instanceof Teleport))
                possibleDirections.add(Behavior.UP);
            if ((map[y + 1][x].isCollision() || map[y + 1][x].hasAnObject()) == false || (map[y + 1][x].hasAnObject() && map[y + 1][x].getObj() instanceof Teleport))
                possibleDirections.add(Behavior.DOWN);
            if ((map[y][x - 1].isCollision() || map[y][x - 1].hasAnObject()) == false || (map[y][x - 1].hasAnObject() && map[y][x - 1].getObj() instanceof Teleport))
                possibleDirections.add(Behavior.LEFT);
            if ((map[y][x + 1].isCollision() || map[y][x + 1].hasAnObject()) == false || (map[y][x + 1].hasAnObject() && map[y][x + 1].getObj() instanceof Teleport))
                possibleDirections.add(Behavior.RIGHT);
            //possibleDirections.forEach(System.out::println);
            if (possibleDirections.size() == 0) return;
            int randomInt = random.nextInt(possibleDirections.size());
            direction = possibleDirections.get(randomInt);
            character.setBehavior(direction);

            setNextChange();
            //System.out.println("CENTER:" +character.getCollisionBoxCenter());
            //System.out.println("NEXTDESTINAZION"+character.getNextChange());
        }
    }
    public Point getNextChangePoint(){
        return nextChange;
    }
    private void setNextChange() {
        distanceNextChange = RandomUtils.generateRandomInt(2,3);
        Point collisionBoxCenter = character.getEntityBox().getEntityRectangleCenter();
        int posYCenter = (((int)collisionBoxCenter.getY())/tileSize);
        int posXCenter = (((int)collisionBoxCenter.getX())/tileSize);
        switch (direction) {
            case DOWN:
                posYCenter += distanceNextChange;
                break;
            case UP:
                posYCenter -= distanceNextChange;
                break;
            case RIGHT:
                posXCenter += distanceNextChange;
                break;
            case LEFT:
                posXCenter -= distanceNextChange;
                break;
        }
        posYCenter *= tileSize;
        posXCenter *= tileSize;
        posYCenter += tileSize/2;
        posXCenter += tileSize/2;
        nextChange = new Point(posXCenter,posYCenter);
    }
    private boolean checkIsNextChange(){
        Rectangle collisionBox = character.getEntityBox().getEntityRectangle();
        if (nextChange==null) return false;
        if (direction == Behavior.RIGHT && collisionBox.getCenterX()>= nextChange.x) return true;
        else if (direction == Behavior.DOWN && collisionBox.getCenterY()>= nextChange.y) return true;
        else if (direction == Behavior.UP && collisionBox.getCenterY()<= nextChange.y) return true;
        else if (direction == Behavior.LEFT && collisionBox.getCenterX()<= nextChange.x) return true;
        return false;
    }
}
