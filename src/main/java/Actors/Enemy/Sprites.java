package Actors.Enemy;

import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Sprites {

    // sprites
    public static final String PLAYER_PNG = "vikingright.gif";
    public static final String ENEMY_1_PNG = "img.png";
    public static final String ENEMY_2_PNG = "img_3.png";

    public static final Animation<TextureRegion> PLAYER_RIGHT = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingright.gif").read());
    public static final Animation<TextureRegion> PLAYER_LEFT = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("vikingleft.gif").read());
    // scales

    public static final float PLAYER_SCALE = 0.5f;
    public static final float ENEMY1_SCALE = 0.1f;
    public static final float ENEMY2_SCALE = 0.1f;

}
