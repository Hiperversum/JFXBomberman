package model.world;

import model.managers.FileManager;
import model.managers.MapManager;
import model.managers.UserManager;

import java.io.*;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.ListIterator;

public class World implements Serializable {
    private String worldName;
    private static final long serialVersionUID = 1L;
    private LinkedList<String> levelPack;
    //METHODS: ////////////////////////////////////////////////////////////////////////////////////////////////////////
    public World() {
        this.levelPack = new LinkedList<>();
    }
    //GETTERS:
    public LinkedList<String> getLevelPack() {
        return levelPack;
    }
    public String getWorldName() {
        return worldName;
    }
    //SETTERS:
    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }


    //SERIALIZABLE METHODS:
    public void save() {
        String parent =
                FileManager.userRoot + File.separator+
                        UserManager.getInstance().getUserSelected() + File.separator+ "myWorlds";
        String fileName = worldName+".wrl";
        //Check if a file with this name already exists
        File[] filesInDirectory = new File(parent).listFiles();
        if (filesInDirectory != null) {
            Arrays.stream(filesInDirectory)
                    .filter(existingFile -> existingFile.getName().equalsIgnoreCase(fileName))
                    .forEach(existingFile -> {
                        String existingFileName = existingFile.getName().replace(".wrl", "");
                        MapManager mapManager = MapManager.getInstance();
                        mapManager.getUserWorlds().remove(existingFileName);
                        mapManager.getUserWorlds().put(this.getWorldName(),this);
                        existingFile.delete();
                    });
        }
        File file = new File(parent,fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static World load(String filename) throws ClassNotFoundException,IOException
    {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fis);
        World loadedWorld= (World) in.readObject();
        loadedWorld.worldName = Path.of(filename).getFileName().toString().replace(".wrl","");
        in.close();
        System.out.println(loadedWorld.getWorldName()+" WORLD is LOADED");
        return loadedWorld;
    }
    public World cloneViaSerialization() {
        try {
            // Serializza l'oggetto Level in un flusso di byte
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            // Deserializza l'oggetto dal flusso di byte per creare una copia
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            World clonedWorld = (World) objectInputStream.readObject();
            objectInputStream.close();
            return clonedWorld;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //UTILITY:
    public void changeLevelName(String oldLevelName, String newLevelName){
        ListIterator<String> iterator = levelPack.listIterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(oldLevelName)) {
                iterator.set(newLevelName);
            }
        }
        save();
    }
    public void addLevel(String levelName){
        levelPack.add(levelName);
    }
    public void removeLevel(String levelName){levelPack.remove(levelName);}

}
