package model.engine;

import javafx.animation.AnimationTimer;

public class GameLoop extends AnimationTimer {
    /*
            Nel GameLoop:
            > 1. Aggiorno informazioni(Posizione Giocatore,NPC....)
            > 2. Avviso la View che deve Disegnare lo schermo con info aggiornate
         */
    //Aggiorna in media 60 volte al secondo

    private Engine engine;
    private double fps;
    private boolean isRunning;
    
    public GameLoop(Engine engine) {
        this.engine = engine;
    }

    private long lastTime = 0;
    private int frameCount = 0;

    @Override
    public void handle(long currentTime) {
        calculateFPS(currentTime);
        engine.updateByGameLoop(currentTime);

    }
    
    public void start() {
        super.start();
        isRunning = true;
    }
    
    public void stop() {
    	super.stop();
    	isRunning = false;
    }
    
    
    public boolean isRunning() {
		return isRunning;
	}


	public void calculateFPS(long currentTime) {
        if (lastTime == 0) {
            lastTime = currentTime;
            return;
        }

        long deltaTime = currentTime - lastTime;
        frameCount++;

        if (deltaTime >= 1_000_000_000) { // Calcola gli FPS ogni secondo
            fps = (double) frameCount / deltaTime * 1_000_000_000;
            System.out.println("FPS: " + (int) fps);

            // Reimposta le variabili per il prossimo intervallo
            lastTime = currentTime;
            frameCount = 0;
        }
    }
}

















