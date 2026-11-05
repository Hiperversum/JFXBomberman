package controller;
import javafx.event.Event;
import model.events.PauseEvent;
import model.managers.AnimationManager;
import model.managers.AudioManager;
import view.GameView;


public class GameController extends Controller {
    //METHODS://///////////////////////////////////////////////////
    public void initialize() {
        unregistersObserversOfTheMenuView();
        PauseEvent.setPause(false);
        new KeyboardController(GameView.getInstance());
    }
    public void restartWorld(){
    	AudioManager.getInstance().setBlockSound(false);
    	AnimationManager.getInstance().stopAnimation();
        mapManager.restartWorld();
        PauseEvent.setPause(false);
    }
    public void exit() {
    	AudioManager.getInstance().setBlockSound(false);
    	AnimationManager.getInstance().stopAnimation();
        menuView.load();
        registersObserversOfTheMenuView();
        menuView.back();
    }

    //EVENTS: (CALLBACK)
    @Override
    protected void eventHandler(Event event, Object o) {}
}
