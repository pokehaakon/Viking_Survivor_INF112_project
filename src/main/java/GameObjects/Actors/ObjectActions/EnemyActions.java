package GameObjects.Actors.ObjectActions;

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
    public static Action<Enemy> moveInStraightLine() {
        return (e) ->{
            e.updateDirectionState();
            e.updateAnimationState();
            e.move();
        };
    }

    /**
     * Chases player by moving towards its current position
     * @param player object to be chased
     * @return
     */
    public static Action<Enemy> chasePlayer(Player player) {
        return (e) -> {
            e.velocityVector.add(player.getBody().getPosition()).sub(e.getBody().getWorldCenter());
            e.move();
            e.updateDirectionState();
            e.updateAnimationState();
        };

    }


    public static Action<Enemy> destroyIfDead() {
        return (e) -> {
            if(e.HP <= 0) {
                e.kill();
                e.destroy();
            }
        };
    }
    public static Action<Enemy> destroyIfOutOfBounds(Player player) {
        return (e) -> {
            if(e.outOfBounds(player.getBody().getPosition(),DESPAWN_RADIUS)) {
                e.destroy();
            }

        };
    }

    public static Action<Enemy> coolDown(long coolDownDuration) {
        return (e) -> {
            if(e.isUnderAttack()) {
                if(TimeUtils.millis() - e.getLastAttackedTime() > coolDownDuration) {
                    e.setUnderAttack(false);
                }
            }
        };
    }








}
