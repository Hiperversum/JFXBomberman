package view.components.savepane;

import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import view.components.fxmlmodifier.FXMLComponentModifier;

import java.util.Observer;



public abstract class SavePane extends FXMLComponentModifier implements Observer {
    protected Pane savePane;
    protected Label warningLabel;
    protected Button saveButton,backButton;
    protected TextField textField;
    public SavePane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
    }
    public TextField getTextField() {
        return textField;
    }

    protected abstract void checkTextField();
    protected void handleName(Controller controller){
        if (textField!=null) {
            checkTextField();
            textField.textProperty().addListener((obs, oldText, newText) -> {
                if (newText.length() > 9) {
                    textField.setText(oldText);
                } else {

                    String filteredText = newText.replaceAll("[^a-zA-Z0-9 -]", "");
                    textField.setText(filteredText);
                    checkTextField();
                    controller.setEvent(null,this);
                }

            });

        }

    }

}
