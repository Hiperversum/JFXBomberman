package view.components.fxmlmodifier.safepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public class UserSafePane extends SafePane {
    private ImageView userAvatarToDelete;
    public UserSafePane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }

    private void initialize(){
        safePane  = (Pane)fxmlLoader.getNamespace().get("userSafePane");
        labelObjToDelete = (Label) fxmlLoader.getNamespace().get("userNameToDelete");
        userAvatarToDelete = (ImageView)fxmlLoader.getNamespace().get("userAvatarToDelete");

    }

    public void showUserSafePane(String userName, Image userAvatar){
        safePane.setVisible(true);
        labelObjToDelete.setText(userName);
        userAvatarToDelete.setImage(userAvatar);

    }

    public void hidePane(){
        safePane.setVisible(false);
    }
}
