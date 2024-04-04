package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Enemy.Enemy;
import com.badlogic.gdx.math.Vector2;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;

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

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds or killed.
     * @param player its location is used to determine if the enemy is out of bounds
     * @return an ActorAction object
     */
    public static ActorAction destroyIfDefeated(Actor player) {
        return (e) -> {
            Enemy enemy = (Enemy) e;
            if(enemy.HP <= 0 || enemy.outOfBounds(player)) {
                enemy.destroy();
            }
        };
    }







}
