package model.world.components.entity.objects.powerup;

import model.UI;

public class FrozenPop extends PowerUp {
    public FrozenPop(int y, int x, Type type) {
        super(y,x,type);
        POINTS = 125;
    }
    @Override
    public void collectPowerUp() {
        UI.getInstance().addPointsToTheScore(POINTS);
    }
}
