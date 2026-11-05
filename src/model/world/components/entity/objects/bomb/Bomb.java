package model.world.components.entity.objects.bomb;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.engine.Animated;
import model.engine.Engine;
import model.events.PauseEvent;
import model.loader.resources.Resource;
import model.loader.resources.BombResources;
import model.managers.AnimationManager;
import model.managers.MapManager;
import model.managers.UserManager;
import model.world.components.entity.character.Character;
import model.world.components.entity.character.Player;
import model.world.components.entity.objects.Objects;
import model.world.components.entity.effects.Explosion;
import model.world.components.tiles.Tile;

public class Bomb extends Objects implements Animated  {
    private Animation animation,timeline;
    private static int range;
    private static final double TIME_EXPLOSION = 2.25;
    private Image[] frames;
    private  Character character;
    private Tile[][] map;
    public Bomb(int y, int x, int range, Character character, Tile[][] map) {
        super(y,x);
        this.range = range;
        this.character = character;
        this.map = map;
        collision = true;
        frames = ((BombResources) Resource.getResource(Resource.Type.BOMB)).getBombImages(UserManager.getInstance().getUserLoaded().getBombSkinKey());
        setAnimation();
        MapManager.getInstance().addObjToMap(y,x,this);
        AnimationManager.getInstance().addAnimated(this);
        destroy();

    }
    @Override
    public void setAnimation() {
        animation = new Transition() {
            {
                setCycleDuration(Duration.millis(420));
            }

            protected void interpolate(double frac) {
                int i = (int) (frac * (frames.length));
                if(i<frames.length)
                setImage(frames[i]);
            }
        };

    }
    @Override
    public void play(long time) {
        animation.play();
        if(timeline.getStatus()!=Animation.Status.RUNNING)
        	timeline.play();
    }
    @Override
    public void pause() {
        animation.pause();
        timeline.pause();
    }
    
    @Override
    public void stop() {
        animation.stop();
        timeline.stop();
    }
    
    
    @Override
    public void updateByGameLoop(long currentTime) {}
    @Override
    public void destroy() {
         timeline = new Timeline(new KeyFrame(Duration.seconds(TIME_EXPLOSION), event -> {
            MapManager.getInstance().removeObjFromMap(getY(),getX());
            new Explosion(getY(),getX(),range,map,character);
            if (map != MapManager.getInstance().getMap() && !PauseEvent.isPause())
                Player.getInstance().resetBombsPlaced();
            if(PauseEvent.isPause()) Player.getInstance().resetBombsPlaced();
            
        }));
        timeline.setOnFinished(event->{
        	AnimationManager.getInstance().removeAnimated(this);
        });
        timeline.play();
    }
}
