package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Actor;
import InputProcessing.KeyStates;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class PlayerActions {

    /**
     * Moves actor (usually player) based on user input
     * @param keyStates keybindings and state for input keys
     * @return an action
     */

    public static ActorAction moveToInput(KeyStates keyStates) {
        return (player) -> {

            var vel = player.getBody().getLinearVelocity();
            vel.set(0, 0);
            vel.y += keyStates.getState(KeyStates.GameKey.UP) ? 1 : 0;
            vel.y += keyStates.getState(KeyStates.GameKey.DOWN) ? -1 : 0;
            vel.x += keyStates.getState(KeyStates.GameKey.RIGHT) ? 1 : 0;
            vel.x += keyStates.getState(KeyStates.GameKey.LEFT) ? -1 : 0;
            vel.setLength(player.getSpeed());

            player.getBody().setLinearVelocity(vel);
        };
    }

//    public static ActorAction<Player> coolDown(long coolDownDuration) {
//        return (p) -> {
//            if (!p.isUnderAttack()) return;
//            if (TimeUtils.millis() - p.getLastAttackedTime() > coolDownDuration) {
//                p.setUnderAttack(false);
//            }
//
//        };
//    }


}
