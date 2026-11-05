package model.world.components.tiles;

import model.world.components.Theme;

public class WallTile extends Tile {
    private static final long serialVersionUID = 1L;
    private FloorTile floorTile;
    public WallTile(String imagePath) {
        super(imagePath);
    }
    public WallTile(WallTile wall,FloorTile floorTile) {
        super(wall.getImagePath());
        this.floorTile = floorTile;
        setImage(imagePath);
        setCollision(true);
    }
    public FloorTile getFloorTile() {
        return floorTile;
    }
    public void setFloorTile(FloorTile floorTile) {
        this.floorTile = floorTile;
    }
}
