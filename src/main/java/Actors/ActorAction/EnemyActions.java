package Actors.ActorAction;

import Actors.Actor;
import Actors.Enemy.Enemy;
import com.badlogic.gdx.math.Vector2;

public abstract class EnemyActions {

    /**
     * A swarm strike is a straight movement in a direction determined by the targets initial position
     * @param swarmDirection
     * @return
     */
    public static ActorAction swarmStrike(Vector2 swarmDirection) {
        return (e)-> {
            Enemy enemy = (Enemy) e;
            enemy.resetVelocity();
            enemy.setVelocityVector(swarmDirection.x, swarmDirection.y);
            enemy.move();
        };
    }

    /**
     * Chases player by moving towards its current position
     * @param player
     * @return
     */
    public static ActorAction chasePlayer(Actor player) {

        return (e) ->{
            Enemy enemy = (Enemy) e;
            enemy.velocityVector.add(player.getBody().getPosition()).sub(e.getBody().getWorldCenter());
            enemy.move();
        };
    }




}
