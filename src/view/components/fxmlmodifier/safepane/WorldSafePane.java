package view.components.fxmlmodifier.safepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

public class WorldSafePane extends SafePane {

    public WorldSafePane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        safePane  = (Pane)fxmlLoader.getNamespace().get("worldSafePane");
        labelObjToDelete = (Label)fxmlLoader.getNamespace().get("worldNameToDelete");
        backButton = (Button) fxmlLoader.getNamespace().get("backButtonWorldSafePane");
        if (backButton!=null)
        backButton.setOnMouseClicked(mouseEvent -> {
            hideSafePane();
        });

    }

    public void showWorldSafePane(String id){
        safePane.setVisible(true);
        labelObjToDelete.setText(id);
    }


    public void setVisible(boolean visible){
        safePane.setVisible(visible);
    }


}
