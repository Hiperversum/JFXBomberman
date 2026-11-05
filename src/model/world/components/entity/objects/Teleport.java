package model.world.components.entity.objects;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.engine.Animated;
import model.loader.resources.Resource;
import model.loader.resources.TeleportResources;
import model.managers.AnimationManager;
import model.managers.MapManager;


public class Teleport extends Objects implements Animated {

    private final int ANIMATION_DURATION = 200;
    private Image[] frames;
    private Animation animation;
    public Teleport(int y, int x){
        super(y,x);
        frames = ((TeleportResources)Resource.getResource(Resource.Type.TELEPORT)).getFrames();
        setImage(frames[0]);
        AnimationManager.getInstance().addAnimated(this);
        setAnimation();
    }

    public void initialize(){
        MapManager.getInstance().addObjToMap(getY(),getX(),this);
    }
    @Override
    public void updateByGameLoop(long currentTime) {
    }
    @Override
    public void destroy() {

    }
    @Override
    public void setAnimation() {
        if(animation == null){
            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(ANIMATION_DURATION));
                }
                protected void interpolate(double frac) {
                    int i = (int) (frac * (frames.length));
                    if (i<frames.length)
                    setImage(frames[i]);
                }
            };
            animation.setCycleCount(Animation.INDEFINITE);
        }
    }
    @Override
    public void play(long time) {
        animation.play();
    }
    @Override
    public void pause() {
        animation.pause();
    }

	@Override
	public void stop() {
		animation.stop();
		
	}
}
