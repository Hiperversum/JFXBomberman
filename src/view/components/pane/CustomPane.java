package view.components.pane;
import controller.Controller;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public abstract class CustomPane extends Pane {

    protected FXMLLoader fxmlLoader;
    protected Controller controller;
    public CustomPane(){}

    public CustomPane(FXMLLoader fxmlLoader){
        this.fxmlLoader = fxmlLoader;
        controller = fxmlLoader.getController();
    }
    public CustomPane(double width, double height) {
        setPrefHeight(height);
        setPrefWidth(width);
    }

}
