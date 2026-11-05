package model.loader;


import model.loader.resources.*;
import model.managers.FileManager;
import model.managers.UserManager;
import model.utility.LoaderUtilityTools;

import java.io.IOException;



public class ResourceManager
{
    //FIELDS:
    private static ResourceManager instance;
    private Resource resources;

    //CONSTRUCTORS:
    private ResourceManager() {}

    public static ResourceManager getInstance()
    {
        if(instance == null) instance = new ResourceManager();
        return instance;
    }
    //METHODS://///////////////////////////////////////////////////
    //GETTERS:
    public void loadResources()
    {
        try {
            FileManager.loadProgramData();
            LoaderUtilityTools.loadFonts();
            UserManager.getInstance().setUserMap(LoaderUtilityTools.loadUsers());
            loadGameResources();
        }
        catch(Exception e)
        { e.printStackTrace();}

    }
    //LOAD GAME RESOURCES:
    private void loadGameResources() throws IOException
    {
        resources = new Resource() {
            @Override
            public void loadResources() throws IOException {
                new AudioResources();
                new PlayerResources();
                new BombResources();
                new ExplosionResources();
                new AvatarResources();
                new NPCResources();
                new PowerUpResources();
                new BackgroundResources();
                new FloorResources();
                new WallsResources();
                new ObstacleResources();
                new TeleportResources();
                //to be loaded after:
                new MapResources();
            }
        };
        resources.loadResources();

    }
}
