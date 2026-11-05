package model.world.components.entity.objects.powerup;

import model.UI;

public class RiceBall extends PowerUp {
    public RiceBall(int y, int x, Type type) {
        super(y,x,type);
        POINTS = 100;
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
