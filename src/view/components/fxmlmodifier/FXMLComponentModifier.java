package view.components.fxmlmodifier;

import controller.Controller;
import javafx.fxml.FXMLLoader;

public class FXMLComponentModifier {

    protected FXMLLoader fxmlLoader;

    protected Controller controller;
    public FXMLComponentModifier(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
        controller = fxmlLoader.getController();
    }
}
