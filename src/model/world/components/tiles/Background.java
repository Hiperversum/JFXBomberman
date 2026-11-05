package model.world.components.tiles;

import model.world.components.Theme;
import model.world.components.WorldComponent;
import model.world.components.tiles.BackgroundTile;


import java.util.Arrays;

public class Background extends WorldComponent {
    private static final long serialVersionUID = 1L;
    private BackgroundTile[] backgroundTiles;
    public Background(String[] imagePaths)
    {
        this.backgroundTiles = Arrays.stream(imagePaths).map(BackgroundTile::new).toArray(BackgroundTile[]::new);
    }

    public  BackgroundTile[] getBackgroundTiles() {
        return backgroundTiles;
    }

    @Override
    public String toString() {
        return "Background{" +
                "backgroundTiles=" + Arrays.toString(backgroundTiles) +
                '}';
    }
}
