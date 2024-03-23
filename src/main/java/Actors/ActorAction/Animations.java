package Actors.ActorAction;

import Animations.GIFs;
import Actors.Player.Player;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import Animations.GIFs.*;

import static Animations.GIFs.*;

public class Animations {

    public static ActorAnimation playerAnimation() {
        return (p) -> {
            Player player = (Player) p;
            Animation<TextureRegion> newAnimation;
            //String newSprite;

            if(player.idle){
                newAnimation = player.lastMoveRight ? getGIF(PLAYER_IDLE_RIGHT) : getGIF(PLAYER_IDLE_LEFT);
                //newSprite = player.lastMoveRight ? PLAYER_IDLE_RIGHT : PLAYER_IDLE_LEFT;
            }
            else {
                newAnimation = player.lastMoveRight ? getGIF(PLAYER_RIGHT) : getGIF(PLAYER_LEFT);
               // newSprite = player.lastMoveRight ? PLAYER_RIGHT : PLAYER_LEFT;

            }
            player.setNewAnimation(newAnimation);
            //player.setNewSprite(newSprite);

        };
    }


}
