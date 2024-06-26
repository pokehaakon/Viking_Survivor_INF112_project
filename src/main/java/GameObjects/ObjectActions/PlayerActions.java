package GameObjects.ObjectActions;

import InputProcessing.KeyStates;

public abstract class PlayerActions {

    /**
     * Moves actor (usually player) based on user input
     * @param keyStates keybindings and state for input keys
     * @return an action
     */

    public static Action moveToInput(KeyStates keyStates) {
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
}
