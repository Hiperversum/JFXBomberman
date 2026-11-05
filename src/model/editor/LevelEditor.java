package model.editor;

import model.Cursor;
import model.editor.handler.WorldComponentsHandler;
import model.engine.UpdateLoopListener;

import model.loader.resources.Resource;
import model.managers.FileManager;
import model.managers.MapManager;
import model.world.Level;
import model.world.components.Theme;
import model.world.components.entity.objects.obstacles.Obstacles;
import model.world.components.tiles.WallTile;
import model.world.components.entity.Entity;
import model.world.components.tiles.Background;
import model.world.components.WorldComponent;
import model.world.components.entity.objects.Objects;
import model.world.components.tiles.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class LevelEditor extends Editor implements UpdateLoopListener {
    //FIELDS:////////////////////////////////////////////////////////
    private static LevelEditor instance;
    private Collection<Level> userLevels;
    private Resource resourceSelected;
    private Cursor cursor;
    private Level level;
    private Tile[][] map;
    private WorldComponentsHandler componentHandler;
    private WorldComponent worldComponentSelected;
    private Theme[] themes = Theme.values();
    private boolean check;
    //CONSTRUCTOR:///////////////////////////////////////////////////
    private LevelEditor() {
        cursor = new Cursor();
    }

    public static LevelEditor getInstance()
    {
        if(instance == null) instance = new LevelEditor();
        return instance;
    }
    //METHODS:///////////////////////////////////////////////////////
    public void startEditor()
    {
        loadResources();
        componentHandler = new WorldComponentsHandler(level.getMap());
    }
    public void loadResources()
    {
        level = new Level(new Tile[Row][Col],new ArrayList<>());
        try {
            level = Level.load(FileManager.findFileAndDirectory("default.lvl").toString());
            level.setLevelName(null);
            notifyView(UpdateType.UPDATE_NEW_LEVEL);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        map = level.getMap();
    }
    //GETTERS:
    public Cursor getCursor() {
        return cursor;
    }

    public Level getLevel() {
        return level;
    }

    public Tile[][] getMap() {
        return map;
    }
    public List<Entity> getEntityList() {
        return level.getEntityList();
    }
    public Resource getEditorResourceSelected()
    {
        return resourceSelected;
    }
    public Collection<Level> getUserLevels() {
        return userLevels;
    }
    public Resource getResourceSelected() {
        return resourceSelected;
    }
    public Theme[] getThemes() {
        return themes;
    }

    //SETTERS:
    public void setUserLevels() {
        userLevels = MapManager.getInstance().getUserLevels().values();
        notifyView(UpdateType.UPDATE_USER_LEVELS);
    }

    public void setEditorResourceSelected(Resource.Type type)
    {
        resourceSelected = Resource.getResource(type);
        if (type == Resource.Type.BACKGROUND){
            notifyView(UpdateType.UPDATE_BACKGROUND_SELECTED);
        }
        else
        notifyView(UpdateType.UPDATE_CATEGORY_SELECTED);
    }

    public void setBackground(String id) {
        System.out.println(id);
        componentHandler.setBackground((Background) Resource.getResource(Resource.Type.BACKGROUND).getWorldComponentMap().get(id));
    }

    public void setTheme(Theme theme) {
        Tile[][] map = level.getMap();
        for (int i=1; i<Row-1;i++){
            for (int j=2; j<Col-2;j++){
                map[i][j] = (Tile) componentHandler.createFromTheme(map[i][j],theme,i,j);
                if (map[i][j] instanceof WallTile )
                    ((WallTile)map[i][j]).setFloorTile((FloorTile) componentHandler.createFromTheme(((WallTile) map[i][j]).getFloorTile(),theme,i,j));
            }
        }
        setBackground(theme.toString());
        checkMap();
    }
    public void setWorldComponent(String key) {
        if (resourceSelected!=null)
        worldComponentSelected = resourceSelected.getWorldComponentMap().get(key);
    }

    //////////////////////////////////////////////////////////////////////
    //LEVEL EDITOR:
    public void place()
    {
        int y_start = Math.min(cursor.getY(),cursor.getYDropped());
        int y_end = Math.max(cursor.getY(),cursor.getYDropped());
        int x_start = Math.min(cursor.getX(),cursor.getXDropped());
        int x_end = Math.max(cursor.getX(),cursor.getXDropped());
        for (int y = y_start; y <=y_end; y++) {
            for (int x = x_start; x <= x_end; x++) {
                WorldComponent worldComponent = componentHandler.create(worldComponentSelected,y,x);
                if ((y == 1 && x == 2) && !(worldComponent instanceof FloorTile)) continue;
                if (worldComponentSelected instanceof Tile) { 
                	if(worldComponent instanceof FloorTile)((Tile)worldComponent).setObject(map[y][x].getObj());
                	if(map[y][x] instanceof WallTile && worldComponent instanceof FloorTile) ((WallTile)map[y][x]).setFloorTile((FloorTile)worldComponent);
                	else map[y][x] = (Tile)worldComponent; 
                	check = true;
                }
                else if (worldComponentSelected instanceof Objects && map[y][x] instanceof FloorTile && checkEntity(y,x)) map[y][x].setObject((Objects)worldComponent);
                else if (worldComponentSelected instanceof Entity && map[y][x] instanceof FloorTile && map[y][x].getObj()==null && checkEntity(y,x)) level.addEntity((Entity) worldComponent);
            }
        }
        if (check)
        checkMap();
    }
    public void remove() {
        int y_start = Math.min(cursor.getY(),cursor.getYDropped());
        int y_end = Math.max(cursor.getY(),cursor.getYDropped());
        int x_start = Math.min(cursor.getX(),cursor.getXDropped());
        int x_end = Math.max(cursor.getX(),cursor.getXDropped());
        for (int y = y_start; y <=y_end; y++) {
            for (int x = x_start; x <= x_end; x++) {
                if (map[y][x] instanceof WallTile) {
                    map[y][x] = ((WallTile) map[y][x]).getFloorTile();
                    check = true;
                }
                map[y][x].setObject(null);
                getEntityList().remove(removeEntity(y,x));
            }
        }
        if (check)
            checkMap();
    }
    public Entity removeEntity(int y,int x){
        for (Entity entity: getEntityList()) {
            if(entity.getY()/tileSize == y && entity.getX()/tileSize == x)
                return entity;
        }
        return null;
    }
    //CHECKER:
    public boolean containsAnObstacle(){
        for (int y = 1; y <Row-1; y++) {
            for (int x = 2; x < Col - 1; x++) {
                if(getMap()[y][x].hasAnObject() && getMap()[y][x].getObj() instanceof Obstacles) return true;
            }
        }
        return false;
    }
    private boolean checkEntity(int y,int x){
        for (Entity entity: getEntityList()) {
            if(entity.getY()/tileSize == y && entity.getX()/tileSize == x)
                return false;
        }
        return true;
    }
    private void checkMap() {
        try {
            for (int y = 1; y <Row-1; y++) {
                for (int x = 2; x < Col-1; x++) {
                    if(map[y][x]!=null)
                    {
                        if (map[y][x] instanceof FloorTile && !(map[y-1][x] instanceof FloorTile) && ((FloorTile) map[y][x]).getType().equals(FloorTile.Type.FLOOR))
                            ((FloorTile)map[y][x]).changeToFloorShadowVersion();
                        else if (map[y][x] instanceof FloorTile && (map[y-1][x] instanceof FloorTile) && ((FloorTile) map[y][x]).getType().equals(FloorTile.Type.FLOOR_SHADOW))
                            ((FloorTile)map[y][x]).changeToFloorVersion();


                    }
                }
            }
        } catch (NullPointerException e) {e.printStackTrace();}
        check = false;
    }
    //METHODS FOR MANAGING:
    public void deleteLevel() {
        MapManager.getInstance().getUserLevels().remove(levelSelected.getLevelName());
        WorldHandler.getInstance().deleteLevelInWorlds();
        FileManager.removeLevelFile(levelSelected.getLevelName());
        notifyView(UpdateType.UPDATE_LEVEL_DELETED);
    }
    public void saveLevel()
    {
        if (containsAnObstacle()) {
            Level levelToSave = level.cloneViaSerialization();
            MapManager.getInstance().getUserLevels().put(levelToSave.getLevelName(), levelToSave);
            levelToSave.save();
            notifyView(UpdateType.UPDATE_LEVEL_SAVED);
        }
        else notifyView(UpdateType.UPDATE_LEVEL_OBSTACLE_NOT_FOUND);
    }
    public void loadLevelSelected() {
        if (levelSelected!=null) {
            level = getUserLevels().stream()
                    .filter(level -> level == levelSelected)
                    .findFirst().orElse(null).cloneViaSerialization();
            map = level.getMap();
            notifyView(UpdateType.UPDATE_LEVEL_LOADED);
        }
    }
    ///CALLED BY ENGINE
    @Override
    public void updateByGameLoop(long currentTime) {
        if(level!=null) {
            setChanged();
            notifyObservers();
        }

    }



}
