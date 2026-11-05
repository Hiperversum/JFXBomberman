package model.world.components.entity.objects.powerup;

import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.engine.Animated;
import model.loader.resources.Resource;
import model.loader.resources.PowerUpResources;
import model.managers.AnimationManager;
import model.managers.AudioManager;
import model.managers.EntityManager;
import model.world.components.entity.EntityBox;
import model.world.components.entity.character.Player;
import model.world.components.entity.objects.Objects;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public abstract class PowerUp extends Objects implements Animated {

    public enum Type{
        HEARTH,EXTRA_BOMB,EXPLOSION_EXPANDER,INCREASE_TIME,
        APPLE,RICE_BALL,FROZEN_POP,ICE_CREAM_CONE,CUP_AND_BALL,CAKE;
        public static Stream<Type> stream() {
            return Arrays.stream(Type.values());

        }
        private static final Random random = new Random();
        public static Type getRandomType() {
            Type[] values = Type.values();
            return values[random.nextInt(values.length)];
        }
    }
    private final int ANIMATION_DURATION = 350;

    protected int POINTS = 75;
    private static final PowerUpResources powerUpResources = (PowerUpResources) Resource.getResource(Resource.Type.POWER_UP);
    private static final PowerUpFactory powerUpFactory = new PowerUpFactory();
    protected transient Image[] frames;
    private static Animation animation;
    private static int indexCurrentImage;

    //CONSTRUCTOR:
    public PowerUp(int y,int x,Type type){
        super(y,x);
        this.frames = powerUpResources.getPowerUpResources(type);
        initialize();
    }
    //METHODS:
    private void initialize(){
        AnimationManager.getInstance().addAnimated(this);
        EntityManager.getInstance().addEntity(this);
        entityBox = new EntityBox(this);
        entityBox.setEntityRectangleDimension(tileSize/2,tileSize/2);
        entityBox.setEntityRectangleCoordinates();
        setAnimation();


    }
    public abstract void collectPowerUp();
    public static PowerUp getRandomPowerUp(int y,int x){
        return powerUpFactory.createPowerUp(Type.getRandomType(),y,x);
    }

    @Override
    public void updateByGameLoop(long currentTime) {
        if (entityBox.getEntityRectangle().intersects(Player.getInstance().getEntityBox().getEntityRectangle())){
            destroy();
        }
        if (indexCurrentImage<frames.length) setImage(frames[indexCurrentImage]);
    }
    @Override
    public void destroy() {
        AudioManager.getInstance().playSound(AudioManager.SoundType.ITEM);
        AnimationManager.getInstance().removeAnimated(this);
        EntityManager.getInstance().removeEntity(this);
        collectPowerUp();
    }
    @Override
    public void setAnimation() {
        if(animation == null){
            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(ANIMATION_DURATION));
                }
                protected void interpolate(double frac) {
                    indexCurrentImage = (int) (frac * (frames.length));
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












