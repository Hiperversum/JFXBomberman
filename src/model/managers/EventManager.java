package model.managers;
import model.Model;
import model.events.Events;

public class EventManager extends Model {
    private static EventManager instance;

    private EventManager(){}

    public static EventManager getInstance() {
        if (instance == null) instance = new EventManager();
        return instance;
    }
    public void createEvent(Events event) {
        setChanged();
        notifyObservers(event);
    }

}
