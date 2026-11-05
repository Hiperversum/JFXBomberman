package view.components.savepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.editor.Editor;
import model.editor.LevelEditor;
import view.menu.LevelMenu;

import java.util.Observable;

public class SaveLevelPane extends SavePane{
    public SaveLevelPane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        savePane = (Pane) fxmlLoader.getNamespace().get("savePane");
        warningLabel = (Label) fxmlLoader.getNamespace().get("warningLabel");
        textField = (TextField) fxmlLoader.getNamespace().get("levelNameTextField");
        saveButton = (Button)fxmlLoader.getNamespace().get("saveButton");
        backButton = (Button)fxmlLoader.getNamespace().get("backLevelButton");
        if (backButton!=null)
        backButton.setOnMouseClicked(mouseEvent -> {
            setVisible(false);
        });
        handleName(fxmlLoader.getController());
    }

    public void setVisible(boolean visible){
        savePane.setVisible(visible);
        checkTextField();
    }
    @Override
    protected void checkTextField() {
        String newName = textField.getText();
        if (newName == null || newName.isEmpty()) {
            warningLabel.setText("Level name cannot be empty.");
            saveButton.setDisable(true);
        }
        else {
            warningLabel.setText("");
            saveButton.setDisable(false);
        }

    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof LevelEditor){
            if (arg == Editor.UpdateType.UPDATE_LEVEL_OBSTACLE_NOT_FOUND){
                warningLabel.setText("The level must contain at least one obstacle.");
                saveButton.setDisable(true);
            }
            if (arg == Editor.UpdateType.UPDATE_LEVEL_SAVED){
                setVisible(false);
            }
        }
    }
}
