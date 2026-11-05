package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;
import model.world.components.entity.character.behavior.Behavior;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


public class PlayerResources extends Resource {
    private Map<Integer, String[]> playerResources;
    public PlayerResources() throws IOException {
        super(Type.PLAYER);
        playerResources = new HashMap<>();
        loadResources();
        checkResources();
    }

    @Override
    public void loadResources() throws IOException {
        playerResources = Arrays.stream(Files.walk(FileManager.findFileAndDirectory(type.toString()), 1)
                        .filter(Files::isDirectory)
                        .toArray(Path[]::new))
                .filter(subfolder -> !subfolder.getFileName().toString().equals(type.toString()))
                .collect(Collectors.toMap(
                                            subfolder -> Integer.parseInt(subfolder.getFileName().toString()),
                                            subfolder -> {
                                                try {
                                                    return Files.walk(subfolder)
                                                            .filter(Files::isRegularFile)
                                                            .map(Path::toString)
                                                            .filter(str -> str.endsWith(".png"))
                                                            .toArray(String[]::new);
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    return null;
                                                }
                                            }
                                         ));
        //TEST:
        //playerResources.forEach((k,v)->System.out.println(k + ": " + Arrays.toString(v)));
    }

    public  Map<Behavior, Image[]>  setPlayerResourceSelected(Integer key) {
        Map<Behavior, Image[]> playerResourceSelected = new HashMap<>();

        String[] imagesPath = playerResources.get(key);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.DOWN);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.LEFT);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.RIGHT);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.UP);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.DEATH);
        insertToPlayerResourceSelected(playerResourceSelected,imagesPath, Behavior.NEXT);

        return playerResourceSelected;
    }
    private void insertToPlayerResourceSelected(Map<Behavior, Image[]> playerResourceSelected, String[] imagesPath, Behavior behavior)
    {
        playerResourceSelected.put(behavior,
        Arrays.stream(imagesPath).filter(path -> path.contains(behavior.name().toLowerCase()))
                .map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
    }


}









