package model;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;

import java.io.IOException;

public class Cursor extends Model{
    public enum MouseButton {
            RIGHT_CLICK("cursor_right_click.png"),LEFT_CLICK("cursor.png");

            private String filename;

            MouseButton(String filename){
                this.filename = filename;
            }

            public String getFilename() {
                return filename;
            }
        }
    private int y,x,yDropped,xDropped;

    private MouseButton mouseButton;
    private Image img;
    public Cursor() {
        y = 1;
        yDropped = y;
        x = 2;
        xDropped = x;
    }
    //GETTERS:
    public Image getImg() {
        return img;
    }

    public MouseButton getMouseButton() {
        return mouseButton;
    }

    public int getY() {
            return y;
        }
    public int getX() {
        return x;
    }
    public int getYDropped() {
        return yDropped;
    }
    public int getXDropped() {
        return xDropped;
    }

    //SETTERS
    public void setImg(String filePath) {
        try {
            this.img = LoaderUtilityTools.createImageFromPath(FileManager.findFileAndDirectory(mouseButton.getFilename()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setMouseButton(MouseButton mouseButton) {
        this.mouseButton = mouseButton;
        setImg(mouseButton.getFilename());
    }
    public void setCursorEntered() {
        mouseButton = MouseButton.LEFT_CLICK;
        setImg(mouseButton.getFilename());
    }

    public void setCursorExited()
        {
            img = null;
        }


        public void setY(int y)
        {
            y = y/tileSize;
            if(y<12 && y>0) {
                this.y = y;
            }
        }
        public void setX(int x)
        {
            x = x/tileSize;
            if(x<15 && x>1) {
                this.x = x;
            }
        }
        public void setYDropped(int yDropped) {
            yDropped = yDropped/tileSize;
            if(yDropped<12 && yDropped>0) {
                this.yDropped = yDropped;
            }
        }
        public void setXDropped(int xDropped) {
            xDropped = xDropped/tileSize;
            if(xDropped<15 && xDropped>1) {
                this.xDropped = xDropped;
            }
        }


}
