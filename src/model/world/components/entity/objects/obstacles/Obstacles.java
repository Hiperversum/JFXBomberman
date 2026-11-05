package model.world.components.entity.objects.obstacles;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.engine.Animated;
import model.loader.resources.Resource;
import model.loader.resources.ObstacleResources;
import model.managers.AnimationManager;
import model.managers.MapManager;
import model.utility.RandomUtils;
import model.world.components.Theme;
import model.world.components.entity.objects.Objects;
import model.world.components.entity.objects.Teleport;

import java.io.IOException;
import java.io.ObjectInputStream;


public class Obstacles extends Objects implements Animated {
	private static final long serialVersionUID = -2012924795384999526L;
    private transient final int  ANIMATION_DURATION = 400;
    private transient final int  DESTRUCTION_DURATION = 650;
    private transient Image[] imageFrames;
    private transient Image[] destructionFrames;
    private transient Objects obj;
    private transient Animation animation;
    private Theme theme;
    public Obstacles(Theme theme)
    {
        this.theme = theme;
    }
    public Obstacles(Obstacles obstacle,int y, int x) {
        super(y,x);
        this.theme = obstacle.theme;
        imageFrames = ((ObstacleResources)Resource.getResource(Resource.Type.OBSTACLES)).getAliveFrames(theme);
        destructionFrames = ((ObstacleResources)Resource.getResource(Resource.Type.OBSTACLES)).getDeathFrames(theme);
        collision = true;
        setImage(imageFrames[0]);
        setAnimation();
        AnimationManager.getInstance().addAnimated(this);
    }
    public void setObj(Objects obj) {
        this.obj = obj;
    }

    public Objects getObj() {
        return obj;
    }
    @Override
    public void destroy() {
        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(DESTRUCTION_DURATION));
            }

            protected void interpolate(double frac) {
                int i = (int) (frac * (destructionFrames.length));
                if (i< destructionFrames.length)
                setImage(destructionFrames[i]);
            }
        };

        animation.setOnFinished( actionEvent -> {
            RandomUtils.generateRandomPowerUp(this,getY(),getX());
            AnimationManager.getInstance().removeAnimated(this);
            MapManager.getInstance().getMap()[getY()][getX()].setObject(null);
            if (obj!= null && obj instanceof Teleport) ((Teleport)obj).initialize();
        });

    }
    @Override
    public void setAnimation() {
        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(ANIMATION_DURATION));
            }

            protected void interpolate(double frac) {
                int i = (int) (frac * (imageFrames.length));
                if (i<imageFrames.length)
                setImage(imageFrames[i]);
            }
        };
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
    //CUSTOM SERIALIZED METHOD
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        imageFrames = ((ObstacleResources)Resource.getResource(Resource.Type.OBSTACLES)).getAliveFrames(theme);
        destructionFrames = ((ObstacleResources)Resource.getResource(Resource.Type.OBSTACLES)).getDeathFrames(theme);
        setImage(imageFrames[0]);
        setAnimation();
        AnimationManager.getInstance().addAnimated(this);
    }

    @Override
    public void updateByGameLoop(long currentTime) {

    }

}
