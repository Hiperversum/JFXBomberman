package model.world.components.entity.character.npc;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.util.Duration;
import model.UI;
import model.loader.resources.NPCResources;
import model.loader.resources.Resource;
import model.managers.AnimationManager;
import model.managers.EntityManager;
import model.managers.MapManager;
import model.utility.RandomUtils;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.character.behavior.movement.RandomMovement;
import model.world.components.entity.effects.Explosion;
import model.world.components.tiles.Tile;

public class Bomber extends NPC{
	private static final long serialVersionUID = 7023194080394933678L;
    private final int TIME_CHANGE_FRAME = 10;
    private final int DEATH_TIME_ANIMATION = 700;
    private boolean isDeath;
    private boolean isAttackTime;
    private int attackCounter;
    public Bomber(){}
    public Bomber(int y, int x) {
        super("bomber");
        setY(y*tileSize);
        setX(x*tileSize);
        setSpeed(1);
        initialize();
    }

    //METHODS://///////////////////////////////////////////////////////////////////////////////////////////////////////
    public void initialize(){
        //BEHAVIOR:
        movementPath = new RandomMovement(this);
        //COLLISION BOX:
        entityBox.setEntityRectangleDimension(30,30);
        entityBox.setEntityRectangleCoordinates();
        //DAMAGE BOX:
        damageBox.setDamageRectangle(entityBox.getEntityRectangle());
        movementPath.chooseDirection();
    }

    //BEHAVIOR :
    @Override
    public void movement(){
        movementPath.chooseDirection();
        entityBox.setEntityRectangleCoordinates();
        //COLLISION DETECTION
        if (!((CharaterBox)entityBox).CollisionDetection()) {
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
            }

        }
    }


    public void animation(){
        if(isAttackTime==false) {
            spriteCounter++;
            if (frameCount != 0) {
                //Calcola spriteNum
                spriteNum = (spriteCounter / TIME_CHANGE_FRAME) % frameCount;
                // Reset
                if (spriteCounter >= frameCount * TIME_CHANGE_FRAME) {
                    spriteCounter = 0;
                }
            }
        }
    }
    
    @Override
	public void setAnimation() {
    	if(isDeath == false && isAttackTime==false){
            movement();
            attack();
        }
        animation();
	}
	@Override
	public void play(long time) {
		if(animation!=null && animation.getStatus()!=Animation.Status.RUNNING)
		animation.play();
		if(timeline!=null && timeline.getStatus()!=Animation.Status.RUNNING)
        	timeline.play();
		
	}
	@Override
	public void pause() {
		if(animation!=null)
		animation.pause();
		if(timeline!=null)
		timeline.pause();
	}

    @Override
    public void changeImageFrames(){
        NPCResources resource = (NPCResources) Resource.getResource(Resource.Type.NPC);
        imageFrames = resource.getWalkFrames(getName(), behavior);
        if(isDeath) imageFrames = resource.getDeathFrames(getName(), behavior);
        if(isAttackTime) imageFrames = resource.getAttackFrames(getName());
        frameCount = imageFrames.length;
    }

    @Override
    public void destroy() {
    	if(!isAttackTime) {
        if (!isDeath) UI.getInstance().addPointsToTheScore(100);
        isDeath = true;
        timeline = new Timeline(
                new KeyFrame(Duration.millis(DEATH_TIME_ANIMATION), event -> {
                    EntityManager.getInstance().removeEntity(this);
                    AnimationManager.getInstance().removeAnimated(this);
                    timeline = null;
                })
        );
    	}
    }

    @Override
    public void updateByGameLoop(long currentTime) {
        super.updateByGameLoop();
        setAnimation();
        
    }

    @Override
    public void attack() {
        attackCounter++;
        Tile[][] map = MapManager.getInstance().getMap();
        if (attackCounter >= RandomUtils.generateRandomInt(500,800)){
            spriteNum = 0;
            isAttackTime = true;
            animation = new Transition() {
                {
                    setCycleDuration(Duration.millis(400));
                }

                protected void interpolate(double frac) {
                    int i = (int) (frac * (imageFrames.length));
                    if(i<imageFrames.length)
                    setImage(imageFrames[i]);
                }
            };
            
            timeline = new Timeline(new KeyFrame(Duration.seconds(1.6), explosion -> {
                new Explosion((int) (getEntityBox().getEntityRectangleCenter().getY()/tileSize),
                        (int) (getEntityBox().getEntityRectangleCenter().getX()/tileSize),
                        RandomUtils.generateRandomInt(1,2),
                        map,
                        this);
                isAttackTime = false;
                attackCounter = 0;
                animation = null;
                timeline = null;
            }));
            
            animation.setOnFinished(event->{
                spriteNum = imageFrames.length-1;
            });



        }


    }
	
}
