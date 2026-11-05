package model.loader.resources;

import javafx.application.Platform;
import model.managers.FileManager;
import model.managers.MapManager;
import model.managers.UserManager;
import model.user.User;
import model.world.Level;
import model.world.World;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;


public class MapResources extends Resource {
    private final String normalGameDirectory = "game";
    public MapResources() throws IOException {
        loadResources();
    }

    @Override
    public void loadResources() throws IOException {
        //LOAD NORMAL GAME:
        loadNormalGame();
        //LOAD USERS LEVELS AND WORLDS:
        User user = UserManager.getInstance().getUserLoaded();
        if (user != null)
        loadUserMap(user);
    }
    public static void loadUserMap(User user){
        MapManager mapManager = MapManager.getInstance();
        Platform.runLater(()->{
            if (!mapManager.getLevels().containsKey(user))
                mapManager.addLevels(user, loadUserLevels(user));
            if (!mapManager.getWorlds().containsKey(user))
                mapManager.addWorlds(user, loadUserWorlds(user));
        });


    }
    private static Map<String, Level> loadUserLevels(User user){
        System.out.println("LOADING LEVELS: ");
        Map<String,Level> levelMap = new TreeMap<>();
        try {
                levelMap.putAll(Files.walk(Path.of(user.getUserPath()+"myLevels"))
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .map(levelPath-> {
                            try { return Level.load(levelPath); }
                            catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e);}

                        })
                        .collect(Collectors.toMap(
                                level->level.getLevelName(),
                                Function.identity(),
                                (existing, replacement) -> existing

                        )));
        } catch (IOException e) {
            throw new RuntimeException("Error while loading levels: ", e);
        }
        levelMap.forEach((k,v)->System.out.println(k+" "+v));
        return levelMap;
    }
    private static Map<String, World> loadUserWorlds(User user){
        System.out.println("LOADING WORLD: ");
        Map<String,World> worldMap = new TreeMap<>();
        try {
                worldMap.putAll(Files.walk(Path.of(user.getUserPath()+"myWorlds"))
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .map(worldPath-> {
                            try { return World.load(worldPath); }
                            catch (IOException | ClassNotFoundException e) { throw new RuntimeException(e);}

                        })
                        .collect(Collectors.toMap(
                                world->world.getWorldName(),
                                Function.identity(),
                                (existing, replacement) -> existing
                        )));

        } catch (IOException e) {
            throw new RuntimeException("Error while loading worlds: ", e);
        }
        return worldMap;
    }
    public  void loadNormalGame() {
        MapManager mapManager = MapManager.getInstance();
        try {
            String worldFileName = "game.wrl";
            String directory = FileManager.findFileAndDirectory(normalGameDirectory).toString();
            mapManager.setNormalGame(World.load(FileManager.findFileInADirectory(worldFileName,directory).toString()));
            mapManager.setNormalGameLevels(
                    Files.walk(Paths.get(directory))
                            .map(Path::toString)
                            .filter(path -> path.endsWith(".lvl"))
                            .map(path-> {try {
                                return Level.load(path);}
                            catch (ClassNotFoundException | IOException e) {throw new RuntimeException(e);}
                            })
                            .collect(Collectors.toMap(
                                    level->level.getLevelName(),
                                    Function.identity()
                            ))
            );

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


}












