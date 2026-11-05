package controller;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import model.Cursor;
import model.editor.Editor;
import model.utility.LoaderUtilityTools;
import model.loader.resources.Resource;
import model.managers.FileManager;
import model.world.components.Theme;
import view.components.button.BackgroundButton;
import view.components.button.ThemeButton;
import view.components.fxmlmodifier.gridpane.EntityGridPane;
import view.components.fxmlmodifier.gridpane.LevelGridPane;
import view.components.pane.LevelPane;
import view.components.savepane.SaveLevelPane;
import view.components.savepane.SaveWorldPane;
import view.components.stackpane.WorldPane;



public class EditorController extends Controller {
    //CAMPI:////////////////////////////////////////////////////////
    @FXML private Canvas canvas;
    @FXML private ImageView levelPreview;

    //EDITOR PANE:
    public void initialize() {
        unregistersObserversOfTheMenuView();
        mouseController();
    }
    public void setCategory(ActionEvent actionEvent) {
        levelEditor.setEditorResourceSelected(Resource.Type.fromString(((Button) actionEvent.getSource()).getId()));
    }
    public void setTheme(){
        levelEditor.notifyView(Editor.UpdateType.UPDATE_THEME_SELECTED);
    }
    private void mouseController()
    {
        Cursor cursor = levelEditor.getCursor();
        canvas.setOnMouseExited(mouseEvent -> {
            cursor.setCursorExited();
        });
        canvas.setOnMouseEntered(mouseEvent -> {
            cursor.setCursorEntered();
        });
        canvas.setOnMouseMoved(mouseEvent -> {
                cursor.setY((int) mouseEvent.getY());
                cursor.setX((int) mouseEvent.getX());
                cursor.setYDropped((int) mouseEvent.getY());
                cursor.setXDropped((int) mouseEvent.getX());
        });

        canvas.setOnMousePressed(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    cursor.setMouseButton(Cursor.MouseButton.LEFT_CLICK);

                } else if (mouseEvent.isSecondaryButtonDown()) {
                    cursor.setMouseButton(Cursor.MouseButton.RIGHT_CLICK);
                }
        });

        canvas.setOnMouseDragged(mouseEvent -> {
                if (mouseEvent.isPrimaryButtonDown()) {
                    cursor.setMouseButton(Cursor.MouseButton.LEFT_CLICK);

                } else if (mouseEvent.isSecondaryButtonDown()) {
                    cursor.setMouseButton(Cursor.MouseButton.RIGHT_CLICK);
                }
                cursor.setYDropped((int) mouseEvent.getY());
                cursor.setXDropped((int) mouseEvent.getX());
        });
        canvas.setOnMouseReleased(mouseEvent -> {
                switch (cursor.getMouseButton()) {
                    case RIGHT_CLICK -> levelEditor.remove();
                    case LEFT_CLICK -> levelEditor.place();
                }
                ;
                cursor.setYDropped(cursor.getY());
                cursor.setXDropped(cursor.getX());
           }

           );
    }
    
    public void resetEditor() {
    	levelEditor.startEditor();
    }

    //LEVEL METHODS: ////////////////////////////////////////////////////////////////////////////////////////////////////
    public void showLoadPane(){
        levelEditor.setUserLevels();
        editorView.getLevelMenu().showLoadLevelPane();
    }
    public void loadLevel() {
        levelEditor.loadLevelSelected();
    }
    public void showSavePane(){
        editorView.getLevelMenu().showSaveLevelPane();
        levelPreview.setImage(canvasToImage());
    }
    public void saveLevel() {
        levelEditor.getLevel().setImage(canvasToImage());
        levelEditor.getLevel().setImageBytes(LoaderUtilityTools.imageToImageByte(canvasToImage()));
        levelEditor.saveLevel();
    }
    public void deleteLevel() {
        levelEditor.deleteLevel();
    }

    //WORLD METHODS: ///////////////////////////////////////////////////////////////////////////////////////////////////
    public void showWorldPane() {
        editorView.getWorldMenu().showWorldPane();
        worldHandler.setUserWorlds();
    }
    public void showSaveWorldPane() {
        editorView.getWorldMenu().showSaveWorldPane();
    }
    public void showWorldEditorPane(){
            levelEditor.setUserLevels();
            editorView.getWorldMenu().showEditorPane();
    }
    public void newWorld() {
        worldHandler.createNewWorld();
        showWorldEditorPane();
    }
    public void addLevelToWorld() {
        worldHandler.addLevel();
    }
    public void removeLevelFromWorld() {
        worldHandler.removeLevel();
    }
    public void saveWorld() {
        worldHandler.saveWorld();
    }
    public void deleteWorld(){
        worldHandler.deleteWorld();
    }

    //UTILITY: /////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void exit() {
        menuView.load();
        registersObserversOfTheMenuView();
        menuView.back();
        FileManager.saveProgramData();
    }
    //UTILITY:
    private Image canvasToImage(){
        WritableImage writableImage = new WritableImage((int) canvas.getWidth(), (int) canvas.getHeight());
        canvas.snapshot(null, writableImage);
        return writableImage;
    }

    //////////////////
    //CALLBACK:
    @Override
    protected void eventHandler(Event event,Object o) {
        if (event != null) {
            if (event.getSource() instanceof LevelPane) {
                worldHandler.setLevelSelected(((LevelPane) event.getSource()).getId());
            } else if (event.getSource() instanceof LevelGridPane.LevelGridPaneCell) {
                worldHandler.setLevelSelected(((LevelGridPane.LevelGridPaneCell) event.getSource()).getId());
                levelEditor.setLevelSelected(((LevelGridPane.LevelGridPaneCell) event.getSource()).getId());
            } else if (event.getSource() instanceof WorldPane) {
                worldHandler.setWorld(((Node) event.getSource()).getId());
            } else if (event.getSource() instanceof EntityGridPane.EntityGridPaneCell) {
                levelEditor.setWorldComponent(((EntityGridPane.EntityGridPaneCell) event.getSource()).getId());
            } else if (event.getSource() instanceof BackgroundButton) {
                levelEditor.setBackground(((Button) event.getSource()).getId());
            } else  if (event.getSource() instanceof ThemeButton){
                levelEditor.setTheme(Theme.fromString(((ThemeButton) event.getSource()).getId()));
            }
        }

        // EVENT == NULL
        if (o instanceof SaveWorldPane){
            worldHandler.getWorld().setWorldName(((SaveWorldPane)o).getTextField().getText().trim());
        }
        else if (o instanceof SaveLevelPane){
            levelEditor.getLevel().setLevelName(((SaveLevelPane)o).getTextField().getText().trim());
        }

    }


}


















