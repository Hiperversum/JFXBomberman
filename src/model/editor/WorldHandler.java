package model.editor;


import model.managers.FileManager;
import model.managers.MapManager;
import model.world.Level;
import model.world.World;

import java.util.*;


public class WorldHandler extends Editor {
    private static WorldHandler instance;
    private Collection<World> userWorlds;
    private World world;
    private List<Level> levelPack;
    private WorldHandler(){
    }

    public static WorldHandler getInstance() {
        if (instance == null) instance = new WorldHandler();
        return instance;
    }

    //METHODS:
    public void setWorld(World world) {
        this.world = world;
        setLevelPack();
        notifyView(UpdateType.UPDATE_LEVELS_OF_WORLDS);
        notifyView(UpdateType.UPDATE_WORLD_SELECTED);
    }
    public void setWorld(String worldName){
        world = MapManager.getInstance().getUserWorlds().get(worldName).cloneViaSerialization();
        notifyView(UpdateType.UPDATE_LEVELS_OF_WORLDS);
        notifyView(UpdateType.UPDATE_WORLD_SELECTED);
    }
    public void setLevelPack() {
        levelPack = MapManager.getInstance().levelPackConverter(world);
    }
    public void setUserWorlds(){
        userWorlds = MapManager.getInstance().getUserWorlds().values();
        notifyView(UpdateType.UPDATE_USER_WORLDS);
    }

    //GETTERS:

    public World getWorld() {return world;}
    
    public List<Level> getLevelPack() {
        setLevelPack();
        return levelPack;
    }
    public Collection<World> getUserWorlds() {
        return userWorlds;
    }
    public LevelEditor getLevelEditor() {
        return LevelEditor.getInstance();
    }

    //METHODS FOR MANAGING:
    public void removeLevel(){
        if (levelSelected!=null) {
            world.removeLevel(levelSelected.getLevelName());
            notifyView(UpdateType.UPDATE_LEVELS_OF_WORLDS);
        }
    }
    public void addLevel(){
        if (levelSelected!=null) {
            world.addLevel(levelSelected.getLevelName());
            notifyView(UpdateType.UPDATE_ADD_LEVEL_TO_WORLD);
        }
    }
    public void createNewWorld(){
        world = new World();
        notifyView(UpdateType.UPDATE_NEW_WORLD);
        notifyView(UpdateType.UPDATE_LEVELS_OF_WORLDS);
    }
    public void saveWorld() {
        World worldToSave = world.cloneViaSerialization();
        MapManager.getInstance().getUserWorlds().put(worldToSave.getWorldName(), worldToSave);
        worldToSave.save();
        notifyView(UpdateType.UPDATE_WORLD_SAVED);
    }
    public void deleteWorld(){
        String worldName = world.getWorldName();
        MapManager.getInstance().getUserWorlds().remove(worldName);
        FileManager.removeWorldFile(worldName);
        notifyView(UpdateType.UPDATE_WORLD_DELETED);

    }
    public void deleteLevelInWorlds(){
        List<World> userWorlds = new ArrayList<>(MapManager.getInstance().getUserWorlds().values());
        //REMOVE IN USER WORLDS:
        for (World world : userWorlds) {
            LinkedList<String> levelPack = world.getLevelPack();
            Iterator<String> iterator = levelPack.iterator();
            while (iterator.hasNext()) {
                String levelName = iterator.next();
                if (levelName.equals(levelSelected.getLevelName())) {
                    iterator.remove();
                }
            }
            world.save();

        }
        //REMOVE IN WORLD SELECTED:
        if (world!= null) {
        LinkedList<String> levelPack = world.getLevelPack();
        Iterator<String> iterator = levelPack.iterator();
	        while (iterator.hasNext()) {
	            String levelName = iterator.next();
	            if (levelName.equals(levelSelected.getLevelName())) {
	                iterator.remove();
	            }
	        }
        }
        //
        if (world!= null && world.getWorldName()!=null) {
            world = MapManager.getInstance().getUserWorlds().get(world.getWorldName());
            LevelEditor.getInstance().setUserLevels();
        }
        notifyView(UpdateType.UPDATE_LEVEL_DELETED);
    }



}
