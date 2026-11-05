package model.world.components.entity.objects.powerup;

import model.UI;

public class IceCreamCone extends PowerUp {
    public IceCreamCone(int y, int x, Type type) {
        super(y,x,type);
        POINTS = 150;
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
