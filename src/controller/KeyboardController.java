package controller;

import javafx.scene.input.KeyCode;
import model.engine.Engine;
import model.events.PauseEvent;
import model.managers.EventManager;
import model.world.components.entity.character.Player;
import view.tools.Brush;
import view.View;

public class KeyboardController {
    private View view;
    public KeyboardController(View view) {
        this.view = view;
        setKeyboard();
    }



    public void setKeyboard() {
        Player player = Player.getInstance();
        view.getWindow().getScene().setOnKeyPressed(keyEvent -> {
            if (Engine.getGameLoop().isRunning())
            switch (keyEvent.getCode()) {
                case UP:
                case W: player.setUp(true);break;
                case LEFT:
                case A: player.setLeft(true);break;
                case DOWN:
                case S: player.setDown(true);break;
                case RIGHT:
                case D: player.setRight(true);break;
                case SPACE:
                case E: player.interact();break;
                case F11: Brush.setShowInfo();

            }
            if (keyEvent.getCode() == KeyCode.P || keyEvent.getCode() == KeyCode.ESCAPE) {
                EventManager.getInstance().createEvent(new PauseEvent());
            }
        });

        view.getWindow().getScene().setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP:
                case W: player.setUp(false);break;
                case LEFT:
                case A: player.setLeft(false);break;
                case DOWN:
                case S: player.setDown(false);break;
                case RIGHT:
                case D: player.setRight(false);break;
            }
        });
    }


}
