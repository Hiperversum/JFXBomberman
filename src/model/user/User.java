package model.user;
import javafx.scene.image.Image;
import model.loader.resources.Resource;
import model.loader.resources.AvatarResources;
import model.loader.resources.BombResources;
import model.loader.resources.PlayerResources;
import model.managers.FileManager;

import java.io.*;
import java.nio.file.Path;

public class User implements Comparable,Serializable {
    private static final long serialVersionUID = 2L;
    private String nickname,bombSkinKey;
    private int avatarKey,playerSkinKey;
    private Stats myStats;

    private transient String userPath;

    //CONSTRUCTOR:
    public User(String nickname, int avatarKey){
        myStats = new Stats();
        bombSkinKey = "classic";
        playerSkinKey = 1;
        this.nickname = nickname;
        this.avatarKey = avatarKey;
        ((PlayerResources) Resource.getResource(Resource.Type.PLAYER)).setPlayerResourceSelected(1);
        ((BombResources) Resource.getResource(Resource.Type.BOMB)).getBombImages("classic");
    }


    //SETTERS:
    public void setAvatarKey(int avatarKey) {
        this.avatarKey = avatarKey;
    }
    public void setPlayerSkinKey(Integer playerSkinKey) {
        this.playerSkinKey = playerSkinKey;
    }
    public void setBombSkinKey(String bombSkinKey) {
        this.bombSkinKey = bombSkinKey;
    }

    //GETTERS:
    public String getUserPath() {
        return userPath;
    }

    public Stats getStats() {
        return myStats;
    }
    public int getPlayerSkinKey() {
        return playerSkinKey;
    }
    public String getBombSkinKey() {
        return bombSkinKey;
    }
    public String getNickname() {
        return nickname;
    }

    public Image getImage(){
        return ((AvatarResources) Resource.getResource(Resource.Type.AVATARS)).getImage(avatarKey);
    }


    //SERIALIZABLE METHODS:
    public void save() {
        userPath = FileManager.userRoot+File.separator+nickname+File.separator;
        File userDirectory = new File(userPath);
        if (!userDirectory.exists()) {
            userDirectory.mkdirs();
        }
        File myLevelsDirectory = new File(userDirectory, "myLevels");
        if (!myLevelsDirectory.exists()) {
            myLevelsDirectory.mkdirs();
        }
        File myWorldsDirectory = new File(userDirectory, "myWorlds");
        if (!myWorldsDirectory.exists()) {
            myWorldsDirectory.mkdirs();
        }
        File file = new File(userDirectory,"userData.bin");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream out = new ObjectOutputStream(fos);
            out.writeObject(this);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static User load(String filename) throws ClassNotFoundException,IOException
    {
        FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fis);
        User loadedUser = (User) in.readObject();
        String userPath = Path.of(filename).getParent().toString()+File.separator;
        loadedUser.userPath = userPath;
        return loadedUser;
    }
    @Override
    public int compareTo(Object o) {
        return this.nickname.compareToIgnoreCase(((User)o).nickname);
    }
}






















