package model.world.components.entity.objects.powerup;

import model.UI;
import model.world.components.entity.character.Player;

public class ExplosionExpander extends PowerUp{
    private final int MAX_RANGE = 10;
    public ExplosionExpander(int y, int x,Type type) {
        super(y, x,type);
    }

    @Override
    public void collectPowerUp() {
        Player player = Player.getInstance();
        if(player.getRange()< MAX_RANGE){
            player.increaseBombRange();
        } else UI.getInstance().addPointsToTheScore(POINTS);
    }
}
