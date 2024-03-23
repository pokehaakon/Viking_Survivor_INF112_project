package Animations;

import Actors.Actor;
import Actors.Enemy.Enemy;
import Actors.Player.Player;

import static Animations.GIFs.*;

public abstract class Animations {

    /**
     * Handles walking animation for player.
     * Changes animations for every new animation states
     * @return ActorAnimation object
     */
    public static ActorAnimation walkingAnimation() {

        return (p) -> {
            Player player = (Player) p;
            PlayerAnimationStates newState;
            if(player.isIdle()) {
                // idle right
                if(player.lastMoveRight) {
                    newState = PlayerAnimationStates.IDLE_RIGHT;
                    // checks if player is not already in the new state
                    // only want to call setNewAnimation() once for every direction change
                    if(player.currentAnimationState != newState){
                        player.setNewAnimationGIF(getGIF(PLAYER_IDLE_RIGHT));
                        player.currentAnimationState = newState;
                    }
                }

                else {
                    // idle left
                    newState = PlayerAnimationStates.IDLE_LEFT;
                    if(player.currentAnimationState != newState) {
                        player.setNewAnimationGIF(getGIF(PLAYER_IDLE_LEFT));
                        player.currentAnimationState = newState;
                    }

                }

            }
            else {
                // move right
                if (player.lastMoveRight) {
                    newState = PlayerAnimationStates.MOVE_RIGHT;
                    if (player.currentAnimationState != newState) {
                        player.setNewAnimationGIF(getGIF(PLAYER_RIGHT));
                        player.currentAnimationState = newState;

                    }
                }

                else {
                    // move left
                    newState = PlayerAnimationStates.MOVE_LEFT;
                    if (player.currentAnimationState != newState) {
                        player.setNewAnimationGIF(getGIF(PLAYER_LEFT));
                        player.currentAnimationState = newState;
                    }

                }
            }
        };
    }

    public static ActorAnimation enemyChaseAnimation(Actor player) {
        return (e) -> {
            Enemy enemy = (Enemy) e;
            Enemy.LocationState newLocationState;
            if(enemy.getBody().getPosition().x - player.getBody().getPosition().x >= 0) {
                newLocationState = Enemy.LocationState.RIGHT_OF_CENTER;
            }
            else {
                newLocationState = Enemy.LocationState.LEFT_OF_CENTER;
            }
            if(newLocationState == Enemy.LocationState.RIGHT_OF_CENTER && enemy.locationState != newLocationState) {
                enemy.setNewAnimationGIF(getGIF(PLAYER_LEFT));
                System.out.println("going left");
                enemy.locationState = newLocationState;
            }
            else if(newLocationState == Enemy.LocationState.LEFT_OF_CENTER && enemy.locationState != newLocationState) {
                System.out.println("going right");
                enemy.setNewAnimationGIF(getGIF(PLAYER_RIGHT));
                enemy.locationState = newLocationState;
            }

        };
    }


}
