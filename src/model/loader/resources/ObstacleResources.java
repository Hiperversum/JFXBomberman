package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;
import model.world.components.Theme;
import model.world.components.entity.character.behavior.Behavior;
import model.world.components.entity.objects.obstacles.Obstacles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ObstacleResources extends Resource {

    private Map<Theme, Map<Behavior, Image[]>> obstacleAliveFramesMap = new HashMap<>();
    private Map<Theme, Map<Behavior, Image[]>> obstacleDeathFramesMap = new HashMap<>();
    public ObstacleResources() {
        super(Type.OBSTACLES);
        worldComponentMap = new HashMap<>();
        icoMap = new HashMap<>();
        loadResources();
        checkResources();
    }

    @Override
    public void loadResources(){
        try (Stream<Path> stream = Files.walk(FileManager.findFileAndDirectory("tiles"), 1)) {
            Path[] subFolders = stream
                    .filter(Files::isDirectory)
                    .toArray(Path[]::new);

            for (Path subfolder : subFolders) {
                String[] imagePaths = Files.walk(subfolder)
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(str -> str.contains("obstacle"))
                        .filter(str -> str.endsWith(".png"))
                        .toArray(String[]::new);

                if (!subfolder.getFileName().toString().equals("tiles")) {
                    Theme theme = Theme.fromString(subfolder.getFileName().toString());
                    String[] arrayPNGs = FileManager.findPNGFromPath(subfolder);
                    loadAliveFrames(theme,arrayPNGs);
                    loadDeathFrames(theme,arrayPNGs);
                    icoMap.put(theme.toString(), LoaderUtilityTools.createImage(Arrays.stream(imagePaths).filter(str -> str.contains("image.png")).findFirst().orElse(null)));
                    worldComponentMap.put(theme.toString(), new Obstacles(theme));
                }
            }
        } catch(IOException e) {throw new RuntimeException(e);}
    }

    //UTILITY METHODS:////////////////////////////////////////////////////////////////////////////////////////////////////
    //ALIVE FRAMES:
    private void loadAliveFrames(Theme theme,String[] arrayPNGs) throws IOException {
        Map<Behavior, Image[]> aliveMap = new HashMap<>();
        aliveMap.put(Behavior.ALIVE, Arrays.stream(arrayPNGs).filter( i-> i.contains("image")).map(LoaderUtilityTools::createImage).toArray(Image[]::new));
        obstacleAliveFramesMap.put(theme,aliveMap);
    }
    public Image[] getAliveFrames(Theme theme){
        return obstacleAliveFramesMap.get(theme).get(Behavior.ALIVE);
    }
    //DEATH FRAMES:
    public Image[] getDeathFrames(Theme theme){
        return obstacleDeathFramesMap.get(theme).get(Behavior.DEATH);
    }
    private void loadDeathFrames(Theme theme,String[] arrayPNGs) throws IOException {
        Map<Behavior, Image[]> deathMap = new HashMap<>();
        deathMap.put(Behavior.DEATH, Arrays.stream(arrayPNGs).filter( i-> i.contains("destruction")).map(LoaderUtilityTools::createImage).toArray(Image[]::new));
        obstacleDeathFramesMap.put(theme,deathMap);
    }


}
