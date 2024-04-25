package GameObjects.Actors.ObjectActions;

public interface Action<T> {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param object the object which performs the action
     */
    void act(T object);
}
