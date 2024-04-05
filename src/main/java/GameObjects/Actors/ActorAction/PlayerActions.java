package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player.Player;
import InputProcessing.KeyStates;

public abstract class PlayerActions {

    /**
     * Moves player based on user input
     * @param keyStates
     * @return an action
     */

    public static ActorAction moveToInput(KeyStates keyStates) {

        return (p) ->{
            Player player = (Player) p;
            player.idle = true;
            player.resetVelocity();
            if (keyStates.getState(KeyStates.GameKey.UP)) {
                player.setVelocityVector(0,1);
                player.idle = false;
            }
            if (keyStates.getState(KeyStates.GameKey.DOWN)) {
                player.setVelocityVector(0,-1);
                player.idle = false;
            }
            if (keyStates.getState(KeyStates.GameKey.LEFT)) {
                player.setVelocityVector(-1,0);
                player.idle = false;

            }
            if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
                player.setVelocityVector(1,0);
                player.idle = false;
            }

            player.move();
        };
    }


}
