package Actors.ActorAction;

import Actors.Actor;

public interface IEnemyActions {

    /**
     * Chasing player by moving towards its position
     * @param player
     */
    ActorAction chasePlayer(Actor player);


}
