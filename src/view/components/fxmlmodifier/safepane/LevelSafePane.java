package view.components.fxmlmodifier.safepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class LevelSafePane extends SafePane {
    private ImageView imgViewLevelToDelete;
    public LevelSafePane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        safePane  = (Pane)fxmlLoader.getNamespace().get("levelSafePane");
        labelObjToDelete = (Label)fxmlLoader.getNamespace().get("labelLevelToDelete");
        imgViewLevelToDelete = (ImageView) fxmlLoader.getNamespace().get("imgViewLevelToDelete");
        backButton = (Button) fxmlLoader.getNamespace().get("backButtonLevelSafePane");
        if (backButton!=null)
        backButton.setOnMouseClicked(mouseEvent -> {
            hideSafePane();
        });

    }

    public void showLevelSafePane(String levelName,Image levelPreview){
        safePane.setVisible(true);
        labelObjToDelete.setText(levelName);
        imgViewLevelToDelete.setImage(levelPreview);
    }

}
