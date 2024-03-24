package Animations;

import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationConstants {

    // sprites
    public static final String PLAYER_PNG = "vikingright.gif";
    public static final String ENEMY_1_PNG = "img.png";
    public static final String ENEMY_2_PNG = "img_3.png";

    // player GIFS
    public static final String PLAYER_RIGHT = "vikingright-crop.gif";
    public static final String PLAYER_LEFT = "vikingleft-crop.gif";
    public static final String PLAYER_IDLE_LEFT ="viking_idle_left-crop.gif";
    public static final String PLAYER_IDLE_RIGHT = "viking_idle_right-crop.gif";

    public static final float FRAME_DURATION = 0.15f;

    // scales

    public static final float PLAYER_SCALE = 0.7f;
    public static final float ENEMY1_SCALE = 0.3f;
    public static final float ENEMY2_SCALE = 0.3f;

    public static Animation<TextureRegion> getGIF(String fileName) {
        return GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(fileName).read());
    }

}
