package model.world.components.tiles;

import model.world.components.Theme;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.stream.Stream;

public class FloorTile extends Tile{
    private static final long serialVersionUID = 1L;

    public enum Type {
        FLOOR, FLOOR_SHADOW;
        public static Stream<Type> stream() {
            return Stream.of(Type.values());
        }

        @Override
        public String toString() {
            return super.toString().toLowerCase() + ".png";
        }
    }

    //FIELD
    private Type type;
    //CONSTRUCTOR
    public FloorTile(String imagePath) {
        super(imagePath);
    }
    public FloorTile(FloorTile floor) {
        this(floor.getImagePath());
        setType(imagePath);
        setImage(imagePath);

    }

    // METHODS:
    public Type getType() {
        return type;
    }
    public void setType(String imagePath)
    {
        type = Type.stream().filter(t -> t.toString().equalsIgnoreCase(new File(imagePath).getName()))
                .findFirst().orElse(null);

    }
    public void changeToFloorShadowVersion()
    {
            type = Type.FLOOR_SHADOW;
            imagePath = imagePath.replace("floor","floor_shadow");
            setImage(imagePath);
    }
    public void changeToFloorVersion()
    {
            type = Type.FLOOR;
            imagePath = imagePath.replace("floor_shadow","floor");
            setImage(imagePath);
    }


}
