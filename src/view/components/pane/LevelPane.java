package view.components.pane;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class LevelPane extends CustomPane {
    private String levelName;
    private Image image;
    public LevelPane(double width, double height, String levelName, Image image){
        super(width,height);
        this.levelName = levelName;
        this.image = image;
        initialize();
        
    }
    private void initialize() {
    	setIsSelectable(false);
        setId(levelName);
        //ImageView
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(60);
        imageView.setLayoutY(getPrefHeight() / 2 - imageView.getFitHeight() / 2);
        imageView.setLayoutX(8);
        //Label:
        Label label = new Label(levelName);
        label.setStyle("-fx-font-family: 'Gill Sans Ultra Bold'; -fx-text-fill: #ffffff; -fx-font-size: 18");
        label.setLayoutY((getPrefHeight() / 2) - (label.getPrefHeight() / 2) - 11);
        label.setLayoutX(imageView.getLayoutX() + imageView.getFitWidth() + 10);
        //DISABLE:
        imageView.setDisable(true);
        //ADD CHILDREN:
        getChildren().addAll(imageView, label);
    }
    public void setIsSelectable(boolean isSelectable){
        if (isSelectable) {
        	getStyleClass().add("pane-selectable");
        	getStyleClass().remove("pane-unselectable");
        }
        else {
        	getStyleClass().add("pane-unselectable");
        	getStyleClass().remove("pane-selectable");
        }
    }
}
