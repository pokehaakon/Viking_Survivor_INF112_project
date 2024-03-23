package Animations;

import Actors.DirectionState;
import Actors.Enemy.Enemy;
import Actors.Player.Player;

import static Animations.GIF.*;

public abstract class Animations {

    /**
     * Handles walking animation for player.
     * Changes animations for every new animation states
     * @return ActorAnimation object
     */
    public static ActorAnimation walkingAnimation() {

        return (p) -> {
            Player player = (Player) p;
            DirectionState currentDirection = player.getDirectionState();
            AnimationStates newState;
            String gifPath;

            if (player.isIdle()) {
                newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.IDLE_RIGHT : AnimationStates.IDLE_LEFT;
                gifPath = (currentDirection == DirectionState.RIGHT) ? PLAYER_IDLE_RIGHT : PLAYER_IDLE_LEFT;
            } else {
                newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.MOVE_RIGHT : AnimationStates.MOVE_LEFT;
                gifPath = (currentDirection == DirectionState.RIGHT) ? PLAYER_RIGHT : PLAYER_LEFT;
            }

            player.setAnimationState(newState,gifPath);

        };
    }

    public static ActorAnimation enemyChaseAnimation() {
        return (e) -> {

            Enemy enemy = (Enemy) e;
            DirectionState currentDirection = enemy.getDirectionState();
            AnimationStates newState = (currentDirection == DirectionState.RIGHT) ? AnimationStates.MOVE_RIGHT : AnimationStates.MOVE_LEFT;
            String gifPath = (currentDirection == DirectionState.RIGHT) ? PLAYER_RIGHT : PLAYER_LEFT;

            enemy.setAnimationState(newState, gifPath);


        };
    }



}
