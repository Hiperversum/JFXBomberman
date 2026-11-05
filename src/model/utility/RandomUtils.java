package model.utility;

import model.Model;
import model.managers.MapManager;
import model.world.components.entity.objects.Teleport;
import model.world.components.entity.objects.obstacles.Obstacles;
import model.world.components.entity.objects.powerup.PowerUp;
import model.world.components.tiles.Tile;

import java.util.*;

public abstract class RandomUtils extends Model {
    private static Random random = new Random();

    public static int generateRandomInt(int min ,int max){
        return random.nextInt(max-min+1)+min;
    }
    public static void generateRandomPowerUp(Obstacles obstacles,int y, int x) {
        if (obstacles.getObj()==null) {
            int randomNumber = random.nextInt(3);
            if (randomNumber == 0) {
                obstacles.setObj(PowerUp.getRandomPowerUp(y * tileSize, x * tileSize));
            }
        }
    }

    public static void generateTeleport(){
        Tile[][] map = MapManager.getInstance().getMap();
        List<Obstacles> list = new ArrayList<>();
        for (int y = 0; y<Row;y++)
            for (int x = 0; x<Col;x++)
                if (map[y][x].getObj() instanceof Obstacles){
                    list.add((Obstacles) map[y][x].getObj());
                }
        Obstacles obstacle =  list.get(random.nextInt(list.size()));
        obstacle.setObj(new Teleport(obstacle.getY(),obstacle.getX()));
    }
}
