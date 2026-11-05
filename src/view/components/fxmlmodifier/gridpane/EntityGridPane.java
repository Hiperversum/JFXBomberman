package view.components.fxmlmodifier.gridpane;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import view.components.stackpane.CustomStackPane;
import view.tools.SelectablePaneManager;
import java.util.Map;

public class EntityGridPane extends GridPaneModifier {

    public EntityGridPane(FXMLLoader fxmlLoader) {
        super(fxmlLoader);
        initialize();
    }


    private void initialize(){
        gridPane = (GridPane) fxmlLoader.getNamespace().get("entityGridPane");
        cols = gridPane.getColumnCount();
    }

    public void initializeEntityGridPane(Map<String, Image> icoMap ){
        y = 0;
        x = 0;
        gridPane.getChildren().clear();
        gridPane.setPrefHeight(0);
        icoMap.forEach((key, image) -> addCell(key,image));
    }

    private void addCell(String key,Image image){
        if (x >= cols) {
            x = 0;
            gridPane.getRowConstraints().add(new RowConstraints());
            gridPane.setPrefHeight(gridPane.getPrefHeight() + image.getHeight());
            y++;
        }
        EntityGridPaneCell entityGridPaneCell = new EntityGridPaneCell(key,image);
        gridPane.add(entityGridPaneCell, x, y);
        GridPane.setHalignment(entityGridPaneCell, HPos.CENTER);
        GridPane.setValignment(entityGridPaneCell, VPos.CENTER);
        SelectablePaneManager.setEvents(entityGridPaneCell,mouseEvent -> {
            controller.setEvent(mouseEvent);
        });
        x++;
    }

    public class EntityGridPaneCell extends CustomStackPane {
        private Image entityPreview;
        public EntityGridPaneCell(String id,Image entityPreview) {
            this.entityPreview = entityPreview;
            setId(id);
            getStyleClass().add("pane-selectable");
            initialize();
        }

        private void initialize() {
            ImageView imgV = new ImageView(entityPreview);
            setPrefWidth(imgV.getFitWidth());
            setPrefHeight(imgV.getFitHeight());
            getChildren().add(imgV);
        }
    }
}
