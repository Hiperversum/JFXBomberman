package view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import model.UI;
import model.events.GameOverEvent;
import model.events.PauseEvent;
import model.events.StartLevelEvent;
import model.events.VictoryEvent;
import model.managers.EntityManager;
import model.managers.EventManager;
import model.managers.MapManager;
import model.world.components.tiles.Tile;
import view.tools.Brush;

import java.io.IOException;
import java.util.Observable;

public class GameView extends View
{
    //CAMPI:////////////////////////////////////////////////////////
    private static GameView instance;
    private final String pathFileFXML = "/fxml/JBGame.fxml";
    private FXMLLoader fxmlLoader;
    private Canvas canvas;
    private Brush brush;
    private Label scoreLabel,livesLabel,nextLevelLabel,readyLabel,scoreEndGameLabel,endGameTitle,timeLabel;
    private Pane nextLevelPane,curtainSX,curtainDX,endGamePane,pausePane;

    //COSTRUTTORE://///////////////////////////////////////////////
    private GameView(){

    }
    public static GameView getInstance()
    {
        if(instance == null) instance = new GameView();
        return instance;
    }
    @Override
    public void load() throws IOException {
        fxmlLoader = setFXML(pathFileFXML);
        loadScene(fxmlLoader);
        getWindow().getScene().getStylesheets().clear();
        initialize();
    }
    //METHODS://///////////////////////////////////////////////////
    private void initialize(){
        canvas =  (Canvas) fxmlLoader.getNamespace().get("canvas");
        brush = new Brush(canvas,tileSize);
        //LABEL:
        scoreLabel = (Label)fxmlLoader.getNamespace().get("scoreLabel");
        livesLabel = (Label)fxmlLoader.getNamespace().get("livesLabel");
        nextLevelLabel = (Label)fxmlLoader.getNamespace().get("nextLevelLabel");
        readyLabel = (Label)fxmlLoader.getNamespace().get("readyLabel");
        scoreEndGameLabel = (Label)fxmlLoader.getNamespace().get("scoreEndGameLabel");
        endGameTitle = (Label)fxmlLoader.getNamespace().get("endGameTitle");
        timeLabel = (Label)fxmlLoader.getNamespace().get("timeLabel");
        //PANE:
        nextLevelPane = (Pane)fxmlLoader.getNamespace().get("nextLevelPane");
        curtainSX = (Pane)fxmlLoader.getNamespace().get("curtainSX");
        curtainDX = (Pane)fxmlLoader.getNamespace().get("curtainDX");
        endGamePane = (Pane)fxmlLoader.getNamespace().get("endGamePane");
        pausePane = (Pane)fxmlLoader.getNamespace().get("pausePane");

    }
    private void startLevelAnimation(String stage){
        nextLevelLabel.setText("STAGE "+stage);
        canvas.setVisible(false);
        readyLabel.setText("READY!");
        //TIMELINE CANVAS:
        Timeline showCanvas = new Timeline(new KeyFrame(Duration.seconds(0.1), e -> {
            nextLevelPane.setVisible(true);
            canvas.setVisible(true);
        }));
        showCanvas.play();
        //TRANSITION:
        TranslateTransition transitionSX = new TranslateTransition(Duration.seconds(1), curtainSX);
        transitionSX.setFromX(0);
        transitionSX.setToX(-curtainSX.getPrefWidth());
        TranslateTransition transitionDX = new TranslateTransition(Duration.seconds(1), curtainDX);
        transitionDX.setFromX(0);
        transitionDX.setToX(curtainDX.getPrefWidth());
        //TIMELINE:
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1),null));
        //EVENTS ON FINISHED
        transitionSX.setOnFinished(actionEvent -> {
            readyLabel.setText("GO!");
            timeline.play();
        });;
        timeline.setOnFinished(actionEvent -> nextLevelPane.setVisible(false));
        transitionSX.play();
        transitionDX.play();
    }
    //GETTERS:
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // METODI DRAW:



    @Override
    public void update(Observable o, Object arg) {
        if(o instanceof MapManager) {
            if (arg instanceof Tile[][]) brush.drawMap((Tile[][]) arg);
            if (arg  == MapManager.UpdateType.RESTART_WORLD){
                endGamePane.setVisible(false);
                pausePane.setVisible(false);
            }
        }
        else if(o instanceof EntityManager){
            EntityManager entityManager = (EntityManager)o;
            brush.drawEntity(entityManager.getEntityList());
        }
        else if (o instanceof UI) {
            UI ui = (UI)o;
            scoreLabel.setText(Integer.toString(ui.getGameScore()));
            livesLabel.setText(Integer.toString(ui.getLives()));
            timeLabel.setText(Integer.toString((int) ui.getPlayTime()));
        }
        else if (o instanceof EventManager){
            EventManager eventManager = (EventManager) o;
            if (arg instanceof StartLevelEvent){
                StartLevelEvent levelChangeEvent = (StartLevelEvent) arg;
                startLevelAnimation(levelChangeEvent.getStageCounter());
            }
            else if (arg instanceof GameOverEvent){
                endGameTitle.setText("Game Over");
                scoreEndGameLabel.setText(scoreLabel.getText());
                endGamePane.setVisible(true);
                pausePane.setVisible(false);
            } else if (arg instanceof VictoryEvent) {
                endGameTitle.setText("Victory!");
                scoreEndGameLabel.setText(scoreLabel.getText());
                endGamePane.setVisible(true);
                pausePane.setVisible(false);
            } else if (arg instanceof PauseEvent){
                PauseEvent pauseEvent = (PauseEvent) arg;
                if (!endGamePane.isVisible())
                pausePane.setVisible(pauseEvent.isPause());
            }



        }

    }



}













