package model.events;
import model.engine.Engine;
import model.managers.AnimationManager;
import model.managers.AudioManager;
import model.managers.EntityManager;
import model.managers.UserManager;
import model.user.Stats;

public class GameOverEvent extends Events{
    public GameOverEvent() {
        customEvent();
    }
    @Override
    public void customEvent() {
    	Engine.getGameLoop().stop();
        PauseEvent.setBlockPause(true);
        AnimationManager.getInstance().clear();
        EntityManager.getInstance().clear();
        AudioManager.getInstance().stopMusic();
        AudioManager.getInstance().stopSound();
        AudioManager.getInstance().playSound(AudioManager.SoundType.GAME_OVER);
        AudioManager.getInstance().setBlockSound(true);
        Stats stats = UserManager.getInstance().getUserLoaded().getStats();
        stats.increaseGamesLost();
        stats.saveScore();
    }
}
