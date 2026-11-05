package model.world.components.entity.objects.powerup;


import model.UI;
import model.world.components.entity.character.Player;

public class ExtraBomb extends PowerUp{
    private final int MAXIMUM_BOMBS_OBTAINABLE = 9;

    public ExtraBomb(int y,int x,Type type) {
        super(y,x,type);
    }

    @Override
    public void collectPowerUp() {
        Player player = Player.getInstance();
        if (player.getNumPlaceableBombs()<MAXIMUM_BOMBS_OBTAINABLE){
            player.increasesPlaceableBombs();
        } else UI.getInstance().addPointsToTheScore(POINTS);

    }

}
