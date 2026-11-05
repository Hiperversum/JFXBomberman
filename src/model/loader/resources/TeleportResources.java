package model.loader.resources;
import javafx.scene.image.Image;
import model.managers.FileManager;
import model.utility.LoaderUtilityTools;
import java.io.IOException;
import java.nio.file.Files;



public class TeleportResources extends Resource {
    private Image[] frames;
    public TeleportResources() throws IOException {
        super(Type.TELEPORT);
        loadResources();
    }
    @Override
    public void loadResources() throws IOException {

        frames = Files.walk(FileManager.findFileAndDirectory(type.toString()))
                .filter(Files::isRegularFile)
                .map(LoaderUtilityTools::createImageFromPath)
                .toArray(Image[]::new);


    }
    public Image[] getFrames() {
        return frames;
    }
}
