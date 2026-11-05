package view.components.savepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import model.editor.Editor;
import model.editor.LevelEditor;
import model.editor.WorldHandler;

import java.util.Observable;


public class SaveWorldPane extends SavePane{
    public SaveWorldPane(FXMLLoader fxmlLoader){
        super(fxmlLoader);
        initialize();
    }



    private void initialize(){
        savePane = (Pane) fxmlLoader.getNamespace().get("saveWorldPane");
        warningLabel = (Label) fxmlLoader.getNamespace().get("warningWorldLabel");
        textField = (TextField) fxmlLoader.getNamespace().get("worldNameTextField");
        saveButton = (Button)fxmlLoader.getNamespace().get("saveWorldButton");
        backButton = (Button)fxmlLoader.getNamespace().get("backWorldButton");
        if (backButton!=null)
        backButton.setOnMouseClicked(mouseEvent -> {
            setVisible(false);
        });
        handleName(fxmlLoader.getController());
    }

    public void setVisible(boolean visible){
        savePane.setVisible(visible);
    }

    @Override
    protected void checkTextField() {
        String newName = textField.getText();
        if (newName == null || newName.isEmpty()) {
            warningLabel.setText("World name cannot be empty.");
            saveButton.setDisable(true);
        }
        else {
            warningLabel.setText("");
            saveButton.setDisable(false);
        }

    }
    
    private void updateHandler(WorldHandler worldHandler,Editor.UpdateType update) {
    	switch(update) {
    	case UPDATE_WORLD_SAVED->{
    		setVisible(false);
    	}
    	case UPDATE_NEW_WORLD->{
    		textField.setText("");
        	saveButton.setDisable(true);
    	}
    	case UPDATE_WORLD_SELECTED->{
    		textField.setText(worldHandler.getWorld().getWorldName());
    	}
    	
    	}
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof WorldHandler)
        	updateHandler((WorldHandler)o,(Editor.UpdateType)arg);	
       
    }



}
