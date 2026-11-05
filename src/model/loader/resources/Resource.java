package model.loader.resources;

import javafx.scene.image.Image;
import model.Model;
import model.world.components.WorldComponent;



import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Resource extends Model {

    public enum Type{
        PLAYER,BOMB,EXPLOSION,AVATARS,NPC,POWER_UP,
        BACKGROUND,FLOOR,WALL,OBSTACLES,TELEPORT;

        @Override
        public String toString(){
            return name().toLowerCase();
        }

        public static Type fromString(String name) {
            return Arrays.stream(Type.values())
                    .filter(e -> e.name().equalsIgnoreCase(name))
                    .findFirst().orElseThrow(() -> new IllegalArgumentException("No Resource.Type constant with name: " + name));
        }
    }

    protected Type type;
    private static Map<Type, Resource> resources;
    protected Map<String, WorldComponent> worldComponentMap;
    protected Map<String, Image> icoMap;

    //CONSTRUCTOR:
    public Resource(){
        if (resources == null) resources = new HashMap<>();
    }

    protected Resource(Type type){
        this.type = type;
        resources.put(type,this);
    }
    public abstract void loadResources() throws IOException;

    protected void checkResources(){
        System.out.println(type.toString().toUpperCase()+" Resources is Loaded");
    }

    public static Resource getResource(Type type){ return resources.get(type);}

    //GETTERS:
    public Map<String, WorldComponent> getWorldComponentMap() {
        return worldComponentMap;
    }
    public Map<String, Image> getIcoMap() {
        return icoMap;
    }

    //TO STRING:
    @Override
    public String toString() {
        String typeString = type.toString();
        if (typeString.length() > 0) {
            char firstChar = Character.toUpperCase(typeString.charAt(0));
            typeString = firstChar + typeString.substring(1);
        }
        return typeString;
    }


}
