package view.components.stackpane;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public abstract class CustomStackPane extends StackPane {

    public CustomStackPane(){}
    public CustomStackPane(double width, double height) {
        setPrefHeight(height);
        setPrefWidth(width);
    }


}
