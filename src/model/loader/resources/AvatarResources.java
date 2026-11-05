package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;


import java.io.IOException;
import java.nio.file.Files;
import java.util.*;


public class AvatarResources extends Resource {
    private Map<Integer, Image> avatarResources;
    public AvatarResources() throws IOException {
        super(Type.AVATARS);
        avatarResources = new HashMap<>();
        loadResources();
        checkResources();
    }

    public Map<Integer, Image> getAvatarResources() {
        return avatarResources;
    }

    public Image getImage(Integer key){
        return avatarResources.get(key);
    }

    @Override
    public void loadResources() throws IOException {
        avatarResources = new HashMap<>(); // Inizializza la mappa delle risorse

        try {
            Files.walk(FileManager.findFileAndDirectory(type.toString()))
                    .filter(Files::isRegularFile)
                    .forEach(imagePath -> {
                        try {
                            int key = Integer.parseInt(imagePath.getFileName().toString().replace(".png", ""));
                            Image image = LoaderUtilityTools.createImageFromPath(imagePath);
                            avatarResources.put(key, image);
                        } catch (NumberFormatException e) {
                        }
                    });
        } catch (IOException e) {
            System.err.println("Error during resource search" + e.getMessage());
        }
    }
}
