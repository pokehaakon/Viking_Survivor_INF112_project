package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Enemy;
import GameObjects.Actors.Player;
import com.badlogic.gdx.utils.TimeUtils;

import static Tools.FilterTool.createFilter;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public abstract class EnemyActions {


    public static final double DESPAWN_RADIUS = (double) 1.1*SCREEN_HEIGHT;
    /**
     * Moves enemy in straight line according to its velocity vector and speed
     *
     */
    public static ActorAction<Enemy> moveInStraightLine() {
        return (e) ->{
            e.updateDirectionState();
            e.move();
        };
    }

    /**
     * Chases player by moving towards its current position
     * @param player object to be chased
     * @return
     */
    public static ActorAction<Enemy> chasePlayer(Player player) {
        return (e) -> {
            e.velocityVector.add(player.getBody().getPosition()).sub(e.getBody().getWorldCenter());
            e.move();
            e.updateDirectionState();
        };

    }

    /**
     * Handles the despawning of enemies.
     * Enemy despawns when it is out of bounds or killed.
     * @param player its location is used to determine if the enemy is out of bounds
     * @return an ActorAction object
     */
    public static ActorAction<Enemy> destroyIfDefeated(Player player) {
        return (e) -> {
            if(e.HP <= 0 || e.outOfBounds(player,DESPAWN_RADIUS)) {
                e.destroy();
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
