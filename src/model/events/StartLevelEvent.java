package model.events;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.engine.Engine;
import model.managers.AnimationManager;
import model.managers.AudioManager;


public class StartLevelEvent extends Events {
    private static int stageCounter;

    public StartLevelEvent() {
        customEvent();
    }
    public static void  resetStageCounter(){
        stageCounter = 0;
    }
    public StartLevelEvent increaseStageCounter(){
        stageCounter+=1;
        return this;
    }
    public static String getStageCounter() {
        return Integer.toString(stageCounter);
    }

    @Override
    public void customEvent() {
        AudioManager.getInstance().stopSound();
        AudioManager.getInstance().playSound(AudioManager.SoundType.STAGE_INTRO,() -> {
            AudioManager.getInstance().playMusic();
        });
        AnimationManager.getInstance().clear();
        Engine.getGameLoop().stop();
        PauseEvent.setBlockPause(true);
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
        	Engine.getGameLoop().start();
            PauseEvent.setBlockPause(false);
        }));
        timeline.play();
    }
}
