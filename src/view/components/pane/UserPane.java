package view.components.pane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;


public class UserPane extends CustomPane{

    private String userID;
    private Image image,trashcanImage;
    private Button deleteButton;

    public UserPane(FXMLLoader fxmlLoader, String userID, Image image){
        this.userID = userID;
        this.image = image;
        trashcanImage = ((ImageView)fxmlLoader.getNamespace().get("deleteImV")).getImage();
        initializeLayout(fxmlLoader);
        initialize();
    }
    private void initializeLayout(FXMLLoader fxmlLoader){
        Pane userPane = (Pane)fxmlLoader.getNamespace().get("userPane");
        setPrefHeight(userPane.getPrefHeight());
        setPrefWidth(userPane.getPrefWidth());
    }

    private void initialize() {
        //PANE:
        setId(userID);
        getStyleClass().add("pane-selectable");
        //IMAGEVIEW:
        ImageView userAvatar = new ImageView(image);
        userAvatar.setDisable(true);
        userAvatar.setLayoutX(8);
        userAvatar.setLayoutY(9);
        userAvatar.setFitHeight(50);
        userAvatar.setFitWidth(52);
        //LABEL:
        Label userName = new Label(userID);
        userName.setLayoutX(68);
        userName.setLayoutY(18);
        userName.getStyleClass().add("label-loadUser");
        //IMAGEVIEW TRASHCAN
        deleteButton = new Button();
        ImageView deleteUser = new ImageView(trashcanImage);
        deleteUser.setFitHeight(31);
        deleteUser.setFitWidth(27);
        deleteUser.setPreserveRatio(false);
        deleteUser.getStyleClass().clear();
        deleteButton.setGraphic(deleteUser);
        deleteButton.setLayoutX(368);
        deleteButton.setLayoutY(15);
        deleteButton.getStyleClass().add("invisible-button");
        getChildren().addAll(userAvatar, userName, deleteButton);
    }

    //GETTERS:

    public String getUserID() {
        return userID;
    }

    public Image getImage() {
        return image;
    }

    public Button getDeleteButton() {
        return deleteButton;
    }




}
