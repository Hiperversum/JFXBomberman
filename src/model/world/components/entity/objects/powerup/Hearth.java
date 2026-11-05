package model.world.components.entity.objects.powerup;

import model.UI;

public class Hearth extends PowerUp{

    private final int MAX_LIVES = 10;
    public Hearth(int y, int x, Type type) {
        super(y, x, type);
    }

    @Override
    public void collectPowerUp() {
        UI ui = UI.getInstance();
        if(ui.getLives()<MAX_LIVES){
            ui.addLife();
        } else ui.addPointsToTheScore(POINTS);
    }


}
