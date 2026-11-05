package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import model.world.components.entity.effects.Explosion;

public class ExplosionResources extends Resource {
    private Map<Explosion.Type,Image[]>  explosionResources;
    public ExplosionResources() throws IOException {
        super(Type.EXPLOSION);
        explosionResources = new HashMap<>();
        loadResources();
        checkResources();
    }

    @Override
    public void loadResources() throws IOException {
        explosionResources = Arrays.stream(Files.walk(FileManager.findFileAndDirectory(type.toString()), 1)
                        .filter(Files::isDirectory)
                        .toArray(Path[]::new))
                .filter(subfolder -> !subfolder.getFileName().toString().equals(type.toString()))
                .collect(Collectors.toMap(
                        subfolder -> Explosion.typeFromString(subfolder.getFileName().toString()),
                        subfolder -> {
                            try {
                                return Files.walk(subfolder)
                                        .filter(Files::isRegularFile)
                                        .map(Path::toString)
                                        .filter(str -> str.endsWith(".png"))
                                        .map(LoaderUtilityTools::createImage)
                                        .toArray(Image[]::new);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return null;
                            }
                        }
                ));
    }


    public Image[] explosionResource(Explosion.Type key){
        return explosionResources.get(key);
    }
}
