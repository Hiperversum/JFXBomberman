package model;
import model.engine.UpdateLoopListener;
import model.events.GameOverEvent;
import model.managers.EventManager;


public class UI extends Model implements UpdateLoopListener {
    private final int TIME = 130;
    private static UI instance;
    private int gameScore;
    private int lives;
    private double playTime;
    private boolean playtimeIsStopped;
    //CONSTRUCTOR:
    private UI(){}

    public static UI getInstance(){
        if(instance == null) instance = new UI();
            return instance;
    }
    //METHODS:
    public void resetUI(){
        resetPlayTime();
        gameScore = 0;
        lives = 3;
        setChanged();
        notifyObservers();
    }
    public void addLife(){
        lives++;
        setChanged();
        notifyObservers();
    }
    public void removeLife(){
        if (lives>0) {
            lives--;
            setChanged();
            notifyObservers();
            removePointsFromTheScore();
        } else EventManager.getInstance().createEvent(new GameOverEvent());
    }

    public void addPointsToTheScore(int points){
        gameScore+=points;
        setChanged();
        notifyObservers();
    }
    public void removePointsFromTheScore(){
        double pointToremove = gameScore*0.12;
        gameScore-=pointToremove;
        setChanged();
        notifyObservers();
    }
    public void resetPlayTime(){
        playTime = TIME;
        playtimeIsStopped = false;}
    public void increasePlayTime(){
        playTime+=15;
    }
    public void stopPlayTime(){
        playtimeIsStopped = true;
    }

    //GETTERS:
    public double getPlayTime() {
        return playTime;
    }
    public int getGameScore() {
        return gameScore;
    }
    public int getLives() {
        return lives;
    }
    @Override
    public void updateByGameLoop(long currentTime) {
        if (!playtimeIsStopped)
        playTime = playTime-(double) 1/100;
        setChanged();
        notifyObservers();
        if (playTime <= 0) EventManager.getInstance().createEvent(new GameOverEvent());

    }
}
