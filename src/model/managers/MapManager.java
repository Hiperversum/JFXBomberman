package model.managers;
import model.Model;
import model.UI;
import model.engine.Animated;
import model.engine.UpdateLoopListener;
import model.events.StartLevelEvent;
import model.events.VictoryEvent;
import model.user.User;
import model.utility.RandomUtils;
import model.world.Level;
import model.world.World;
import model.world.components.entity.character.Player;
import model.world.components.entity.objects.Objects;
import model.world.components.entity.objects.Teleport;
import model.world.components.tiles.Tile;

import java.util.*;
import java.util.stream.Collectors;


public class MapManager extends Model implements UpdateLoopListener {

    public enum UpdateType{
        RESTART_WORLD;
    }
    private enum GameMode{
        NORMAL,CUSTOM;
    }
    //CAMPI:
    private static MapManager instance;
    private Map<User,Map<String, Level>> levels;
    private Map<User,Map<String, World>> worlds;
    private  Map<String, Level> normalGameLevels;
    private Level loadedLevel;
    private World normalGame,loadedWorld,worldToRestart;
    private GameMode gameMode;
    private Tile[][] map;
    //COSTRUTTORE:
    private MapManager() {
        levels = new TreeMap<>();
        worlds = new TreeMap<>();
    }

    public static MapManager getInstance() {
        if (instance == null) instance = new MapManager();
        return instance;
    }
    //METHODS://///////////////////////////////////////////////////

    // GAME MODE:
    private void reset(){
        Player.getInstance().resetSkills();
        UI.getInstance().resetUI();
        StartLevelEvent.resetStageCounter();
    }
    public void setNormalGameMode() {
        reset();
        gameMode = GameMode.NORMAL;
        loadedWorld = normalGame.cloneViaSerialization();
        worldToRestart = loadedWorld.cloneViaSerialization();
        nextLevel();
    }
    public void setCustomWorldMode(String customWorld){
        reset();
        gameMode = GameMode.CUSTOM;
        loadedWorld = getUserWorlds().get(customWorld).cloneViaSerialization();
        worldToRestart = loadedWorld.cloneViaSerialization();
        nextLevel();
    }

    //LEVEL METHODS:
    public void nextLevel() {
        if (loadedWorld.getLevelPack().isEmpty()) {
            EventManager.getInstance().createEvent(new VictoryEvent());
            return;
        }
        String levelName = loadedWorld.getLevelPack().get(0);
        loadedWorld.getLevelPack().remove(0);
        if ((gameMode == GameMode.NORMAL && normalGameLevels.containsKey(levelName)) || (gameMode == GameMode.CUSTOM && getUserLevels().containsKey(levelName))) {
            loadedLevel = (gameMode == GameMode.NORMAL) ? normalGameLevels.get(levelName).cloneViaSerialization() : getUserLevels().get(levelName).cloneViaSerialization();
            loadLevel();
        } else nextLevel();
    }
    public void loadLevel(){
        map = loadedLevel.getMap();
        setChanged();
        notifyObservers(map);
        EntityManager.getInstance().setEntityList(loadedLevel.getEntityList());
        EventManager.getInstance().createEvent(new StartLevelEvent().increaseStageCounter());
        RandomUtils.generateTeleport();
        recordAnimated();
        UI.getInstance().resetPlayTime();

    }
    private void recordAnimated(){
    	//RECORD OBJECTS
        AnimationManager aM = AnimationManager.getInstance();
        for(int i = 0; i<Row;i++){
            for(int j=0; j<Col;j++){
                if(map[i][j].hasAnObject() && map[i][j].getObj() instanceof Animated){
                    aM.addAnimated((Animated) map[i][j].getObj());
                }
            }
        }
        //RECORD ENTITIES
        loadedLevel.getEntityList().stream().filter(entity-> entity instanceof Animated)
        .forEach(animated->aM.addAnimated((Animated)animated));
        
        //RECORD PLAYER
        aM.addAnimated(Player.getInstance());
        
    }
    public void addObjToMap(int y, int x, Objects obj){
        if(!map[y][x].hasAnObject()) map[y][x].setObject(obj);
    }
    public void removeObjFromMap(int y, int x ){if(map[y][x].hasAnObject()) map[y][x].setObject(null);}

    //WORLD METHODS:
    public void restartWorld(){
        notifyView(UpdateType.RESTART_WORLD);
        AudioManager.getInstance().stopMusic();
        Player.getInstance().resetSkills();
        UI.getInstance().resetUI();
        loadedWorld = worldToRestart.cloneViaSerialization();
        StartLevelEvent.resetStageCounter();
        nextLevel();
    }
    //CHECKER:
    public boolean checkPlayerOnTeleportation(Player player){
        int y = ((int)player.getEntityBox().getEntityRectangleCenter().getY())/tileSize;
        int x = ((int)player.getEntityBox().getEntityRectangleCenter().getX())/tileSize;
        if(map!= null && map[y][x].getObj()!= null && map[y][x].getObj() instanceof Teleport)
            return true;
        return false;
    }
    //GETTERS:
    public boolean isNormalMode() {
        return gameMode == GameMode.NORMAL;
    }
    public Map<User, Map<String, Level>> getLevels() {
        return levels;
    }
    public Map<User, Map<String, World>> getWorlds() {
        return worlds;
    }
    public Map<String, Level> getUserLevels() {
        User user = UserManager.getInstance().getUserLoaded();
        Map<String, Level> userLevels = levels.get(user);
        if (userLevels == null) addLevels(user,new TreeMap<>());
        return levels.get(user);
    }
    public Map<String, World> getUserWorlds() {
        User user = UserManager.getInstance().getUserLoaded();
        Map<String, World> userWorlds = worlds.get(user);
        if (userWorlds == null) addWorlds(user,new TreeMap<>());
        return worlds.get(user);
    }

    public boolean isLevelInLevels(String levelName){
        return getUserLevels().containsKey(levelName);
    }
    public List<Level> levelPackConverter(World world){
        if (world==null) return null;
        List<Level> levelPack = world.getLevelPack().stream().filter(levelName->isLevelInLevels(levelName))
                .map(levelName->getUserLevels().get(levelName)).collect(Collectors.toList());
        if (levelPack == null) return new LinkedList<>();
        return levelPack;
    }
    public Level getLoadedMap() {
        return loadedLevel;
    }

    public Tile[][] getMap() {
        return map;
    }

    //SETTERS:
    public void setNormalGame(World normalGame) {this.normalGame = normalGame;}
    public void setNormalGameLevels(Map<String, Level> normalGameLevels) {this.normalGameLevels = normalGameLevels;}
    public void addLevels(User user,Map<String, Level> userLevels){levels.put(user,userLevels);}
    public void addWorlds(User user, Map<String, World> userWorlds) {worlds.put(user,userWorlds);}



    //////////
    public void notifyView(MapManager.UpdateType updateType){
        setChanged();
        notifyObservers(updateType);
    }

    @Override
    public void updateByGameLoop(long currentTime) {
        setChanged();
        notifyObservers(map);

    }



}

