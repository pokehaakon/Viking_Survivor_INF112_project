package Actors.ActorAction;

import InputProcessing.KeyStates;
import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;

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

                //p.setNewAnimation(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingleft.gif").read()));
            }
            if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
                p.setVelocityVector(1,0);
                //p.setNewAnimation(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingright.gif").read()));
            }
            p.moveWithConstantSpeed();
        };
    }


}
