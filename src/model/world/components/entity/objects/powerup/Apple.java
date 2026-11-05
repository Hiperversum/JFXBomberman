package model.world.components.entity.objects.powerup;

import model.UI;

public class Apple extends PowerUp{
    public Apple(int y, int x,Type type) {
        super(y, x,type);
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
