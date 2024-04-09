package Animations;

import GameObjects.Actors.DirectionState;

import static Animations.AnimationConstants.*;
import static GameObjects.Actors.ObjectTypes.EnemyType.RAVEN;
import static GameObjects.Actors.ObjectTypes.EnemyType.ORC;

public abstract class ActorAnimations {

    /**
     * Handles walking animation for player.
     * Changes animations for every new animation state
     * @return ActorAnimation object
     */
    public static ActorAnimation playerMoveAnimation() {

        return (player) -> {

            DirectionState currentDirection = player.getDirectionState();
            AnimationStates newState;
            String gifPath;

            // if player is idle
            if (player.isIdle()) {
                // checks the direction state
                newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.IDLE_RIGHT : AnimationStates.IDLE_LEFT;
                gifPath = (currentDirection == DirectionState.RIGHT) ? PLAYER_IDLE_RIGHT : PLAYER_IDLE_RIGHT;

            }

            // if player is moving
            else {
                newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.MOVE_RIGHT : AnimationStates.MOVE_LEFT;
                gifPath = (currentDirection == DirectionState.RIGHT) ? PLAYER_RIGHT : PLAYER_RIGHT;
            }

            player.setAnimationState(newState,gifPath);

        };
    }

    /**
     * Handles the moving animation for enemy
     * @return animation object
     */
    public static ActorAnimation enemyMoveAnimation() {
        return (enemy) -> {
            String SpritePath;

            // enemy can temporarily only have two animation states: move left and move right

            DirectionState currentDirection = enemy.getDirectionState();
            AnimationStates newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.MOVE_RIGHT : AnimationStates.MOVE_LEFT;
            if (enemy.getType().equals(RAVEN)) {
                SpritePath = "raven.gif";
            } else if (enemy.getType().equals(ORC)) {
                SpritePath = "orc.gif";
            } else {
                throw new IllegalStateException("Unexpected value: " + enemy.getType());
            }



            enemy.setAnimationState(newState, SpritePath);


        };
    }



}
