package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import static Tools.FilterTool.createFilter;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public abstract class EnemyActions {


    public static final double DESPAWN_RADIUS = (double) 1.1*SCREEN_HEIGHT;
    /**
     * Moves enemy in straight line according to its velocity vector and speed
     *
     */
    public static ActorAction<Enemy> moveInStraightLine(Vector2 vel) {
        var newVel = vel.cpy();
        return e -> {
            e.getBody().setLinearVelocity(newVel.setLength(e.speed));
        };
    }

    /**
     * Chases player by moving towards its current position
     * @param player object to be chased
     * @return
     */
    public static ActorAction<Enemy> chasePlayer(Player player) {
        return (e) -> {
            var vel = e.getBody().getLinearVelocity();
            vel
                    .set(player.getBody().getPosition())
                    .sub(e.getBody().getWorldCenter())
                    .scl(e.speed);
            e.getBody().setLinearVelocity(vel);
            //e.move();
            //e.updateDirectionState();
            //e.updateAnimationState();
        };

    }

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds.
     * @param player its location is used to determine if the enemy is out of bounds
     * @return an ActorAction object
     */
    public static ActorAction<Enemy> destroyIfOutOfBounds(Player player) {
        return (e) -> {
            if(e.outOfBounds(player, DESPAWN_RADIUS)) {
                e.kill();
            }
        };
    }

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds or killed.
     * @return an ActorAction object
     */
    public static ActorAction<Enemy> destroyIfDefeated() {
        return (e) -> {
            if(e.HP <= 0) {
                e.kill();
            }
        };
    }

    public static ActorAction<Enemy> coolDown(long coolDownDuration) {
        return (e) -> {
            if(e.isUnderAttack()) {
                if(TimeUtils.millis() - e.getLastAttackedTime() > coolDownDuration) {
                    e.setUnderAttack(false);
                }
            }
        };
    }








}
