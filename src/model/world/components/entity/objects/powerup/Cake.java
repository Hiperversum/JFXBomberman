package model.world.components.entity.objects.powerup;

import model.UI;

public class Cake extends PowerUp {
    public Cake(int y, int x, Type type) {
        super(y,x,type);
        POINTS = 500;
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
