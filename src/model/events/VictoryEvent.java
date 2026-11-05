package model.events;

import model.engine.Engine;
import model.managers.AnimationManager;
import model.managers.AudioManager;
import model.managers.EntityManager;
import model.managers.UserManager;
import model.user.Stats;

public class VictoryEvent extends Events{
    public VictoryEvent() {
        customEvent();
    }

    @Override
    public void customEvent() {
    	 Engine.getGameLoop().stop();
        PauseEvent.setBlockPause(true);
        AnimationManager.getInstance().clear();
        EntityManager.getInstance().clear();
        AudioManager.getInstance().stopSound();
        AudioManager.getInstance().playSound(AudioManager.SoundType.VICTORY);
        AudioManager.getInstance().setBlockSound(true);
        Stats stats = UserManager.getInstance().getUserLoaded().getStats();
        stats.increaseGamesWon();
        stats.saveScore();
    }
}
