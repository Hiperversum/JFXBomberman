package controller;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.engine.Engine;
import model.loader.resources.MapResources;
import model.managers.*;
import view.GameView;
import view.View;
import view.components.pane.UserPane;
import view.components.stackpane.WorldPane;


import java.io.IOException;

public class MenuController extends Controller {

    //INIT: ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void initialize() {
        unregistersObserversOfTheGameView();
        unregistersObserversOfTheEditorView();
        animationManager.clear();
        entityManager.clear();
        engine.setGameState(Engine.GameState.MENU);
    }

    //MENU METHODS: ///////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void playNormalGame() throws IOException {
        loadGameScene();
        mapManager.setNormalGameMode();
    }

    public void playCustomLevels() throws IOException {
        loadGameScene();
        if (worldHandler.getWorld()!=null)
        mapManager.setCustomWorldMode(worldHandler.getWorld().getWorldName());
    }
    public void loadGameScene() throws IOException {
        engine.setGameState(Engine.GameState.GAME);
        View view = GameView.getInstance();
        view.load();
        registersObserversOfTheGameView();
    }
    public void loadEditorScene(){
        engine.setGameState(Engine.GameState.EDITOR);
        levelEditor.startEditor();
        editorView.load();
        registersObserversOfTheEditorView();
    }
    public void exit() {
        FileManager.saveProgramData();
        Platform.exit();
    }

    //FIRST MENU METHODS: /////////////////////////////////////////////////////////////////////////////////////////////////////
    public void continueByLastUser() {
        menuView.showMenu();
        userManager.notifyView(UserManager.UpdateType.LOAD_USER);
    }
    public void newUser() {
        userManager.notifyView(UserManager.UpdateType.NEW_USER);

    }
    //LOAD USER PANE METHODS: /////////////////////////////////////////////////////////////////////////////////////////////////
    public void loadUser(MouseEvent mouseEvent) {
        Pane clickedPane = (Pane) mouseEvent.getSource();
        userManager.setUserSelected(clickedPane.getId());
        MapResources.loadUserMap(userManager.getUserLoaded());
        userManager.notifyView(UserManager.UpdateType.LOAD_USER);
    }
    public void deleteUser() {
        UserManager.getInstance().deleteUser();
        menuView.setInvisibleAllPane();
        showLoadMenuPane();
    }

    //EDIT USER PANE METHODS: ////////////////////////////////////////////////////////////////////////////////////////////////
    public void createNewUser() {
        userManager.newUser();
        showMenu();
    }

    //SHOW METHODS: /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void backToFirstMenu() {
        userManager.notifyView(UserManager.UpdateType.LOAD_USER);
        userManager.notifyView(UserManager.UpdateType.MENU);
        userManager.setNickname(null);
    }
    public void showMenu() {
        if(userManager.getUserLoaded()!=null) {
            menuView.showMenu();
            userManager.notifyView(UserManager.UpdateType.LOAD_USER);
        }
    }
    public void showStatsPane() {
        userManager.notifyView(UserManager.UpdateType.LOAD_STATS);
    }
    public void showSettingsPane() {
        menuView.showSettingsPane();
    }
    public void showLoadMenuPane() {
        userManager.setUserSelected(null);
        userManager.notifyView(UserManager.UpdateType.LOAD_USERS_MENU);
    }
    public void showCustomLevels(){
        worldHandler.setUserWorlds();
    }
    //EVENTS (CALLBACK)
    @Override
    protected void eventHandler(Event event, Object o) {

        if (event!=null) {
            //USER MENU:
            if (event.getSource() instanceof Button) {
                if (((Button) event.getSource()).getParent() instanceof UserPane) {
                    UserManager.getInstance().setUserSelected(((UserPane) ((Button) event.getSource()).getParent()).getUserID());
                }
            } else if (event.getSource() instanceof WorldPane) {
                worldHandler.setWorld(((Node) event.getSource()).getId());
            } else if (event.getSource() instanceof UserPane) {
                userManager.setUserSelected(((UserPane) event.getSource()).getUserID());
                userManager.notifyView(UserManager.UpdateType.LOAD_USER);
                loadUser((MouseEvent) event);
            } else if (event.getSource() instanceof ImageView) {
                userManager.setAvatarKey(Integer.parseInt(((ImageView) event.getSource()).getId()));
            } else if (event.getSource() instanceof Pane) {
                if (((Pane) event.getSource()).getId().equals("editUserPane")) {
                    userManager.setNickname((String) o);
                }

            }
            //WORLD MENU:
            else if (event.getSource() instanceof WorldPane) {
                worldHandler.setWorld(((Node) event.getSource()).getId());
            }
        }
        if (userManager.getUserLoaded() != null)
            userManager.notifyView(UserManager.UpdateType.LOAD_USER);
    }
}
