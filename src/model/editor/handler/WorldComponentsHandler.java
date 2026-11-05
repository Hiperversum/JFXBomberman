package model.editor.handler;
import model.Model;
import model.editor.factory.EntityFactory;
import model.editor.factory.TileFactory;
import model.loader.resources.Resource;
import model.world.components.Theme;
import model.world.components.entity.Entity;
import model.world.components.entity.objects.obstacles.Obstacles;
import model.world.components.tiles.*;
import model.world.components.WorldComponent;

import model.world.components.entity.objects.Objects;

public class WorldComponentsHandler extends Model {
    private TileFactory tileFactory;
    private BackgroundHandler backgroundHandler;
    private EntityFactory entityFactory;

    protected Tile[][] map;
    public WorldComponentsHandler(Tile[][] map) {
        super();
        this.map = map;
        tileFactory = new TileFactory();
        backgroundHandler = new BackgroundHandler();
        entityFactory = new EntityFactory();
    }

    public void setBackground(Background backgroundSelected) {
        backgroundHandler.setBackground(backgroundSelected);
    }
    public WorldComponent create(WorldComponent worldComponentSelected,int y,int x) {
        if (worldComponentSelected instanceof Tile) return tileFactory.createTile(worldComponentSelected,y,x);
        else if (worldComponentSelected instanceof Entity) return entityFactory.createEntity(worldComponentSelected,y,x);
        return null;
    }
    public WorldComponent createFromTheme(WorldComponent worldComponent, Theme theme,int y,int x){
        WorldComponent worldComponentReturned = null;
        if(worldComponent instanceof  Tile) {
            if (worldComponent instanceof FloorTile)
                worldComponentReturned = Resource.getResource(Resource.Type.FLOOR).getWorldComponentMap().get(theme.toString());
            else if (worldComponent instanceof WallTile)
                worldComponentReturned = Resource.getResource(Resource.Type.WALL).getWorldComponentMap().get(theme.toString());
            worldComponentReturned  = tileFactory.createTile(worldComponentReturned,y,x);
            if (((Tile)worldComponent).hasAnObject()) {
                Objects objects = ((Tile) worldComponent).getObj();
                if (objects instanceof Obstacles)
                    objects = (Objects) entityFactory.createEntity(Resource.getResource(Resource.Type.OBSTACLES).getWorldComponentMap().get(theme.toString()),y,x);
                ((Tile) worldComponentReturned).setObject(objects);
            }
        }
        return worldComponentReturned;
    }
}
