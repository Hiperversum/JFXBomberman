package view.components.stackpane;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class WorldPane extends CustomStackPane {
    private String worldName;
    private Label deleteLabel,nameLabel;
    public WorldPane(double width,double height,String worldName) {
        super(width,height);
        this.worldName = worldName;
        initialize();
    }
    public void canBeDeleted(boolean canBeDeleted){
        deleteLabel.setVisible(canBeDeleted);
    }

    public void setEmpty(){
        nameLabel.setStyle("-fx-font-family: 'Gill Sans Ultra Bold'; -fx-text-fill: #e31c1c; -fx-font-size: 35px;");
    }
    private void initialize() {
        getStyleClass().add("pane-selectable");
        setId(worldName);
        nameLabel = new Label(worldName);
        nameLabel.setStyle("-fx-font-family: 'Gill Sans Ultra Bold'; -fx-text-fill: #ffffff; -fx-font-size: 35px;");
        //HBOX:
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER_RIGHT);
        deleteLabel = new Label("X");
        deleteLabel.setId(worldName);
        deleteLabel.setStyle("-fx-font-family: 'Gill Sans Ultra Bold'; -fx-text-fill: #ffffff; -fx-font-size: 20px;");
        hbox.getChildren().add(deleteLabel);
        //VBOX:
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.TOP_CENTER);
        vBox.getChildren().addAll(hbox, nameLabel);
        getChildren().addAll(vBox);
    }
    //EVENTS:
    public void setDeleteLabelOnMouseClicked(EventHandler<? super MouseEvent> event) {
        deleteLabel.setOnMouseClicked(event);
    }



}


















