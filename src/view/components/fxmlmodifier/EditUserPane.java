package view.components.fxmlmodifier;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import view.components.fxmlmodifier.FXMLComponentModifier;

import java.util.Map;

public class EditUserPane extends FXMLComponentModifier {

    private TextField playerNameTextField;
    private ImageView editUserAvatar;
    private GridPane avatarsGridPane;
    private Pane editUserPane;

    public EditUserPane(FXMLLoader fxmlLoader){
        super(fxmlLoader);
        initialize();
    }

    public void initialize(){

        playerNameTextField = (TextField) fxmlLoader.getNamespace().get("playerNameTextField");
        avatarsGridPane = (GridPane) fxmlLoader.getNamespace().get("avatarsGridPane");
        editUserAvatar = (ImageView) fxmlLoader.getNamespace().get("editUserAvatar");
        editUserPane = (Pane)  fxmlLoader.getNamespace().get("editUserPane");
    }
    //METHODS:
    public void setEditUserPane(Map<Integer, Image> avatarsMap){
        int columns = avatarsGridPane.getColumnCount();
        int y = 0;
        int x = 0;
        for (Map.Entry<Integer, Image> entry : avatarsMap.entrySet()) {

            Integer key = entry.getKey();
            Image image = entry.getValue();

            ImageView imgV = new ImageView(image);
            imgV.setFitWidth(52);
            imgV.setFitHeight(50);
            imgV.setPreserveRatio(true);
            imgV.setId(key.toString());

            GridPane.setHalignment(imgV, HPos.CENTER);
            GridPane.setValignment(imgV, VPos.CENTER);

            avatarsGridPane.add(imgV, x, y);

            imgV.setOnMouseClicked(event -> {
                editUserAvatar.setImage(image);
                controller.setEvent(event);
            });

            x++;
            if (x >= columns) {
                x = 0;
                y++;
            }
        }

    }
    public void handleNickname(){
        playerNameTextField.clear();
        playerNameTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().trim().length() <= 11) {
                return change;
            }
            return null;
        }));
        playerNameTextField.setOnMouseClicked(event -> {
            playerNameTextField.requestFocus();
        });

        editUserPane.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
            if (!playerNameTextField.isHover()) {
                playerNameTextField.getParent().requestFocus();
                playerNameTextField.setText(playerNameTextField.getText().trim());
                controller.setEvent(event,playerNameTextField.getText().trim());
            }
        });
    }
}
