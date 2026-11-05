package model.events;

import model.engine.Engine;
import model.managers.AnimationManager;

public class PauseEvent extends Events {
    private static boolean blockPause;
    private static boolean pause;
    public PauseEvent() {
        customEvent();
    }
    public static void setPause(boolean pause){
        PauseEvent.pause = pause;
    }
    public static void setBlockPause(boolean blockPause) {
        PauseEvent.blockPause = blockPause;
    }
    public static boolean isPause() {
        return pause;
    }
    public static boolean isBlockPause() {
        return blockPause;
    }

    @Override
    public void customEvent() {
        if (!blockPause) {
            pause = isPause() ? false : true;
            if (pause) {
            	Engine.getGameLoop().stop();
            	AnimationManager.getInstance().pauseAnimation();
            }
            else Engine.getGameLoop().start();
        }
    }
}
