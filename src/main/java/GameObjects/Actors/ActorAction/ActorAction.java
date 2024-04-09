package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import GameObjects.GameObject;

public interface ActorAction {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(Actor actor);
}
