package model.world.components.tiles;
import model.world.components.Theme;

import java.io.File;
import java.util.stream.Stream;

public class BackgroundTile extends Tile {
    private static final long serialVersionUID = 1L;

    public enum Type
    {
        BORDER_SX,BORDER_UP, BORDER_DX, BORDER_DOWN,
        BORDER_CORNER_SX_UP, BORDER_CORNER_SX_DOWN, BORDER_CORNER_DX_UP,  BORDER_CORNER_DX_DOWN,
        EXTERNAL_BORDER_SX, EXTERNAL_BORDER_CORNER_SX_UP, EXTERNAL_BORDER_CORNER_SX_DOWN, EXTERNAL_BORDER_DX, EXTERNAL_BORDER_CORNER_DX_UP, EXTERNAL_BORDER_CORNER_DX_DOWN,
        BORDERS, EXTERNALS_BORDERS;
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
    public BackgroundTile(BackgroundTile backgroundTile) {
        this(backgroundTile.getImagePath());
    }
    public BackgroundTile(String imagePath){
        super(imagePath);
        setType(imagePath);
        setImage(imagePath);
        setCollision(true);
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


}
