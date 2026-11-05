package model.managers;

import model.Model;
import model.loader.resources.AvatarResources;
import model.loader.resources.Resource;
import model.user.User;


import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserManager extends Model {
    public enum UpdateType{
        LOAD_USERS_MENU, USER_DELETED, NEW_USER,LOAD_STATS,LOAD_USER, MENU;
    }
    private static UserManager instance;
    private Map<String,User> userMap;
    private String userSelected;
    private int avatarKey;
    private String nickname;
    private UserManager(){
        userMap = new TreeMap<>();
    }

    public static UserManager getInstance(){
        if(instance == null) instance = new UserManager();
        return instance;
    }
    //METHODS:
    public void setUserSelected(String userID) {
        userSelected = userID;
    }
    public void deleteUser(){
        if (FileManager.removeUserFile(userSelected)) {
            MapManager mapManager = MapManager.getInstance();
            mapManager.getLevels().remove(getUser(userSelected));
            mapManager.getWorlds().remove(getUser(userSelected));
            userMap.remove(userSelected);
            userSelected = null;
            notifyView(UpdateType.USER_DELETED);
            notifyView(UpdateType.MENU);
        }
        FileManager.saveProgramData();
    }
    public void newUser(){
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9 ]");
        Matcher matcher = pattern.matcher(nickname);
        if(nickname!= null && !nickname.trim().isEmpty() && !matcher.find() && !userMap.containsKey(nickname))  {
            userSelected = nickname;
            User user = new User(nickname,avatarKey);
            userMap.put(nickname,user);
            userMap.get(userSelected).save();
            notifyView(UpdateType.NEW_USER);
        }
    }
    public AvatarResources getUserResources() {
        return (AvatarResources) Resource.getResource(Resource.Type.AVATARS);
    }

    public User getUserLoaded() {
        if (userSelected == null) return null;
        if (userMap.containsKey(userSelected)) return userMap.get(userSelected);
        return null;
    }
    public User getUser(String nickname){
        return userMap.get(nickname);
    }
    public Map<String, User> getUserMap() {
        return userMap;
    }
    public String getUserSelected() {
        return userSelected;
    }
    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }
    public void setAvatarKey(int key){
        avatarKey = key;
    }
    public void setNickname(String nickname){
        this.nickname = nickname;
    }
    public void notifyView(UpdateType updateType){
        setChanged();
        notifyObservers(updateType);
    }
}
