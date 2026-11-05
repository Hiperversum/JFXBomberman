package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;
import model.world.components.entity.objects.powerup.PowerUp;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class PowerUpResources extends Resource {
    private Map<PowerUp.Type, Image[]> PowerUpResources;
    public PowerUpResources() throws IOException {
        super(Type.POWER_UP);
        PowerUpResources = new HashMap<>();
        loadResources();
        checkResources();
    }

    public Image[] getPowerUpResources(PowerUp.Type type) {
        return PowerUpResources.get(type);
    }

    @Override
    public void loadResources() throws IOException {
        Path root = FileManager.findFileAndDirectory("powerup");
        PowerUpResources = PowerUp.Type.stream() .collect(Collectors.toMap(
                powerUpType -> powerUpType,
                powerUpType -> {
                    try {
                        return Arrays.stream(FileManager.findPNGFromNameInALocation(powerUpType.toString().toLowerCase(), root))
                                .map(LoaderUtilityTools::createImage)
                                .toArray(Image[]::new);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        ));
        //PowerUpResources.forEach((k,v)->System.out.println(k+" "+Arrays.toString(v)));
    }
}
