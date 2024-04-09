package GameObjects.Actors.ActorAction;

import GameObjects.Actors.Player.Player;
import InputProcessing.KeyStates;

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

            p.move();
        };
    }


}
