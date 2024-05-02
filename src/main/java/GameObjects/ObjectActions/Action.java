package GameObjects.ObjectActions;

import GameObjects.Actor;

@FunctionalInterface
public interface Action {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor the actor to execute the action on (usually this)
     */
    void act(Actor actor);

}
