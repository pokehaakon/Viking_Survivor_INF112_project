package GameObjects.AnimationRendering;

import GameObjects.AnimationRendering.GifPair;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static Animations.AnimationConstants.getGIF;

public class GIFS {

    public static final GifPair RAVEN_GIF = new GifPair(getGIF("raven.gif"),flippedGIF(getGIF("raven.gif")));
    public static final GifPair ORC_GIF = new GifPair(getGIF("orc.gif"),flippedGIF(getGIF("orc.gif")));
    public static final GifPair WOLF_GIF = new GifPair(getGIF("wolf.gif"),flippedGIF(getGIF("wolf.gif")));
    public static final GifPair PLAYER_MOVING_GIF = new GifPair(getGIF("vikingleft-crop.gif"),flippedGIF(getGIF("vikingleft-crop.gif")));
    public static final GifPair PLAYER_IDLE_GIF = new GifPair(getGIF("viking_idle_left-crop.gif"),flippedGIF(getGIF("viking_idle_left-crop.gif")));


    public static Animation<TextureRegion> flippedGIF(Animation<TextureRegion> originalGIF) {
        Object[] keyFrames = originalGIF.getKeyFrames();
        TextureRegion[] textureRegions = new TextureRegion[keyFrames.length];
        for (int i = 0; i < keyFrames.length; i++) {
            TextureRegion originalRegion = (TextureRegion) keyFrames[i];
            TextureRegion flippedRegion = new TextureRegion(originalRegion);
            flippedRegion.flip(true, false); // Flip horizontally
            textureRegions[i] = flippedRegion;
        }

        Animation<TextureRegion> flippedGIF = new Animation<>(originalGIF.getFrameDuration(), textureRegions);
        flippedGIF.setPlayMode(originalGIF.getPlayMode());

        return flippedGIF;
    }


}

