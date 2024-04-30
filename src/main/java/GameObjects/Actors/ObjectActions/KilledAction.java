package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;

public abstract class KilledAction {

    static public ActorAction doIfDefeated(ActorAction action) {
        return (e) -> {
            if(e.getHP() <= 0) {
                action.act(e);
            }
        };
    }

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds or killed.
     * @return an ActorAction object
     */
    static public ActorAction destroyIfDefeated() {
        return doIfDefeated(Actor::kill);
    }
}
