package model.world.components.entity.objects.powerup;

import model.UI;

public class CupAndBall extends PowerUp{
    public CupAndBall(int y, int x, Type type) {
        super(y, x, type);
        POINTS = 250;
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
