package view.menu;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.editor.Editor;
import model.editor.LevelEditor;
import view.components.fxmlmodifier.gridpane.LevelGridPane;
import view.components.savepane.SaveLevelPane;

import java.util.Observable;


public class LevelMenu extends Menu {

    //COMPONENTS:
    private Pane loadLevelPane;
    private LevelGridPane levelGridPane;
    private SaveLevelPane saveLevelPane;
    private TextField levelNameTextField;
    private Label labelLevelName;
    private Button backButtonLoadLevel,loadButton;

    //CONSTRUCTOR:
    public LevelMenu(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }
    private void initialize(){
        saveLevelPane = new SaveLevelPane(fxmlLoader);
        levelGridPane = new LevelGridPane(fxmlLoader,(GridPane) fxmlLoader.getNamespace().get("levelGridPane"));
        levelNameTextField = (TextField) fxmlLoader.getNamespace().get("levelNameTextField");
        labelLevelName = (Label) fxmlLoader.getNamespace().get("labelLevelName");
        loadLevelPane = (Pane) fxmlLoader.getNamespace().get("loadLevelPane");
        loadButton = (Button) fxmlLoader.getNamespace().get("loadButton");
        backButtonLoadLevel = (Button) fxmlLoader.getNamespace().get("backButtonLoadLevel");
        if (backButtonLoadLevel!=null)
            backButtonLoadLevel.setOnMouseClicked(mouseEvent -> {
                loadLevelPane.setVisible(false);
                loadButton.setDisable(true);
            });
    }
    //METHODS:
    public void showSaveLevelPane() {
    	saveLevelPane.setVisible(true);
    	}
    public void showLoadLevelPane(){
        loadLevelPane.setVisible(true);

    }
    //GETTERS:
    public SaveLevelPane getSaveLevelPane() {
        return saveLevelPane;
    }

    private void levelEditorUpdateHandler(LevelEditor levelEditor, Editor.UpdateType updateType) {
        if (updateType!=null)
            switch (updateType) {
                case UPDATE_USER_LEVELS -> {
                    levelGridPane.initializeLevelGridPane(levelEditor.getUserLevels());
                }
                case UPDATE_LEVEL_LOADED -> {
                    levelNameTextField.setText(levelEditor.getLevel().getLevelName());
                    labelLevelName.setText(levelEditor.getLevel().getLevelName());
                    loadLevelPane.setVisible(false);
                }
                case UPDATE_LEVEL_SAVED ->{
                    labelLevelName.setText(levelEditor.getLevel().getLevelName());
                }
                case UPDATE_LEVEL_SELECTED -> {
                    loadButton.setDisable(false);
                }
                case UPDATE_LEVEL_DELETED -> {
                    levelGridPane.initializeLevelGridPane(levelEditor.getUserLevels());
                    levelGridPane.getLevelSafePane().hideSafePane();
                    loadButton.setDisable(true);
                }
                case UPDATE_NEW_LEVEL ->{
                	labelLevelName.setText("");
                	levelNameTextField.setText("");
                	
                }

            }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelEditor){
            levelEditorUpdateHandler((LevelEditor)o,(Editor.UpdateType) arg);
        }

    }


}
