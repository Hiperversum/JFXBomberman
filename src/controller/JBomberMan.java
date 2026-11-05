package controller;

import javafx.application.Application;
import javafx.stage.Stage;
import model.editor.WorldHandler;
import model.loader.ResourceManager;
import model.managers.UserManager;
import view.MenuView;


public class JBomberMan extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {

        MenuView menuView = MenuView.getInstance();
        ResourceManager rM = ResourceManager.getInstance();
        //START APPLICATION:
        rM.loadResources();
        menuView.startView();
        //REGISTERS OBSERVERS////////////////////////
        UserManager.getInstance().addObserver(menuView);
        UserManager.getInstance().addObserver(menuView.getUserMenu());
        UserManager.getInstance().addObserver(menuView.getStatsMenu());
        WorldHandler.getInstance().addObserver(menuView.getWorldMenu());
        /////////////////////////////////////////////
        UserManager.getInstance().notifyView(UserManager.UpdateType.MENU);


    }
}
