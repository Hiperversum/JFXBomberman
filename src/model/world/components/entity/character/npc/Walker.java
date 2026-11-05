package model.world.components.entity.character.npc;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import model.UI;
import model.loader.resources.Resource;
import model.loader.resources.NPCResources;
import model.managers.AnimationManager;
import model.managers.EntityManager;
import model.world.components.entity.character.behavior.CharaterBox;
import model.world.components.entity.character.behavior.movement.RandomMovement;


public class Walker extends NPC {
	
	private static final long serialVersionUID = 7036461494112562156L;
    private final int TIME_CHANGE_FRAME = 6;
    private final int DEATH_TIME_ANIMATION = 700;
    private boolean isDeath;
    public Walker(){}
    public Walker(int y,int x){
        super("walker");
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
        spriteCounter++;
        if(frameCount!=0) {
            //Calcola spriteNum
            spriteNum = (spriteCounter / TIME_CHANGE_FRAME) % frameCount;
            // Reset spriteCounter se necessario
            if (spriteCounter >= frameCount * TIME_CHANGE_FRAME) {
                spriteCounter = 0;
            }
        }
    }
    //FRAMES:
    @Override
    public void destroy() {
        if (!isDeath) UI.getInstance().addPointsToTheScore(100);
        isDeath = true;
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.millis(DEATH_TIME_ANIMATION), event -> {
                    EntityManager.getInstance().removeEntity(this);
                    AnimationManager.getInstance().removeAnimated(this);
                })
        );
        timeline.play();

    }

    @Override
    public void changeImageFrames(){
        NPCResources resource = (NPCResources) Resource.getResource(Resource.Type.NPC);
        imageFrames = resource.getWalkFrames(getName(), behavior);
        if(isDeath) imageFrames = resource.getDeathFrames(getName(), behavior);
        frameCount = imageFrames.length;
    }
    @Override
    public void updateByGameLoop(long currentTime) {
        super.updateByGameLoop();
        setAnimation();
            
    }

    @Override
    public void attack() {}
    
	@Override
	public void setAnimation() {
		if(isDeath == false) movement();
        animation();
	}
	@Override
	public void play(long time) {}
	@Override
	public void pause() {}
}
