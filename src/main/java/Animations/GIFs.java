package Animations;

import Actors.ActorAction.Animations;
import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class GIFs {

    // sprites
    public static final String PLAYER_PNG = "vikingright.gif";
    public static final String ENEMY_1_PNG = "img.png";
    public static final String ENEMY_2_PNG = "img_3.png";
    //public static final Animation<TextureRegion> WOLF_RIGHT = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("wolfright.gif").read());
    //public static final Animation<TextureRegion> WOLF_LEFT = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("wolfLeft.gif").read());
    public static final String PLAYER_RIGHT = "vikingright.gif";
    public static final String PLAYER_LEFT = "vikingleft.gif";
    public static final String PLAYER_IDLE_LEFT ="viking_idle_left.gif";
    public static final String PLAYER_IDLE_RIGHT = "viking_idle_right.gif";

    // scales

    public static final float PLAYER_SCALE = 0.5f;
    public static final float ENEMY1_SCALE = 0.1f;
    public static final float ENEMY2_SCALE = 0.1f;

    public static Animation<TextureRegion> getGIF(String fileName) {
        return GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(fileName).read());
    }

}
