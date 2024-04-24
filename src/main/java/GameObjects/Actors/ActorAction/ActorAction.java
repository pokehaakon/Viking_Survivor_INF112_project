package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;

public interface ActorAction<T extends Actor> {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(T actor);
}
