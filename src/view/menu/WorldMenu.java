package view.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.editor.Editor;
import model.editor.LevelEditor;
import model.editor.WorldHandler;
import model.world.Level;
import model.world.World;
import view.components.fxmlmodifier.gridpane.LevelGridPane;
import view.components.pane.LevelPane;
import view.components.fxmlmodifier.safepane.WorldSafePane;
import view.components.savepane.SaveWorldPane;
import view.components.stackpane.WorldPane;
import view.tools.SelectablePaneManager;
import java.util.Collection;
import java.util.Observable;



public class WorldMenu extends Menu {

    //COMPONENTS:
    private Pane selectWorldPane,worldEditorPane,worldPanel,newWorldPane,customLevelsPane;
    private Label labelWorldName;
    private VBox levelListVBox,worldVBox;
    private LevelGridPane levelGridPane;
    private WorldSafePane worldSafePane;
    private SaveWorldPane saveWorldPane;
    private Button playCustomWorldButton,backButtonWorldMenu,editWorldButton;
    //
    //CONSTRUCTOR:
    public WorldMenu(FXMLLoader fxmlLoader){
        super(fxmlLoader);
        initialize();
    }
    private void initialize(){
        worldSafePane = new WorldSafePane(fxmlLoader);
        saveWorldPane = new SaveWorldPane(fxmlLoader);
        GridPane levels = (GridPane) fxmlLoader.getNamespace().get("worldEditorLevelGridPane");
        if (levels!=null) levelGridPane = new LevelGridPane(fxmlLoader,levels);
        labelWorldName = (Label)fxmlLoader.getNamespace().get("labelWorldName");
        worldVBox = (VBox) fxmlLoader.getNamespace().get("worldVBox");
        //PANE:
        levelListVBox = (VBox) fxmlLoader.getNamespace().get("levelListVBox");
        selectWorldPane = (Pane) fxmlLoader.getNamespace().get("selectWorldPane");
        worldEditorPane = (Pane) fxmlLoader.getNamespace().get("worldEditorPane");
        worldPanel = (Pane) fxmlLoader.getNamespace().get("worldPanel");
        newWorldPane = (Pane) fxmlLoader.getNamespace().get("newWorldPane");
        customLevelsPane = (Pane) fxmlLoader.getNamespace().get("customLevelsPane");
        //BUTTON:
        editWorldButton = (Button) fxmlLoader.getNamespace().get("editWorldButton");
        playCustomWorldButton = (Button) fxmlLoader.getNamespace().get("playCustomWorldButton");
        backButtonWorldMenu = (Button) fxmlLoader.getNamespace().get("backButtonWorldMenu");
        if (backButtonWorldMenu!=null)
            backButtonWorldMenu.setOnMouseClicked(mouseEvent -> {
                worldPanel.setVisible(false);
            });

    }
    //GETTERS:

    public SaveWorldPane getSaveWorldPane() {
        return saveWorldPane;
    }

    //UTILITY:
    public void cleanWorldPreview(){
        levelListVBox.getChildren().clear();
        levelListVBox.setPrefHeight(0);
    }
    //SHOW METHODS:
    public void showWorldPane(){
        cleanWorldPreview();
        labelWorldName.setText(null);
        selectWorldPane.setVisible(true);
        worldEditorPane.setVisible(false);
        worldPanel.setVisible(true);
        editWorldButton.setDisable(true);
    }
    public void showEditorPane() {
        selectWorldPane.setVisible(false);
        saveWorldPane.setVisible(false);
        worldEditorPane.setVisible(true);
        levelListVBox.getChildren().forEach(pane->((LevelPane)pane).setIsSelectable(true));
        
    }
    public void showSaveWorldPane() {saveWorldPane.setVisible(true);}
    private void showSafePane(String id){worldSafePane.showWorldSafePane(id);}

    //VISIBLE & DISABLE:
    public void showCustomLevels(){
        if (customLevelsPane!=null) {
            customLevelsPane.setVisible(customLevelsPane.isVisible() ? false : true);
            playButtonSetDisable(true);
            if (customLevelsPane.isVisible()) {
                levelListVBox.getChildren().clear();
                levelListVBox.setPrefHeight(0);
                labelWorldName.setText(null);
            }
        }
    }
    public void playButtonSetDisable(boolean disable){
        if (playCustomWorldButton!=null)
        playCustomWorldButton.setDisable(disable);
    }
    public void editButtonSetDisable(boolean disable){
        if (editWorldButton!=null)
            editWorldButton.setDisable(disable);
    }
    //WORLD VBOX:
    private void cleanWorldVBox(){
        worldVBox.getChildren().clear();
        if (newWorldPane!=null) {
            worldVBox.getChildren().add(newWorldPane);
            worldVBox.setPrefHeight(newWorldPane.getPrefHeight());
        }
        else worldVBox.setPrefHeight(0);
    }
    private void initializeWorldVBox(Collection<World> worlds) {
        cleanWorldVBox();
        for (World world: worlds) {
            String worldName = world.getWorldName();

            WorldPane worldPane = new WorldPane(worldVBox.getPrefWidth(), 100, worldName);
            ////////////////// CUSTOMIZE WORLD PANE:
            if (world.getLevelPack().isEmpty()) {
                worldPane.setEmpty();
                if (customLevelsPane != null)
                worldPane.setDisable(true);
            }
            if (customLevelsPane != null) worldPane.canBeDeleted(false);
            /////////////////////
            worldVBox.getChildren().add(worldPane);
            worldVBox.setPrefHeight(worldVBox.getPrefHeight() + worldPane.getPrefHeight());
            //EVENTS:
            SelectablePaneManager.setEvents(worldPane,mouseEvent -> {
                labelWorldName.setText(worldName);
                controller.setEvent(mouseEvent);
            });
            worldPane.setDeleteLabelOnMouseClicked(mouseEvent -> {
                controller.setEvent(mouseEvent);
                showSafePane(worldName);
            });
        }
    }

    //WORLD LEVELS:
    private void addLevelPane(String levelName, Image levelPreview){
        LevelPane levelPane = new LevelPane(levelListVBox.getPrefWidth(),80, levelName, levelPreview);
        levelListVBox.getChildren().add(levelPane);
        levelListVBox.setPrefHeight(levelListVBox.getPrefHeight() + levelPane.getPrefHeight());
        SelectablePaneManager.setEvents(levelPane,mouseEvent -> {
            controller.setEvent(mouseEvent);
        });

    }

    ////////////////////////////////////////////
    //OBSERVER & OBSERVABLE UPDATES:
    private void worldEditorUpdateHandler(WorldHandler worldHandler, Editor.UpdateType updateType){
        if (updateType!=null)
        switch (updateType){
            case UPDATE_ADD_LEVEL_TO_WORLD-> {
                Level levelToAdd = WorldHandler.getInstance().getLevelSelected();
                addLevelPane(levelToAdd.getLevelName(), levelToAdd.getImage());
            }
            case UPDATE_LEVELS_OF_WORLDS-> {
                cleanWorldPreview();
                labelWorldName.setText(worldHandler.getWorld().getWorldName());
                worldHandler.getLevelPack().forEach(level -> addLevelPane(level.getLevelName(), level.getImage()));
                playButtonSetDisable(false);
            }
            case UPDATE_USER_WORLDS-> {
                showCustomLevels();
                initializeWorldVBox(worldHandler.getUserWorlds());
            }
            case UPDATE_WORLD_DELETED-> {
                cleanWorldPreview();
                initializeWorldVBox(worldHandler.getUserWorlds());
                worldSafePane.hideSafePane();
                editWorldButton.setDisable(true);
                labelWorldName.setText(null);
            }
            case UPDATE_LEVEL_DELETED-> {
                cleanWorldPreview();
                levelGridPane.getLevelSafePane().hideSafePane();
                levelGridPane.initializeLevelGridPane(worldHandler.getLevelEditor().getUserLevels());
                if (worldHandler.getLevelPack()!=null)
                worldHandler.getLevelPack().forEach(level-> addLevelPane(level.getLevelName(), level.getImage()));
            }
            case UPDATE_WORLD_SAVED -> {
                labelWorldName.setText(worldHandler.getWorld().getWorldName());
            }
            case UPDATE_WORLD_SELECTED -> {
                editButtonSetDisable(false);
                playButtonSetDisable(false);
            }
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WorldHandler){
            worldEditorUpdateHandler((WorldHandler)o,(Editor.UpdateType) arg);
        }
        if (o instanceof LevelEditor){
            LevelEditor levelEditor = (LevelEditor) o;
            if (arg == Editor.UpdateType.UPDATE_USER_LEVELS){
                levelGridPane.initializeLevelGridPane(levelEditor.getUserLevels());
            }

        }
    }

}




