package model.loader.resources;
import model.managers.FileManager;
import model.world.components.Theme;
import model.world.components.tiles.Background;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class BackgroundResources extends Resource {
    public BackgroundResources() {
        super(Type.BACKGROUND);
        worldComponentMap = new HashMap<>();
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
                        .filter(str -> str.contains("background"))
                        .filter(str -> str.endsWith(".png"))
                        .toArray(String[]::new);

                if (!subfolder.getFileName().toString().equals("tiles")) {
                    Theme theme = Theme.fromString(subfolder.getFileName().toString());
                    worldComponentMap.put(theme.toString() ,new Background(imagePaths));
                }
            }
        } catch(IOException e) {throw new RuntimeException(e);}
    }
}
