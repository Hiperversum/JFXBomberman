package view.menu;

import controller.Controller;
import javafx.fxml.FXMLLoader;

import java.util.Observer;

public abstract class Menu implements Observer {

    protected Controller controller;

    protected FXMLLoader fxmlLoader;

    public Menu(FXMLLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
        this.controller = fxmlLoader.getController();
    }

    public Controller getController() {
        return controller;
    }

}
