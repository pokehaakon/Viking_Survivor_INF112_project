package Actors.ActorAction;

import InputProcessing.KeyStates;

public class PlayerActions {

    public static ActorAction moveToInput(KeyStates keyStates) {

        return (p) ->{
            p.resetVelocity();
            if (keyStates.getState(KeyStates.GameKey.UP)) {
                p.setVelocityVector(0,1);
            }
            if (keyStates.getState(KeyStates.GameKey.DOWN)) {
                p.setVelocityVector(0,-1);
            }
            if (keyStates.getState(KeyStates.GameKey.LEFT)) {
                p.setVelocityVector(-1,0);
            }
            if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
                p.setVelocityVector(1,0);
            }
            p.moveWithConstantSpeed();
        };
    }
}
