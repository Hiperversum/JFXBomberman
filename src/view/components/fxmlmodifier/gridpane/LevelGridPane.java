package view.components.fxmlmodifier.gridpane;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import model.world.Level;
import view.components.fxmlmodifier.safepane.LevelSafePane;
import view.components.stackpane.CustomStackPane;
import view.tools.SelectablePaneManager;

import java.util.Collection;


public class LevelGridPane extends GridPaneModifier {
    private double cellWidth, cellHeight;
    private LevelSafePane levelSafePane;
    public LevelGridPane(FXMLLoader fxmlLoader, GridPane gridPane ) {
        super(fxmlLoader);
        this.gridPane = gridPane;
        this.levelSafePane = new LevelSafePane(fxmlLoader);
        initialize();
    }
    private void initialize() {
        cols = gridPane.getColumnCount();
        cellWidth = gridPane.getPrefWidth()/cols;
        cellHeight = 178;
    }
    public void initializeLevelGridPane(Collection<Level> levels){
        y = 0;
        x = 0;
        gridPane.getChildren().clear();
        gridPane.setPrefHeight(cellHeight);
        levels.stream().forEach(level -> addCell(level.getLevelName(), level.getImage()));
    }
    public LevelSafePane getLevelSafePane() {
        return levelSafePane;
    }

    private void addCell(String levelName, Image levelPreview){
        if (x >= cols) {
            x = 0;
            gridPane.getRowConstraints().add(new RowConstraints());
            gridPane.setPrefHeight(gridPane.getPrefHeight() + cellHeight);
            y++;
        }
        LevelGridPaneCell levelGridPaneCell = new LevelGridPaneCell(cellWidth, cellHeight,levelName,levelPreview);
        gridPane.add(levelGridPaneCell, x, y);
        //EVENTS:
        SelectablePaneManager.setEvents(levelGridPaneCell,mouseEvent -> {
            controller.setEvent(mouseEvent);
        });
        levelGridPaneCell.setDeleteLabelOnMouseClicked(mouseEvent -> {
            controller.setEvent(mouseEvent);
            levelSafePane.showLevelSafePane(levelName,levelPreview);
        });
        x++;


    }

    public class LevelGridPaneCell extends CustomStackPane {
        private String levelName;
        private Image levelPreview;
        private Label deleteLabel;
        public LevelGridPaneCell(double width, double height, String levelName, Image levelPreview) {
            super(width,height);
            this.levelName = levelName;
            this.levelPreview = levelPreview;
            initialize();
        }

        private void initialize() {
            setId(levelName);
            getStyleClass().add("pane-selectable");
            ImageView imgV = new ImageView(levelPreview);
            imgV.setFitHeight(136);
            imgV.setFitWidth(211);

            Label label = new Label(levelName);
            label.setStyle("-fx-font-family: 'Gill Sans Ultra Bold'; -fx-text-fill: #ffffff;");

            HBox hbox = new HBox();
            deleteLabel = new Label("X");
            deleteLabel.setStyle("-fx-font-family: 'Press Start 2P'; -fx-text-fill: #ffffff;");

            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            hbox.setAlignment(Pos.CENTER_RIGHT);
            hbox.getChildren().add(deleteLabel);
            vbox.getChildren().addAll(hbox, imgV, label);
            getChildren().addAll(vbox);
        }

        //EVENTS:
        public void setDeleteLabelOnMouseClicked(EventHandler<? super MouseEvent> event) {
            deleteLabel.setOnMouseClicked(event);
        }
    }

}
