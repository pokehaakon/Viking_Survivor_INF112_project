package GameObjects.Actors.ObjectActions;

import GameObjects.Actors.Actor;

@FunctionalInterface
public interface Action {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(Actor actor);
}
