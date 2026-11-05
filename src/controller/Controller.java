package controller;

import javafx.event.Event;
import model.UI;
import model.editor.LevelEditor;
import model.editor.WorldHandler;
import model.engine.Engine;
import model.managers.*;
import view.EditorView;
import view.GameView;
import view.MenuView;

public abstract class Controller {
    //VIEW:
    protected MenuView menuView = MenuView.getInstance();
    protected GameView gameView = GameView.getInstance();
    protected EditorView editorView = EditorView.getInstance();

    //MODEL:
    protected AnimationManager animationManager = AnimationManager.getInstance();
    protected UserManager userManager = UserManager.getInstance();
    protected EntityManager entityManager = EntityManager.getInstance();
    protected MapManager mapManager = MapManager.getInstance();
    protected WorldHandler worldHandler = WorldHandler.getInstance();
    protected LevelEditor levelEditor = LevelEditor.getInstance();

    protected Engine engine = Engine.getInstance();

    /////////////////////////////////////
    public  void setEvent(Event event) {
        eventHandler(event,null);
    }
    public  void setEvent(Event event,Object obj) {
        eventHandler(event,obj);
    }
    protected abstract void eventHandler(Event event,Object obj);

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //OBSERVER OBSERVABLE:
    //REGISTER:
	protected void registersObserversOfTheMenuView(){
        userManager.addObserver(menuView);
        userManager.addObserver(menuView.getUserMenu());
        userManager.addObserver(menuView.getStatsMenu());
        worldHandler.addObserver(menuView.getWorldMenu());
        ///UPDATE MENU:
        UserManager.getInstance().notifyView(UserManager.UpdateType.LOAD_USER);
    }
	protected void registersObserversOfTheGameView(){
        entityManager.addObserver(gameView);
        mapManager.addObserver(gameView);
        EventManager.getInstance().addObserver(gameView);
        UI.getInstance().addObserver(gameView);
    }
	protected void registersObserversOfTheEditorView(){
        worldHandler.addObserver(editorView.getWorldMenu());
        worldHandler.addObserver(editorView.getWorldMenu().getSaveWorldPane());
        levelEditor.addObserver(editorView.getLevelMenu());
        levelEditor.addObserver(editorView.getLevelMenu().getSaveLevelPane());
        levelEditor.addObserver(editorView.getWorldMenu());
        levelEditor.addObserver(editorView);
        levelEditor.getCursor().addObserver(editorView);
    }
    //UNREGISTER:

	protected void unregistersObserversOfTheMenuView(){
        userManager.deleteObserver(menuView);
        userManager.deleteObserver(menuView.getUserMenu());
        worldHandler.deleteObserver(menuView.getWorldMenu());
        userManager.deleteObserver(menuView.getStatsMenu());
    }
	protected void unregistersObserversOfTheGameView(){
        entityManager.deleteObserver(gameView);
        mapManager.deleteObserver(gameView);
        EventManager.getInstance().deleteObserver(gameView);
        UI.getInstance().deleteObserver(gameView);
    }
	protected void unregistersObserversOfTheEditorView(){
        if (editorView.getWorldMenu()!=null) {
            worldHandler.deleteObserver(editorView.getWorldMenu());
            worldHandler.deleteObserver(editorView.getWorldMenu().getSaveWorldPane());
            levelEditor.deleteObserver(editorView.getWorldMenu());
        }
        if (editorView.getLevelMenu()!=null) {
            levelEditor.deleteObserver(editorView.getLevelMenu());
            levelEditor.deleteObserver(editorView.getLevelMenu().getSaveLevelPane());
        }
        levelEditor.deleteObserver(editorView);
        levelEditor.getCursor().deleteObserver(editorView);
    }
}
