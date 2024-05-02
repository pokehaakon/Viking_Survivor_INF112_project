package GameObjects.ObjectActions;

import GameObjects.Actor;
import Tools.Pool.ObjectPool;

import java.util.List;

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

    /**
     * When actor is killed, it has a certain probability of spawning a desired pickup
     * @param prob probability of spawning pickup
     * @param type pickup type
     * @param pickups list of pickups
     * @param pool actor pool
     * @param pickupActions the desired pickup actions
     * @return an action object
     */
    public static Action spawnPickupsIfKilled(
            double prob,
            String type,
            List<Actor> pickups,
            ObjectPool<Actor> pool,
            Action... pickupActions) {

        return (actor) -> {
            if(Math.random() <= prob) {
                Actor pickup = pool.get(type);
                pickup.setPosition(actor.getBody().getPosition());
                pickup.addDieAction(pickupActions);
                //pickup.addAction(pickupActions);

                pickups.add(pickup);
            }

        };
    }




}
