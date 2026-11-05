package model.editor.factory;
import model.editor.LevelEditor;
import model.world.components.WorldComponent;
import model.world.components.tiles.BackgroundTile;
import model.world.components.tiles.FloorTile;
import model.world.components.tiles.Tile;
import model.world.components.tiles.WallTile;

public class TileFactory {

    public Tile createTile(WorldComponent worldComponent, int y, int x){
        if (worldComponent instanceof BackgroundTile) return new BackgroundTile((BackgroundTile) worldComponent);
        if(worldComponent instanceof FloorTile) return new FloorTile((FloorTile) worldComponent);
        else if (worldComponent instanceof WallTile) {
            LevelEditor levelEditor = LevelEditor.getInstance();
            if (((WallTile) worldComponent).getFloorTile()!=null) return new WallTile((WallTile) worldComponent, ((WallTile)worldComponent).getFloorTile());
            else if (levelEditor.getMap()[y][x] instanceof WallTile) return new WallTile((WallTile) worldComponent, ((WallTile) levelEditor.getMap()[y][x]).getFloorTile());
            return new WallTile((WallTile) worldComponent, (FloorTile) levelEditor.getMap()[y][x]);
        }
        return null;
    }
}
