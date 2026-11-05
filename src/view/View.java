package view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import model.managers.FileManager;

import java.io.IOException;
import java.net.URL;
import java.util.Observable;
import java.util.Observer;

public abstract class View implements Observer {
    //CAMPI:
    protected final int tileSize = 48;
    protected static Stage window;

    protected static Scene scene;
    //COSTRUTTORE:
    protected View()
    {
        if (window == null) window = new Stage();
    }
    //METODI:
    public void startView()  {
        MenuView.getInstance().load();
        initWindow();
    }
    private void initWindow()
    {	
    	window.setX(0);
    	window.setY(0);
        window.setResizable(false);
        window.setTitle("JBomberman");
        window.setOnCloseRequest(event -> {
            event.consume();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Logout");
            alert.setHeaderText("Are you sure you want to close the program?");
            if (alert.showAndWait().get() == ButtonType.OK){
                FileManager.saveProgramData();
                Platform.exit();
            }
        });
    }
    public Stage getWindow() {return window;}
    protected void loadScene(FXMLLoader fxmlLoader)
    {
        try {
            if (scene == null) scene = new Scene(fxmlLoader.load());
            else scene.setRoot(fxmlLoader.load());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        window.setScene(scene);
        window.show();

    }
    public abstract void load() throws IOException;
    protected FXMLLoader setFXML(String pathFileFXML)
    {
        URL fxmlLocation = getClass () . getResource ( pathFileFXML ) ;
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        return fxmlLoader;
    }
    @Override
    public abstract void update(Observable o, Object arg);

}
