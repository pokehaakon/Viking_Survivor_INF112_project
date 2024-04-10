package Animations;

public abstract class ActorAnimations {

    /**
     * Handles walking animation for player.
     * Changes animations for every new animation state
     * @return ActorAnimation object
     */
    public static ActorMovement playerMovement() {

        return (player) -> {

            MovementState newState;

            // if player is idle
            if (player.isIdle()) {
                // checks the direction state
                newState = MovementState.IDLE;
            }
            // if player is moving
            else {
               newState = MovementState.WALKING;
            }

            player.setAnimationState(newState);

        };
    }









}
