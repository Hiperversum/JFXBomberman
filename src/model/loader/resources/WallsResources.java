package model.loader.resources;

import model.utility.LoaderUtilityTools;
import model.managers.FileManager;
import model.world.components.Theme;
import model.world.components.tiles.WallTile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

public class WallsResources extends Resource {
    public WallsResources() {
        super(Type.WALL);
        worldComponentMap = new HashMap<>();
        icoMap = new HashMap<>();
        loadResources();
        checkResources();
    }


    @Override
    public void loadResources() {
        try (Stream<Path> stream = Files.walk(FileManager.findFileAndDirectory("tiles"), 1)) {
            Path[] subfolders = stream
                    .filter(Files::isDirectory)
                    .toArray(Path[]::new);

            for (Path subfolder : subfolders) {
                String imagePath = Files.walk(subfolder)
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(str -> str.endsWith("wall.png"))
                        .findFirst().orElse(null);

                if (!subfolder.getFileName().toString().equals("tiles")) {
                    Theme theme = Theme.fromString(subfolder.getFileName().toString());
                    icoMap.put(theme.toString(), LoaderUtilityTools.createImage(imagePath));
                    worldComponentMap.put(theme.toString(), new WallTile(imagePath));
                }
            }
        } catch(IOException e) {throw new RuntimeException(e);}
    }
}
