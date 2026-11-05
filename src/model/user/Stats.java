package model.user;
import model.UI;
import model.managers.MapManager;
import java.io.Serializable;

public class Stats implements Serializable {
    //Match Statistics
    private int bestScore;
    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;


    //METHODS:
    public void setBestScore(int bestScore) {
        this.bestScore = bestScore;
    }
    public int getBestScore() {
        return bestScore;
    }
    public int getGamesPlayed() {
        return gamesPlayed;
    }
    public int getGamesWon() {
        return gamesWon;
    }
    public int getGamesLost() {
        return gamesLost;
    }

    //OTHERS:
    private void increaseGamesPlayed(){
        if (MapManager.getInstance().isNormalMode())
        gamesPlayed++;
    }
    public void increaseGamesWon(){
        if (MapManager.getInstance().isNormalMode()) {
            gamesWon++;
            increaseGamesPlayed();
        }
    }
    public void increaseGamesLost(){
        if (MapManager.getInstance().isNormalMode()) {
            gamesLost++;
            increaseGamesPlayed();
        }
    }
    public void saveScore(){
        int gameScore = UI.getInstance().getGameScore();
        if (MapManager.getInstance().isNormalMode() &&
                bestScore<gameScore) setBestScore(gameScore);
    }
}
