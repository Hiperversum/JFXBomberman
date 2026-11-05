package view;
import controller.Controller;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import model.editor.Editor;
import model.editor.LevelEditor;
import model.world.Level;
import model.world.components.Theme;
import model.world.components.tiles.Tile;
import view.components.button.BackgroundButton;
import view.components.button.CustomButton;
import view.components.button.ThemeButton;
import view.components.fxmlmodifier.gridpane.EntityGridPane;
import view.menu.LevelMenu;
import view.menu.WorldMenu;
import view.tools.Brush;
import java.util.Map;
import java.util.Observable;

@SuppressWarnings("deprecation")
public class EditorView extends View {

    //CAMPI:////////////////////////////////////////////////////////
    private static EditorView instance;
    private FXMLLoader fxmlLoader;
    private final String pathFileFXML = "/fxml/JBEditor.fxml";
    private final String pathFileCSS = "/css/EditorStyle.css";
    private LevelMenu levelMenu;
    private WorldMenu worldMenu;
    private EntityGridPane entityGridPane;
    private Label chosenCategoryLabel;
    private VBox vboxWithButtons;
    private ScrollPane scrollPane,scrollPaneWithGrid;
    private Brush brush;
    //COSTRUTTORE://///////////////////////////////////////////////
    private EditorView(){
    }
    public static EditorView getInstance()
    {
        if(instance == null) instance = new EditorView();
        return instance;
    }
    @Override
    public void load() {
        fxmlLoader = setFXML(pathFileFXML);
        loadScene(fxmlLoader);
        initialize();
        initializeComponents();
    }
    private void initialize()
    {;
        window.setWidth(1075);
        getWindow().getScene().getStylesheets().clear();
        getWindow().getScene().getStylesheets().add(pathFileCSS);
    }

    private void initializeComponents(){
        worldMenu = new WorldMenu(fxmlLoader);
        levelMenu = new LevelMenu(fxmlLoader);
        entityGridPane = new EntityGridPane(fxmlLoader);
        brush = new Brush((Canvas)fxmlLoader.getNamespace().get("canvas") ,tileSize);
        chosenCategoryLabel = (Label)fxmlLoader.getNamespace().get("chosenCategoryLabel");
        scrollPane = (ScrollPane)fxmlLoader.getNamespace().get("scrollPane");
        scrollPaneWithGrid = (ScrollPane)fxmlLoader.getNamespace().get("scrollPaneWithGrid");
        vboxWithButtons = (VBox) fxmlLoader.getNamespace().get("vboxWithButtons");

    }
    //METHODS://///////////////////////////////////////////////////
    private void addBackgroundButtons(Theme[] themes){
        vboxWithButtons.getChildren().clear();
        for (Theme theme : themes) {
            CustomButton customButton = new BackgroundButton(theme.toString(),scrollPane.getPrefWidth(),20);
            customButton.setOnAction(event -> {
                ((Controller)fxmlLoader.getController()).setEvent(event);
            });
            vboxWithButtons.getChildren().add(customButton);
        }
    }

    private void addThemeButtons(Theme[] themes){
        vboxWithButtons.getChildren().clear();
        for (Theme theme : themes) {
            CustomButton customButton = new ThemeButton(theme.toString(),scrollPane.getPrefWidth(),20);
            customButton.setOnAction(event->{
                ((Controller)fxmlLoader.getController()).setEvent(event);
            });
            vboxWithButtons.getChildren().add(customButton);
        }

    }

    //GETTERS:
    public WorldMenu getWorldMenu() {
        return worldMenu;
    }
    public LevelMenu getLevelMenu() {
        return levelMenu;
    }

    private void updateHandler(LevelEditor levelEditor, Editor.UpdateType updateType){
        if (updateType!=null){
            switch (updateType){
                case UPDATE_CATEGORY_SELECTED -> {
                    chosenCategoryLabel.setText(levelEditor.getResourceSelected().toString()+":");
                    chosenCategoryLabel.setVisible(true);
                    Map<String, Image> icoMap = levelEditor.getEditorResourceSelected().getIcoMap();
                    if (icoMap!=null)
                        entityGridPane.initializeEntityGridPane(icoMap);
                    scrollPane.setVisible(false);
                    scrollPaneWithGrid.setVisible(true);
                }
                case UPDATE_THEME_SELECTED->{
                    chosenCategoryLabel.setText("Theme:");
                    chosenCategoryLabel.setVisible(true);
                    scrollPane.setVisible(true);
                    scrollPaneWithGrid.setVisible(false);
                    addThemeButtons(levelEditor.getThemes());
                }

                case UPDATE_BACKGROUND_SELECTED->{
                    chosenCategoryLabel.setText(levelEditor.getResourceSelected().toString()+":");
                    chosenCategoryLabel.setVisible(true);
                    scrollPane.setVisible(true);
                    scrollPaneWithGrid.setVisible(false);
                    addBackgroundButtons(levelEditor.getThemes());
                }
            }
        }

    }


    //UPDATE BY MODEL
    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof LevelEditor) {
            LevelEditor levelEditor = (LevelEditor) o;
            Level level = LevelEditor.getInstance().getLevel();
            Tile[][] map = level.getMap();
            if(map!=null)
            brush.drawMap(map);
            brush.drawCursor(levelEditor.getCursor());
            brush.drawEntity(level.getEntityList());
            if (arg instanceof Editor.UpdateType)
                updateHandler(levelEditor, (Editor.UpdateType) arg);

        }
    }


}


























