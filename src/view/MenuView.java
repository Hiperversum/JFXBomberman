package view;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.Model;
import model.managers.UserManager;
import view.menu.StatsMenu;
import view.menu.UserMenu;
import view.menu.WorldMenu;



import java.util.Observable;

public class MenuView extends View
{
    //CAMPI:////////////////////////////////////////////////////////
    private static MenuView instance;
    private final String pathFileFXML = "/fxml/JBMenu.fxml";
    private final String pathFileCSS = "/css/MenuStyle.css";
    private FXMLLoader fxmlLoader;
    private Pane mainPane, firstMenuPane, menuPane,editUserPane,loadUserPane,
            userSafePane,settingsPane,statsPane,customLevelsPane;

    private Button continueButton,loadGameButton,okButtonLoadUser;
    private UserMenu userMenu;
    private WorldMenu worldMenu;
    private StatsMenu statsMenu;

    //COSTRUTTORE://///////////////////////////////////////////////
    private MenuView(){}

    public static MenuView getInstance()
    {
        if(instance == null) instance = new MenuView();
        return instance;
    }

    @Override
    public void load() {
        fxmlLoader = setFXML(pathFileFXML);
        loadScene(fxmlLoader);
        initialize();
        initializeComponents();
        initializeAesthetics();
    }
    public void back(){
        firstMenuPane.setVisible(false);
        menuPane.setVisible(true);
    }
    private void initialize()
    {
        window.setWidth(832);
        getWindow().getScene().getStylesheets().clear();
        getWindow().getScene().getStylesheets().add(pathFileCSS);
    }

    private void initializeAesthetics()
    {
        cloudAnimationDX((ImageView) fxmlLoader.getNamespace().get("cloud"), Duration.seconds(80));
        cloudAnimationSX((ImageView) fxmlLoader.getNamespace().get("cloud2"), Duration.seconds(130));
        cloudAnimationSX((ImageView) fxmlLoader.getNamespace().get("cloud3"), Duration.seconds(100));
        cloudAnimationSX((ImageView) fxmlLoader.getNamespace().get("cloud4"), Duration.seconds(180));
        cloudAnimationDX((ImageView) fxmlLoader.getNamespace().get("cloud5"), Duration.seconds(120));
        cloudAnimationSX((ImageView) fxmlLoader.getNamespace().get("cloud6"), Duration.seconds(90));

    }

    private void initializeComponents(){
        userMenu = new UserMenu(fxmlLoader);
        worldMenu = new WorldMenu(fxmlLoader);
        statsMenu = new StatsMenu(fxmlLoader);
        mainPane = (Pane) fxmlLoader.getNamespace().get("mainPane");
        firstMenuPane = (Pane) fxmlLoader.getNamespace().get("firstMenuPane");
        menuPane = (Pane) fxmlLoader.getNamespace().get("menuPane");
        editUserPane = (Pane) fxmlLoader.getNamespace().get("editUserPane");
        loadUserPane = (Pane) fxmlLoader.getNamespace().get("loadUserPane");
        userSafePane = (Pane) fxmlLoader.getNamespace().get("userSafePane");
        settingsPane = (Pane) fxmlLoader.getNamespace().get("settingsPane");
        statsPane = (Pane) fxmlLoader.getNamespace().get("statsPane");
        customLevelsPane = (Pane) fxmlLoader.getNamespace().get("customLevelsPane");
        continueButton = (Button) fxmlLoader.getNamespace().get("continueButton");
        loadGameButton = (Button) fxmlLoader.getNamespace().get("loadGameButton");
        okButtonLoadUser = (Button) fxmlLoader.getNamespace().get("okButtonLoadUser");

    }
    //METHODS:://///////////////////////////////////////////////////
    private void initializeButton(UserManager userManager){
        continueButton.setDisable(userManager.getUserLoaded() != null ? false : true);
        loadGameButton.setDisable(userManager.getUserMap().isEmpty() ? true: false);
    }
    //SHOW METHODS:
    public void showMenu(){
        setInvisibleAllPane();
        menuPane.setVisible(true);
    }
    public void showLoadMenu() {
        setInvisibleAllPane();
        loadUserPane.setVisible(true);
    }
    public void showSettingsPane() {
        settingsPane.setVisible(!settingsPane.isVisible());
    }
    //OTHER:
    public void setInvisibleAllPane(){
        firstMenuPane.setVisible(false);
        menuPane.setVisible(false);
        editUserPane.setVisible(false);
        loadUserPane.setVisible(false);
        userSafePane.setVisible(false);
        settingsPane.setVisible(false);
        statsPane.setVisible(false);
        customLevelsPane.setVisible(false);
    }
    //GETTERS:
    public UserMenu getUserMenu() {
        return userMenu;
    }
    public WorldMenu getWorldMenu() {
        return worldMenu;
    }
    public StatsMenu getStatsMenu() {
        return statsMenu;
    }

    //ANIMATIONS:
    public void cloudAnimationDX(ImageView imgV, Duration duration){
        double wScene = mainPane.getPrefWidth();
        TranslateTransition transition = new TranslateTransition(duration, imgV);
        transition.setFromX(0);
        transition.setToX(wScene - imgV.getLayoutX());
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }
    public void cloudAnimationSX(ImageView imgV, Duration duration){
        double wScene = mainPane.getPrefWidth();
        TranslateTransition transition = new TranslateTransition(duration, imgV);
        transition.setFromX(0);
        transition.setToX(-wScene);
        transition.setCycleCount(TranslateTransition.INDEFINITE);
        transition.setAutoReverse(true);
        transition.play();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof UserManager){
            UserManager userManager = (UserManager) o;
            if (arg == UserManager.UpdateType.NEW_USER) {
                setInvisibleAllPane();
                editUserPane.setVisible(true);
            }
            if (arg == UserManager.UpdateType.LOAD_USERS_MENU) {
                okButtonLoadUser.setDisable(true);
                showLoadMenu();
            }
            if (arg == UserManager.UpdateType.MENU) {
                initializeButton((UserManager)o);
                setInvisibleAllPane();
                firstMenuPane.setVisible(true);
            }
            if (arg == UserManager.UpdateType.LOAD_USER){
                okButtonLoadUser.setDisable(userManager.getUserSelected()!=null ? false: true);
            }
        }
    }



}
