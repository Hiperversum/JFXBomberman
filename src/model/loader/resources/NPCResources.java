package model.loader.resources;

import javafx.scene.image.Image;
import model.utility.LoaderUtilityTools;
import model.managers.FileManager;
import model.world.components.entity.character.behavior.Behavior;
import model.world.components.entity.character.npc.Bomber;
import model.world.components.entity.character.npc.NPC;
import model.world.components.entity.character.npc.Walker;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class NPCResources extends Resource {
    private Map<String, Map<Behavior, Image[]>> entityWalkFramesMap = new HashMap<>();
    private Map<String, Map<Behavior, Image[]>> entityDeathFramesMap = new HashMap<>();
    private Map<String, Map<Behavior, Image[]>> entityAttackFramesMap = new HashMap<>();

    public NPCResources() {
        super(Type.NPC);
        worldComponentMap = new HashMap<>();
        icoMap = new HashMap<>();
        loadResources();
        checkResources();
    }
    @Override
    public void loadResources() {
        try (Stream<Path> stream = Files.walk(FileManager.findFileAndDirectory(type.name().toLowerCase()), 1)) {
            Path[] subfolders = stream
                    .filter(Files::isDirectory)
                    .toArray(Path[]::new);
            for (Path subfolder : subfolders) {
                String imagePath = Files.walk(subfolder)
                        .filter(Files::isRegularFile)
                        .map(Path::toString)
                        .filter(str -> str.endsWith("down01.png"))
                        .findFirst().orElse(null);
                if(!subfolder.getFileName().toString().equals(type.name().toLowerCase())) {
                    String[] arrayPNGs = FileManager.findPNGFromPath(subfolder);
                    loadWalkFrames(subfolder, arrayPNGs);
                    loadDeathFrames(subfolder, arrayPNGs);
                    loadAttackFrames(subfolder,arrayPNGs);

                    String id = subfolder.toString();
                    icoMap.put(id, LoaderUtilityTools.createImage(imagePath));
                    worldComponentMap.put(id, generateNPCModel(id));
                }
            }
        } catch (IOException e){ throw new RuntimeException();}


    }
    private NPC generateNPCModel(String npc){
        switch (Path.of(npc).getFileName().toString()) {
            case "walker": return new Walker();
            case "bomber": return  new Bomber();
        }
        return null;
    }


    //UTILITY METHODS:////////////////////////////////////////////////////////////////////////////////////////////////////

    //DEATH FRAMES:
    public Image[] getDeathFrames(String entity, Behavior direction){
        return entityDeathFramesMap.get(entity).get(direction);
    }
    private void loadDeathFrames(Path path,String[] arrayPNGs) throws IOException {
        Map<Behavior, Image[]> deathMap = new HashMap<>();

        deathMap.put(Behavior.UP, createDeathFrames( Behavior.UP,arrayPNGs));
        deathMap.put(Behavior.LEFT, createDeathFrames( Behavior.LEFT,arrayPNGs));
        deathMap.put(Behavior.RIGHT, createDeathFrames( Behavior.RIGHT,arrayPNGs));
        deathMap.put(Behavior.DOWN, createDeathFrames( Behavior.DOWN,arrayPNGs));
        entityDeathFramesMap.put(path.getFileName().toString(),deathMap);
    }

    private Image[] createDeathFrames(Behavior direction, String[] arrayPNGs){
        Image[] frames = new Image[2];
        frames[0] = Arrays.stream(arrayPNGs).filter(path->path.contains("death") && path.contains(direction.name().toLowerCase())).map(LoaderUtilityTools::createImage)
                .findFirst().orElse(null);
        frames[1] = Arrays.stream(arrayPNGs).filter(path->!path.contains("death") && path.contains(direction.name().toLowerCase())).map(LoaderUtilityTools::createImage)
                .findFirst().orElse(null);
        return frames;
    }

    //WALK FRAMES:
    public Image[] getWalkFrames(String entity, Behavior direction){
        return entityWalkFramesMap.get(entity).get(direction);
    }
    private void loadWalkFrames(Path path,String[] arrayPNGs) throws IOException {
        Map<Behavior, Image[]> walkMap = new HashMap<>();
        walkMap.put(Behavior.UP, Arrays.stream(arrayPNGs).filter(p -> p.contains(Behavior.UP.name().toLowerCase()) && !p.contains("death")).map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
        walkMap.put(Behavior.LEFT,Arrays.stream(arrayPNGs).filter(p -> p.contains(Behavior.LEFT.name().toLowerCase()) && !p.contains("death")).map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
        walkMap.put(Behavior.RIGHT,Arrays.stream(arrayPNGs).filter(p -> p.contains(Behavior.RIGHT.name().toLowerCase()) && !p.contains("death")).map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
        walkMap.put(Behavior.DOWN,Arrays.stream(arrayPNGs).filter(p -> p.contains(Behavior.DOWN.name().toLowerCase()) && !p.contains("death")).map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
        entityWalkFramesMap.put(path.getFileName().toString(),walkMap);
    }

    //ATTACK FRAMES:

    public Image[] getAttackFrames(String entity) {
        return entityAttackFramesMap.get(entity).get(Behavior.ATTACK);
    }

    private void loadAttackFrames(Path path, String[] arrayPNGs) throws IOException {
        Map<Behavior, Image[]> attackMap = new HashMap<>();
        attackMap.put(Behavior.ATTACK, Arrays.stream(arrayPNGs).filter(p -> p.contains(Behavior.ATTACK.name().toLowerCase())).map(LoaderUtilityTools::createImage)
                .toArray(Image[]::new));
        entityAttackFramesMap.put(path.getFileName().toString(),attackMap);
    }
}
