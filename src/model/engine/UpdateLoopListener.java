package model.engine;

@FunctionalInterface
public interface UpdateLoopListener {
     void updateByGameLoop(long currentTime);
}
