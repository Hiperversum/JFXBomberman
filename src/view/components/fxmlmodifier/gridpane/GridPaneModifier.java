package view.components.fxmlmodifier.gridpane;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
import view.components.fxmlmodifier.FXMLComponentModifier;

public abstract class GridPaneModifier extends FXMLComponentModifier {
    protected GridPane gridPane;
    protected int cols,y,x;
    public GridPaneModifier(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
    }

}
