package model.managers;
import model.engine.Animated;
import model.engine.UpdateLoopListener;

import java.util.ArrayList;
import java.util.List;

public class AnimationManager implements UpdateLoopListener {
    private static AnimationManager instance;
    private List<Animated> animatedEntity;
    private AnimationManager()
    {
        animatedEntity = new ArrayList<>();
    }

    public static AnimationManager getInstance()
    {
        if(instance == null) instance = new AnimationManager();
        return instance;
    }
    //METHODS:
    public boolean containsAnimated(Animated animatedObject){
        return animatedEntity.contains(animatedObject);
    }
    public void addAnimated(Animated animatedObject) {
        if(!containsAnimated(animatedObject))
        animatedEntity.add(animatedObject);
    }
    public void removeAnimated(Animated animatedObject) {
        animatedEntity.remove(animatedObject);
        animatedObject.pause();
    }
    public void clear()
    {
        animatedEntity.clear();
    }
    public void printAnimatedList(){
        System.out.println("Animated Recorded: ");
        animatedEntity.forEach(System.out::println);
    }
    public void stopAnimation() {
    	animatedEntity.forEach(animated->animated.stop());
    }
    public void pauseAnimation() {
    	animatedEntity.forEach(animated->animated.pause());
    }
    
    @Override
    public void updateByGameLoop(long currentTime) {
        for (Animated animatedObject : animatedEntity) {
            if (animatedObject!=null) {
                animatedObject.play(currentTime);
                //printAnimatedList();
            }
        }
    }
}
