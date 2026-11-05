package model.world.components.entity.objects.powerup;

import model.UI;

public class IncreaseTime extends PowerUp{
    public IncreaseTime(int y, int x, Type type) {
        super(y, x, type);
    }

    @Override
    public void collectPowerUp() {
        UI.getInstance().increasePlayTime();
    }
}
