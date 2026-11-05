package model.world;
import javafx.scene.image.Image;
import model.Model;
import model.managers.FileManager;
import model.managers.MapManager;
import model.managers.UserManager;
import model.utility.LoaderUtilityTools;
import model.world.components.entity.Entity;
import model.world.components.tiles.Tile;
import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Level extends Model implements Serializable {
    private static final long serialVersionUID = 1L;
    //FIELDS: //////////////////////////////////////////////////////////////////////////////////////////////////////////
    private String levelName;
    private byte[] imageBytes;
    private Tile[][] map;
    private List<Entity> entityList;
    private transient Image image;

    //CONSTRUCTOR: /////////////////////////////////////////////////////////////////////////////////////////////////////
    public Level(Tile[][] map,List<Entity> entityList) {
        this.map = map;
        this.entityList = entityList;
    }

    //METHODS: /////////////////////////////////////////////////////////////////////////////////////////////////////////

    //GETTERS:
    public String getLevelName() {
        return levelName;
    }

    public Image getImage() {
        if (image == null) image = LoaderUtilityTools.imageBytesToImage(imageBytes);
        return image;
    }

    public Tile[][] getMap() {
        return map;
    }
    public List<Entity> getEntityList() {
        return entityList;
    }

    //SETTERS:
    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setMap(Tile[][] map) {
        this.map = map;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }
    //ENTITY LIST METHODS:
    public void addEntity(Entity entity){
        entityList.add(entity);
    }

    //SERIALIZABLE METHODS:
    public void save() {
        String parent =
                FileManager.userRoot + File.separator+
                UserManager.getInstance().getUserSelected() + File.separator+ "myLevels";
        String fileName = levelName+".lvl";
        //Check if a file with this name already exists
        File[] filesInDirectory = new File(parent).listFiles();
        if (filesInDirectory != null) {
            Arrays.stream(filesInDirectory)
                    .filter(existingFile -> existingFile.getName().equalsIgnoreCase(fileName))
                    .forEach(existingFile -> {
                        String existingFileName = existingFile.getName().replace(".lvl", "");
                        MapManager mapManager = MapManager.getInstance();
                        mapManager.getUserLevels().remove(existingFileName);
                        mapManager.getUserLevels().put(this.getLevelName(),this);
                        List<World> worlds = new ArrayList<>(mapManager.getUserWorlds().values());
                        for (World world: worlds){
                            world.changeLevelName(existingFileName,levelName);
                        }
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

    public static Level load(String filename) throws ClassNotFoundException,IOException
    {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fis);
        Level loadedLevel = (Level) in.readObject();
        loadedLevel.levelName = Path.of(filename).getFileName().toString().replace(".lvl","");
        in.close();
        System.out.println(loadedLevel.getLevelName()+" LEVEL is LOADED");
        return loadedLevel;
    }
    public Level cloneViaSerialization() {
        try {
            // Serializza Level
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(this);
            objectOutputStream.close();
            // Deserializza per creare una copia
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Level clonedLevel = (Level) objectInputStream.readObject();
            objectInputStream.close();
            // Crea Immagine
            clonedLevel.setImage(LoaderUtilityTools.imageBytesToImage(clonedLevel.imageBytes));
            return clonedLevel;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}






