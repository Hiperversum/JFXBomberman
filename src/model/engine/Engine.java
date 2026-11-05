package model.engine;

import model.Model;
import model.UI;
import model.editor.LevelEditor;
import model.managers.*;
import model.world.components.entity.character.Player;

import java.util.ArrayList;
import java.util.List;

public class Engine extends Model implements Runnable, UpdateLoopListener {
    //CAMPI:////////////////////////////////////////////////////////
    public enum GameState { MENU,GAME,EDITOR; }
    private static GameLoop gameLoop;
    private Thread gameThread;
    private static GameState GAME_STATE;
    private static Engine instance;
    private List<UpdateLoopListener> updateableObjects;

    //COSTRUTTORE://////////////////////////////////////////////////
    private Engine()
    {
        updateableObjects = new ArrayList<>();
        gameLoop = new GameLoop(this);
        gameThread = new Thread(this);
        gameThread.start();


    }
    public static Engine getInstance()
    {
        if(instance == null) instance = new Engine();
        return instance;
    }
    //METODI:///////////////////////////////////////////////////////    
    
    public static GameLoop getGameLoop() {
		return gameLoop;
	}
	public void setGameState(GameState newGameState) {
        GAME_STATE = newGameState;
        gameChangeManager();
    }

    private void gameChangeManager() {
        updateableObjects.clear();
        AudioManager.getInstance().stopSound();
        AudioManager.getInstance().stopMusic();
        switch (GAME_STATE) {
            case GAME: {
                gameLoop.start();
                updateableObjects.add(MapManager.getInstance());
                updateableObjects.add(Player.getInstance());
                updateableObjects.add(EntityManager.getInstance());
                updateableObjects.add(AnimationManager.getInstance());
                updateableObjects.add(UI.getInstance());
                break;
            }
            case EDITOR: {
            	gameLoop.start();
                updateableObjects.add(LevelEditor.getInstance());
                break;
            }
            case MENU: {
                AudioManager.getInstance().stopSound();
                AudioManager.getInstance().playSound(AudioManager.SoundType.MENU);
                gameLoop.stop();
                break;
            }
        }
        //updateableObjects.forEach(System.out::println);
    }

    @Override
    public void run() {
        gameLoop.start();
    }
    
    @Override
    public void updateByGameLoop(long currentTime) {
        updateableObjects.forEach( updateLoopListener -> updateLoopListener.updateByGameLoop(currentTime));
    }


}

























