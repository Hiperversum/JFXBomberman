package model.engine;

public interface Animated {
	
    void setAnimation();
    void play(long time);
    void pause();
    void stop();
}
