package view.tools;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.function.Consumer;

public class SelectablePaneManager {
    private static Pane selectedPane;
    public static void setEvents(Pane pane,  Consumer<MouseEvent> eventHandler ){
        //EVENTS:
        pane.setOnMouseEntered(mouseEvent ->  {
            if (selectedPane != pane && !pane.getStyleClass().contains("pane-unselectable")) pane.getStyleClass().add("hover");
        });
        pane.setOnMouseExited(mouseEvent -> {
            if (selectedPane != pane && !pane.getStyleClass().contains("pane-unselectable") ) pane.getStyleClass().remove("hover");
        });
        pane.setOnMouseClicked(mouseEvent -> {
        	if(!pane.getStyleClass().contains("pane-unselectable")){
	            eventHandler.accept(mouseEvent);
	            if (selectedPane != null) selectedPane.getStyleClass().remove("selected");
	            if (selectedPane != pane) {
	                selectedPane = pane;
	                selectedPane.getStyleClass().add("selected");
	            } else selectedPane = null;
	        	}
        });
    }
}
