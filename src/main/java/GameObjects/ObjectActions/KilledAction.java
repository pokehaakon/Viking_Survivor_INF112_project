package GameObjects.ObjectActions;

import GameObjects.Actor;

public abstract class KilledAction {

    static public Action doIfDefeated(Action action) {
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
    static public Action destroyIfDefeated() {
        return doIfDefeated(Actor::kill);
    }
}
