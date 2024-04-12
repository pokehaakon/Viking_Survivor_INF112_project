package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player;
import InputProcessing.KeyStates;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class PlayerActions {

    /**
     * Moves player based on user input
     * @param keyStates
     * @return an action
     */

    public static ActorAction<Player> moveToInput(KeyStates keyStates) {

        return (p) ->{
            p.idle = true;
            p.resetVelocity();
            if (keyStates.getState(KeyStates.GameKey.UP)) {
                p.setVelocityVector(0,1);
                p.idle = false;
            }
            if (keyStates.getState(KeyStates.GameKey.DOWN)) {
                p.setVelocityVector(0,-1);
                p.idle = false;
            }
            if (keyStates.getState(KeyStates.GameKey.LEFT)) {
                p.setVelocityVector(-1,0);
                p.idle = false;

            }
            if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
                p.setVelocityVector(1,0);
                p.idle = false;
            }
            p.updateDirectionState();
            p.updateAnimationState();
            p.move();
        };
    }

    public static ActorAction<Player> coolDown(long coolDownDuration) {
        return (p) -> {
            if(p.isUnderAttack()) {
                if(TimeUtils.millis() - p.getLastAttackedTime() > coolDownDuration) {
                    p.setUnderAttack(false);
                }
            }
        };
    }


}
