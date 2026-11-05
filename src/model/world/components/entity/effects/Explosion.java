package model.world.components.entity.effects;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.util.Duration;
import model.loader.resources.Resource;
import model.events.PauseEvent;
import model.loader.resources.ExplosionResources;
import model.managers.AnimationManager;
import model.managers.AudioManager;
import model.managers.MapManager;
import model.world.components.entity.Entity;
import model.world.components.entity.character.Character;
import model.world.components.entity.character.Player;
import model.world.components.entity.character.behavior.DamageBox;
import model.world.components.entity.objects.Destructible;
import model.world.components.entity.objects.Teleport;
import model.world.components.entity.objects.bomb.Bomb;
import model.world.components.tiles.FloorTile;
import model.world.components.tiles.Tile;


public class Explosion extends Entity {


    // Enumeration for different explosion types
    public enum Type {
        CENTER, TOP, LEFT, RIGHT, DOWN, VERTICAL, HORIZONTAL
    }
    private final int ANIMATION_DURATION = 300;
    private int range,y,x;
    private Character character;
    private int height,depth, leftLength, rightLength;
    private Tile[][] map;
    public Explosion(int y, int x, int range,Tile[][] map, Character character){
        this.y = y;
        this.x = x;
        this.range = range;
        this.character = character;
        this.map = map;
        initialiaze();
    }
    
    private void initialiaze() {
    	if(map == MapManager.getInstance().getMap() && !PauseEvent.isPause()) {
         generateExplosion();
         new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
             destroy();
         })).play();
         AudioManager.getInstance().playSound(AudioManager.SoundType.EXPLOSION);
    	}
    }
    public void generateExplosion() {
        calculateDimensions();
        checkDestructibleObjects();
        Effects.addEffect(new ExplosionCell(y, x, Type.CENTER));
        while (height > 0 || depth > 0 || leftLength > 0 || rightLength > 0) {
                //places cells by range
                Type type;
                if (height > 0) {
                    type = range == height ? Type.TOP : Type.VERTICAL;
                    Effects.addEffect(new ExplosionCell(y - height, x, type));
                }
                if (depth > 0) {
                    type = range == depth ? Type.DOWN : Type.VERTICAL;
                    Effects.addEffect(new ExplosionCell(y + depth, x, type));
                }
                if (leftLength > 0) {
                    type = range == leftLength ? Type.LEFT : Type.HORIZONTAL;
                    Effects.addEffect(new ExplosionCell(y, x - leftLength, type));
                }
                if (rightLength > 0) {
                    type = range == rightLength ? Type.RIGHT : Type.HORIZONTAL;
                    Effects.addEffect(new ExplosionCell(y, x + rightLength, type));
                }
                height--; depth--; leftLength--; rightLength--;
        }
    }
    private void calculateDimensions()
    {
        int temp_range = range;
        while (temp_range > 0){
            if (isValidTile(y - height - 1, x)) height++;
            if (isValidTile(y + depth + 1, x)) depth++;
            if (isValidTile(y, x - leftLength - 1)) leftLength++;
            if (isValidTile(y, x + rightLength + 1)) rightLength++;
            temp_range--;
        }
    }

    //CHECK DESTRUCTIBLES:
    private void checkDestructibleObjects(){
        if( height + 1 <=  range && map[y - height - 1][x].getObj() instanceof Destructible) (map[y - height - 1][x].getObj()).destroy();
        if( depth + 1 <= range && map[y + depth + 1][x].getObj() instanceof Destructible) (map[y + depth + 1][x].getObj()).destroy();
        if( leftLength + 1 <=  range && map[y][x - leftLength - 1].getObj() instanceof Destructible) (map[y][x - leftLength - 1].getObj()).destroy();
        if( rightLength + 1<=  range && map[y][x + rightLength + 1].getObj() instanceof Destructible) (map[y][x + rightLength + 1].getObj()).destroy();
    }
    /////////////////////////////////
    private boolean isValidTile(int row, int col) {
        if (row >= 0 && row < map.length && col >= 0 && col < map[0].length) {
            Tile tile = map[row][col];
            return tile instanceof FloorTile && (!tile.hasAnObject() || (tile.hasAnObject() && tile.getObj() instanceof Teleport) || tile.getObj() instanceof Bomb);
        }
        return false;
    }

    // Convert a string to a Type enum value
    public static Type typeFromString(String name) {
        try {
            return Type.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid Type name: " + name);
        }
    }

    @Override
    public void updateByGameLoop(long currentTime) {

    }
    @Override
    public void destroy() {
        if(character instanceof Player)
            ((Player)character).decreasesBombsPlaced();
    }
    private class ExplosionCell extends Effects {
    	
        private Animation animation,animationReversed ,timeline;
        // CONSTRUCTOR: ///////////////////////////////////////////////////////////////////////////////////////////////
        public ExplosionCell(int y, int x, Type type) {
            super(y * Explosion.this.tileSize, x * Explosion.this.tileSize);
            frames = ((ExplosionResources) Resource.getResource(Resource.Type.EXPLOSION)).explosionResource(type);
            damageBox = new DamageBox(y*tileSize,x*tileSize,tileSize,tileSize,character);
            setAnimation();
            AnimationManager.getInstance().addAnimated(this);

        }
        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
        @Override
        public void play(long time) {
            animation.play();
            if(timeline.getStatus()==Animation.Status.PAUSED)
            	timeline.play();
        }
        @Override
        public void pause() {
            animation.pause();
            animationReversed.pause();
            timeline.pause();
        }

        // Set up the animations
        public void setAnimation() {
            animation = createAnimation(false);
            animationReversed = createAnimation(true);
            timeline = new Timeline(new KeyFrame(Duration.millis(ANIMATION_DURATION), event -> {
                removeEffect(this);
                destroy();
            }));


            animation.setOnFinished(actionEvent -> {

                animation = animationReversed;
                timeline.play();
                
            });
        }

        // Create the animation with optional reversal
        private Animation createAnimation(boolean reversed) {
            return new Transition() {
                {
                    setCycleDuration(Duration.millis(ANIMATION_DURATION));
                }

                protected void interpolate(double frac) {
                    int i = (int) (reversed ? (1 - frac) * (frames.length-1) : frac * (frames.length));
                    if(i<frames.length) setImage(frames[i]);
                }
            };
        }

        @Override
        public void updateByGameLoop(long currentTime) {}
		@Override
		public void stop() {
			animation.stop();
            animationReversed.stop();
            timeline.stop();
			
		}

    }

}









