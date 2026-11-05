package model.world.components.entity.character;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.util.Duration;
import model.UI;
import model.events.PauseEvent;
import model.loader.resources.Resource;
import model.loader.resources.PlayerResources;
import model.managers.AudioManager;
import model.managers.MapManager;
import model.managers.UserManager;
import model.world.components.entity.character.behavior.Attacker;
import model.world.components.entity.character.behavior.Behavior;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.objects.bomb.Bomb;
import model.world.components.tiles.Tile;

import java.util.Map;

public class Player extends Character implements Attacker {
    //FIELDS:
    private final int TIME_CHANGE_FRAME = 22;
    private final double SECONDS_OF_INVINCIBILITY = 1.2;
    private final int TIME_ANIMATION = 1000;
    private static Player instance;
    private Map<Behavior,Image[]> playerSelected;
    private Image[] bombSelected;
    private boolean up, left, down, right;
    private boolean onTeleportation;
    //ABILITY:
    private int  numPlaceableBombs = 1;
    private int numBombPlaced;
    private int range = 1;
    //COSTRUTTORI:
    private Player(){
        initialize();
    }

    public static Player getInstance()
    {
        if(instance ==null) instance = new Player();
        return instance;
    }

    //METHODS://///////////////////////////////////////////////////////////
    private void initialize()
    {
        setPlayerResourceSelected(UserManager.getInstance().getUserLoaded().getPlayerSkinKey());
        //POSITION:
        startPosition();
        setSpeed(1);
        //COLLISION BOX:
        getEntityBox().setEntityRectangleDimension(25,20);
        getEntityBox().setEntityRectangleCoordinates();
    }

    //ABILITY METHODS:
    public void resetSkills(){
        startPosition();
        numBombPlaced = 0;
        numPlaceableBombs = 1;
        range = 1;
        
    }

    // Movement: //////////////////////////////////////////////
    public void startPosition(){
        onTeleportation = false;
        int startY = 1 * tileSize;
        int startX = 2 * tileSize;
        setY(startY);
        setX(startX);
        behavior = Behavior.DOWN;
        changeImageFrames();
        getEntityBox().setEntityRectangleCoordinates();
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(SECONDS_OF_INVINCIBILITY), event -> {
                    ((CharaterBox)entityBox).setDamageable(true);

                })
        );
        timeline.play();
    }
    public void movement()
    {
        spriteCounter++;
        if (up || left || down || right) {
            if (up) {
                behavior = Behavior.UP;
            } else if (left) {
                behavior = Behavior.LEFT;
            } else if (down) {
                behavior = Behavior.DOWN;
            } else if (right) {
                behavior = Behavior.RIGHT;
            }
            getEntityBox().setEntityRectangleCoordinates();
            //COLLISION DETECTION
            if (!((CharaterBox) entityBox).CollisionDetection()) {
                switch (behavior) {
                    case UP:
                        setY(getY() - getSpeed());
                        break;
                    case LEFT:
                        setX(getX() - getSpeed());
                        break;
                    case DOWN:
                        setY(getY() + getSpeed());
                        break;
                    case RIGHT:
                        setX(getX() + getSpeed());
                        break;
                }
            }


        } else {
            spriteCounter = TIME_CHANGE_FRAME - 1;
            spriteNum = 0;
        }

    }
    // Bomb: /////////////////////////////////////////////////

    public void increaseBombRange(){
        range++;
    }
    public int getRange() {
        return range;
    }
    public int getNumPlaceableBombs() {
        return numPlaceableBombs;
    }
    public void increasesPlaceableBombs(){
        numPlaceableBombs++;
    }
    public void decreasesBombsPlaced(){
    	if(numBombPlaced>=0)
        numBombPlaced--;
    }
    public void resetBombsPlaced(){
        numBombPlaced = 0;
    }
    public void interact() {
        if (!PauseEvent.isPause() && behavior!=Behavior.NEXT) {
            if (!onTeleportation) attack();
            else nextLevelAnimation();
        }
    }

    @Override
    public void attack() {
        if (numBombPlaced < numPlaceableBombs) {
            Tile[][] map = MapManager.getInstance().getMap();
            int yPlacePosition = ((int) getEntityBox().getEntityRectangleCenter().getY()) / tileSize;
            int xPlacePosition = ((int) getEntityBox().getEntityRectangleCenter().getX()) / tileSize;
            if (map[yPlacePosition][xPlacePosition].getObj() == null) {
                new Bomb(yPlacePosition, xPlacePosition, range, this, MapManager.getInstance().getMap());
                numBombPlaced++;
            }
        }

    }
    // Animations: ///////////////////////////////////////////
    private void nextLevelAnimation(){
        if (behavior!=Behavior.DEATH) {
            PauseEvent.setBlockPause(true);
            int startY = ((int)(entityBox.getEntityRectangleCenter().getY()/tileSize))*tileSize;
            int startX = ((int)(entityBox.getEntityRectangleCenter().getX()/tileSize))*tileSize;
            setY(startY);
            setX(startX);
            behavior = Behavior.NEXT;
            changeImageFrames();
            spriteNum = 0;
            Animation animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(TIME_ANIMATION));
                }

                protected void interpolate(double frac) {
                    spriteNum = (int) (frac * (imageFrames.length));
                }
            };
            UI.getInstance().stopPlayTime();
            AudioManager.getInstance().stopMusic();
            AudioManager.getInstance().playSound(AudioManager.SoundType.STAGE_CLEAR,()->{
                Platform.runLater(()->{
                    startPosition();
                    MapManager.getInstance().nextLevel();
                    }
                );

            });
            animation.play();
        }
    }

    @Override
    public void destroy() {
        if (((CharaterBox)entityBox).isDamageable() && behavior!=Behavior.NEXT) {
            ((CharaterBox)entityBox).setDamageable(false);
            behavior = Behavior.DEATH;
            changeImageFrames();
            spriteNum = 0;
            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(TIME_ANIMATION));
                }

                protected void interpolate(double frac) {
                    spriteNum = (int) (frac * (imageFrames.length));

                }
            };
            animation.setOnFinished(event -> {
                UI.getInstance().removeLife();
                startPosition();
                animation = null;
            });
        }
    }
    
    public void animation()
    {
        if(frameCount!=0) {
            //Calcola spriteNum
            spriteNum = (spriteCounter / TIME_CHANGE_FRAME) % frameCount;

            // Reset spriteCounter se necessario
            if (spriteCounter >= frameCount * TIME_CHANGE_FRAME) {
                spriteCounter = 0;
            }
        }
    }
    @Override
	public void setAnimation() {
    	if (behavior!=Behavior.NEXT) {
            if (behavior != Behavior.DEATH) {
                movement();
                animation();
            }
            onTeleportation = MapManager.getInstance().checkPlayerOnTeleportation(this);
        }
	}

	@Override
	public void play(long time) {
		if(animation!=null && animation.getStatus()!=Animation.Status.RUNNING)
			animation.play();	
	}

	@Override
	public void pause() {
		if(animation!=null) animation.pause();
	}
	
	public void changeImageFrames()
    {
        imageFrames = playerSelected.get(behavior);
        frameCount = imageFrames.length;
    }
    //GETTERS: ///////////////////////////////////////////////
    public int getTIME_CHANGE_FRAME() {
        return TIME_CHANGE_FRAME;
    }

    @Override
    public Image getImage()
    {
        if(behavior!=Behavior.DEATH || behavior!=Behavior.NEXT) changeImageFrames();
        try {
            return imageFrames[spriteNum];
        } catch (ArrayIndexOutOfBoundsException e){}
        return null;
    }
    //SETTERS: /////////////////////////////////////////////
    public void setPlayerResourceSelected(Integer key) {
        this.playerSelected = ((PlayerResources) Resource.getResource(Resource.Type.PLAYER)).setPlayerResourceSelected(key);
    }

    public void setUp(boolean up) {
        this.up = up;
    }
    public void setLeft(boolean left) {
        this.left = left;
    }
    public void setDown(boolean down) {
        this.down = down;
    }
    public void setRight(boolean right) {
        this.right = right;
    }

    @Override
    public void updateByGameLoop(long currentTime) {
        super.updateByGameLoop();
        setAnimation();
    }

	
}
