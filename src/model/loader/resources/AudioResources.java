package model.loader.resources;
import model.managers.AudioManager;
import model.managers.FileManager;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;

public class AudioResources extends Resource {

    public AudioResources(){
        loadResources();
    }

    @Override
    public void loadResources() {
        loadSound();
        loadMusic();
    }

    private void loadMusic() {
        try {
            AudioManager.getInstance().setMusicList(
                    Files.walk(FileManager.findFileAndDirectory("music"),1)
                            .filter(Files::isRegularFile)
                            .map(Path::toString)
                            .filter(musicPath->musicPath.endsWith(".wav"))
                            .collect(Collectors.toList())
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    private void loadSound() {
        try {
            AudioManager.getInstance().setSoundMap(
                    Files.walk(FileManager.findFileAndDirectory("sound"),1)
                            .filter(Files::isRegularFile)
                            .collect(Collectors.toMap(
                                    soundPath-> AudioManager.SoundType.fromString(soundPath.getFileName().toString().replace(".wav","")),
                                    soundPath-> soundPath.toString()
                            ))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
