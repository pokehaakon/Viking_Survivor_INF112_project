package Actors.Enemy;

import Actors.Actor;

public interface IEnemy {


    /**
     * Follows an actor object
     * @param actor
     */
    void chase(Actor actor);

    /**
     * Swarm strike: swarm members strikes in a straight line according to the player's initial position
     * @param actor
     */
    void swarmStrike(Actor actor);


}
