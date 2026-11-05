package model.editor;

import model.Model;
import model.managers.MapManager;
import model.world.Level;


public abstract class Editor extends Model {
    public enum UpdateType{
        UPDATE_USER_WORLDS, UPDATE_LEVELS_OF_WORLDS, UPDATE_USER_LEVELS, UPDATE_WORLD_DELETED,
        UPDATE_LEVEL_DELETED, UPDATE_ADD_LEVEL_TO_WORLD, UPDATE_LEVEL_LOADED,UPDATE_WORLD_SAVED, 
        UPDATE_LEVEL_OBSTACLE_NOT_FOUND, UPDATE_LEVEL_SAVED, UPDATE_CATEGORY_SELECTED, UPDATE_THEME_SELECTED, 
        UPDATE_BACKGROUND_SELECTED, UPDATE_LEVEL_SELECTED, UPDATE_WORLD_SELECTED, UPDATE_NEW_LEVEL, UPDATE_NEW_WORLD, 
    }
    protected Level levelSelected;

    public void setLevelSelected(String levelSelected) {
        this.levelSelected = MapManager.getInstance().getUserLevels().get(levelSelected);
        notifyView(UpdateType.UPDATE_LEVEL_SELECTED);
    }
    public Level getLevelSelected() {
        return levelSelected;
    }

    //UPDATE VIEW:
    public void notifyView(UpdateType updateType){
        setChanged();
        notifyObservers(updateType);
    }
}
