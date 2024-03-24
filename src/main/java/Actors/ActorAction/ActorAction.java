package Actors.ActorAction;

import Actors.Actor;

public interface ActorAction {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(Actor actor);
}
