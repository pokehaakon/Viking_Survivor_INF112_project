package Actors.ActorAction;

import Actors.Enemy.Sprites;
import Actors.Player.Player;
import InputProcessing.KeyStates;

public class Animations {

    public static ActorAnimation playerAnimation(KeyStates keyStates) {
        return (p) -> {
            Player player = (Player) p;
            if(keyStates.getState(KeyStates.GameKey.LEFT)) {
                player.idleAnimation = false;
                player.rightAnimation = false;
                if(!player.leftAnimation) {
                    player.setNewAnimation(Sprites.PLAYER_LEFT);
                    System.out.println("left");
                    player.leftAnimation = true;
                }
            }
            else if(keyStates.getState(KeyStates.GameKey.RIGHT)) {
                player.leftAnimation = false;
                player.idleAnimation = false;
                if(!player.rightAnimation) {
                    player.setNewAnimation(Sprites.PLAYER_RIGHT);
                    System.out.println("right");
                    player.rightAnimation = true;
                }

            }
        };
    }
}
