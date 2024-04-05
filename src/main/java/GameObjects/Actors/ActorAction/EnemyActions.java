package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Enemy.Enemy;

import static Tools.FilterTool.createFilter;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public abstract class EnemyActions {


    public static final double DESPAWN_RADIUS = (double) 1.1*SCREEN_HEIGHT;
    /**
     * Moves enemy in straight line according to its velocity vector and speed
     *
     */
    public static ActorAction moveInStraightLine() {
        return Actor::move;
    }

    /**
     * Chases player by moving towards its current position
     * @param player object to be chased
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
            if(enemy.HP <= 0 || enemy.outOfBounds(player,DESPAWN_RADIUS)) {
                enemy.destroy();
            }
        };
    }







}
