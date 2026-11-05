package model.editor.handler;
import model.Model;
import model.editor.LevelEditor;
import model.world.components.tiles.Background;
import model.world.components.tiles.BackgroundTile;
import model.world.components.tiles.Tile;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;


public class BackgroundHandler extends Model {

    private Map<BackgroundTile.Type, Consumer<BackgroundTile>> backgroundOperations;

    protected BackgroundHandler() {
        backgroundOperations = new HashMap<>();
        backgroundOperations.put(BackgroundTile.Type.EXTERNALS_BORDERS, this::setExternalsBorders);
        backgroundOperations.put(BackgroundTile.Type.BORDERS, this::setBorders);
        backgroundOperations.put(BackgroundTile.Type.BORDER_UP, this::setBorderRow);
        backgroundOperations.put(BackgroundTile.Type.BORDER_DOWN, this::setBorderRow);
        backgroundOperations.put(BackgroundTile.Type.BORDER_SX, this::setBorderCol);
        backgroundOperations.put(BackgroundTile.Type.BORDER_DX, this::setBorderCol);
        backgroundOperations.put(BackgroundTile.Type.BORDER_CORNER_SX_UP, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.BORDER_CORNER_SX_DOWN, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.BORDER_CORNER_DX_UP, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.BORDER_CORNER_DX_DOWN, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_SX, this::setExternalBorderCol);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_DX, this::setExternalBorderCol);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_CORNER_SX_UP, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_CORNER_SX_DOWN, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_CORNER_DX_UP, this::setCorner);
        backgroundOperations.put(BackgroundTile.Type.EXTERNAL_BORDER_CORNER_DX_DOWN, this::setCorner);
    }
    private void setCorner(BackgroundTile backgroundTile) {
        Tile[][] map = LevelEditor.getInstance().getMap();
        switch (backgroundTile.getType())
        {
            case BORDER_CORNER_SX_UP -> map[0][1] = backgroundTile;
            case BORDER_CORNER_SX_DOWN -> map[12][1] = backgroundTile;
            case BORDER_CORNER_DX_UP ->  map[0][15] = backgroundTile;
            case BORDER_CORNER_DX_DOWN -> map[12][15] = backgroundTile;
            case EXTERNAL_BORDER_CORNER_SX_UP -> map[0][0] = backgroundTile;
            case EXTERNAL_BORDER_CORNER_SX_DOWN -> map[12][0] = backgroundTile;
            case EXTERNAL_BORDER_CORNER_DX_UP ->  map[0][16] = backgroundTile;
            case EXTERNAL_BORDER_CORNER_DX_DOWN -> map[12][16] = backgroundTile;
        }
    }
    private void setBorders(BackgroundTile backgroundTile)
    {
        Tile[][] map = LevelEditor.getInstance().getMap();
        setColMap(1, backgroundTile);
        setColMap(15, backgroundTile);
        for(int c =2;c< 15;c++) {
            map[0][c] = backgroundTile;
            map[Row-1][c] = backgroundTile;
        }

    }
    private void setBorderRow(BackgroundTile backgroundTile)
    {
        Tile[][] map = LevelEditor.getInstance().getMap();
        int r = 0;
        switch (backgroundTile.getType())
        {
            case BORDER_UP -> r = 0;
            case BORDER_DOWN -> r =12;
        }
        for(int c =2;c< 15;c++) {
            map[r][c] = backgroundTile;
        }
    }
    private void setBorderCol(BackgroundTile backgroundTile)
    {
        Tile[][] map = LevelEditor.getInstance().getMap();
        int c = 0;
        switch (backgroundTile.getType())
        {
            case BORDER_SX -> c = 1;
            case BORDER_DX -> c =15;
        }
        for(int r =1;r< 12;r++) {
            map[r][c] = backgroundTile;
        }
    }
    private void setExternalBorderCol(BackgroundTile backgroundTile)
    {
        Tile[][] map = LevelEditor.getInstance().getMap();
        int c = 0;
        switch (backgroundTile.getType())
        {
            case EXTERNAL_BORDER_SX -> c = 0;
            case EXTERNAL_BORDER_DX -> c =16;
        }
        for(int r =1;r< 12;r++) {
            map[r][c] = backgroundTile;
        }
    }
    private void setExternalsBorders(BackgroundTile tile)
    {
        setColMap(0, tile);
        setColMap(16, tile);
    }
    private void setColMap(int c, BackgroundTile tile)
    {
        Tile[][] map = LevelEditor.getInstance().getMap();
        for(int r=0;r< Row;r++)
            map[r][c] = tile;
    }
    private void applyBackgroundType(BackgroundTile background) {
        Consumer<BackgroundTile> handler = backgroundOperations.get(background.getType());
        if (handler != null) {
            handler.accept(background);
        }
    }

    //SET BACKGROUND BY TYPE
    public void setBackground(Background backgroundSelected) {
        try{
            for (BackgroundTile backgroundTile : backgroundSelected.getBackgroundTiles()) {
                applyBackgroundType(backgroundTile);
            }
        } catch (Exception e) {
            System.err.println("An error occurred while setting the background " + e.getMessage());
            e.printStackTrace();

        }

    }
}
