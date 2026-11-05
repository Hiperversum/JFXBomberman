package view.tools;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import model.Cursor;
import model.world.components.entity.Entity;
import model.world.components.entity.character.behavior.Behavior;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.character.npc.NPC;
import model.world.components.entity.character.Player;
import model.world.components.tiles.Tile;
import java.awt.*;
import java.util.List;


public class Brush {
    //CAMPI:////////////////////////////////////////////////////////
    private static int tileSize;
    private GraphicsContext brush;
    private static boolean showInfo;

    //COSTRUTTORE://///////////////////////////////////////////////
    public Brush(Canvas canvas, int tileSize) {
        Brush.tileSize = tileSize;
        brush = canvas.getGraphicsContext2D();
    }

    //METHODS://///////////////////////////////////////////////////
    public static void setShowInfo(){
        showInfo = showInfo ? false : true;
    }

    public void drawCursor(Cursor c) {
        Image img = c.getImg();
        int y_start = Math.min(c.getY(), c.getYDropped());
        int y_end = Math.max(c.getY(), c.getYDropped());
        int x_start = Math.min(c.getX(), c.getXDropped());
        int x_end = Math.max(c.getX(), c.getXDropped());
        for (int y = y_start; y <= y_end; y++)
            for (int x = x_start; x <= x_end; x++)
                brush.drawImage(img, x * tileSize, y * tileSize, tileSize, tileSize);
    }



    public void drawMap(Tile[][] map) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] != null) {
                    brush.drawImage(map[i][j].getImage(), j * tileSize, i * tileSize, tileSize, tileSize);
                    if (map[i][j].getObj() != null)
                        brush.drawImage(map[i][j].getObj().getImage(), j * tileSize, i * tileSize, tileSize, tileSize);
                }
            }
        }
    }

    //ENTITY BRUSH
    public void drawEntity(List<Entity> entityList) {
        for (Entity e : entityList) {
            if(e.getImage()!=null) {
                if(e instanceof  Player) drawPlayer((Player) e);
                else if (e instanceof NPC)
                    brush.drawImage(e.getImage(), e.getX(), e.getY() - e.getImage().getHeight() + tileSize - 10);
                else
                    brush.drawImage(e.getImage(), e.getX(), e.getY());
                drawInfo(e);
            }
        }
    }

    public void drawPlayer(Player player){

        if (!((CharaterBox)player.getEntityBox()).isDamageable() && player.getBehavior()!= Behavior.DEATH){
            long currentTime = System.currentTimeMillis();
            if ((currentTime / player.getTIME_CHANGE_FRAME()) % 2 == 0) {
                brush.drawImage(player.getImage(), player.getX(), player.getY() - player.getImage().getHeight() + tileSize - 8);
            }
        }
        else brush.drawImage(player.getImage(), player.getX(), player.getY() -player.getImage().getHeight() + tileSize - 8);

    }


    //DRAW INFO:
    public void drawNPCInfo(NPC npc) {
        Point point = npc.getEntityBox().getEntityRectangleCenter();
        if (point != null) {
            double x = point.getX();
            double y = point.getY();
            brush.setStroke(Color.PINK);
            brush.setLineWidth(1);
            brush.strokeRect(x, y, 1, 1);

            if (npc.getNextChangePoint() != null) {
                x = npc.getNextChangePoint().getX();
                y = npc.getNextChangePoint().getY();
                brush.setStroke(Color.YELLOW);
                brush.setLineWidth(1);
                brush.strokeRect(x, y, 1, 1);

            }
        }
    }

    public void drawInfo(Entity entity) {
        if (showInfo){
            if (entity.getEntityBox()!=null) {
                Rectangle r = entity.getEntityBox().getEntityRectangle();
                brush.setStroke(Color.RED);
                brush.setLineWidth(2);
                brush.strokeRect(r.x, r.y, r.width, r.height);
            }
            if(entity instanceof NPC) drawNPCInfo((NPC)entity);
            if (entity.getDamageBox() != null) {
                //drawDamageBox
                Rectangle r = entity.getDamageBox().getDamageRectangle();
                brush.setStroke(Color.DARKRED);
                brush.setLineWidth(2);
                brush.strokeRect(r.x, r.y, r.width, r.height);
            }
        }
    }

}





















