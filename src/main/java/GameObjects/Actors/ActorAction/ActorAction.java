package GameObjects.Actors.ActorAction;

public interface ActorAction<T> {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(T actor);
}
