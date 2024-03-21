package Actors.ActorAction;

import Actors.Actor;
import com.badlogic.gdx.math.Vector2;

public class EnemyActions {

    /**
     * A swarm strike is a straight movement in a direction determined by the targets initial position
     * @param swarmDirection
     * @return
     */
    public static ActorAction swarmStrike(Vector2 swarmDirection) {
        return (e)-> {
            e.resetVelocity();
            e.setVelocityVector(swarmDirection.x, swarmDirection.y);
            e.moveWithConstantSpeed();
        };
    }

    /**
     * Chases player by moving towards its current position
     * @param player
     * @return
     */
    public static ActorAction chasePlayer(Actor player) {

        return (e) ->{
            e.velocityVector.add(player.getBody().getPosition()).sub(e.getBody().getWorldCenter());
            e.moveWithConstantSpeed();
        };

    }

}
