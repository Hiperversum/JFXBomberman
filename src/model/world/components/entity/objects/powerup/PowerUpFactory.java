package model.world.components.entity.objects.powerup;

public class PowerUpFactory {
    public PowerUp createPowerUp(PowerUp.Type type,int y,int x){
        switch (type){
            case HEARTH -> {
                return new Hearth(y,x,type);
            }
            case EXTRA_BOMB -> {
                return new ExtraBomb(y,x,type);
            }
            case EXPLOSION_EXPANDER -> {
                return new ExplosionExpander(y,x,type);
            }
            case APPLE -> {
                return new Apple(y,x,type);
            }
            case RICE_BALL -> {
                return new RiceBall(y,x,type);
            }
            case FROZEN_POP -> {
                return new FrozenPop(y,x,type);
            }
            case ICE_CREAM_CONE -> {
                return new IceCreamCone(y,x,type);
            }
            case CUP_AND_BALL -> {
                return new CupAndBall(y,x,type);
            }
            case CAKE -> {
                return new Cake(y,x,type);
            }
            case INCREASE_TIME -> {
                return new IncreaseTime(y,x,type);
            }

        }
        return null;
    }
}
