package view.components.fxmlmodifier.safepane;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import view.components.fxmlmodifier.FXMLComponentModifier;

public abstract class SafePane extends FXMLComponentModifier {

    protected Pane safePane;
    protected Label labelObjToDelete;
    protected Button backButton;

    public SafePane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
    }

    public void hideSafePane(){
        safePane.setVisible(false);
    }
}
