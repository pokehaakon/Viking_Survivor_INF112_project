package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.IActor;
import GameObjects.GameObject;

public interface ActorAction<T> {

    // this interface acts as an action object

    /**
     * Executes a desired action
     * @param actor
     */
    void act(T actor);
}
