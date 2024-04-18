package GameObjects.Animations.AnimationRendering;

import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;


public abstract class GIFS {

    public static final float FRAME_DURATION = 0.15f;

    // enemies
    public static final String RAVEN_FILE_PATH = "raven.gif";
    public static final String ORC_FILE_PATH = "orc.gif";

    public static final String WOLF_FILE_PATH = "wolf.gif";
    public static final float PLAYER_WIDTH = 600,RAVEN_WIDTH = 600, WOLF_WIDTH = 600, ORC_WIDTH = 600, PICKUP_ORB_WIDTH = 600, KNIFE_WIDT = 600;
    public static final String PlAYER_MOVING_FILE_PATH = "vikingleft-crop.gif";

    public static final String PlAYER_IDLE_FILE_PATH = "viking_idle_left-crop.gif";

    public static final String PICK_UP_ORB_FILE_PATH= "pickupOrb.gif";

    public static final String KNIFE_FILE_PATH= "knife.gif";


    // scale
    public static final float PLAYER_SCALE = 0.7f;
    public static final float RAVEN_SCALE = 0.3f;
    public static final float ORC_SCALE = 0.3f;

    public static final float PICKUPORB_SCALE = 0.3f;

    public static Map<String, GifPair> gifMap(){
        Map<String, GifPair> map = new HashMap<>();
        map.put(RAVEN_FILE_PATH,new GifPair(getGIF(RAVEN_FILE_PATH),flippedGIF(getGIF(RAVEN_FILE_PATH))));
        map.put(ORC_FILE_PATH, new GifPair(getGIF(ORC_FILE_PATH),flippedGIF(getGIF(ORC_FILE_PATH))));
        map.put(WOLF_FILE_PATH,new GifPair(getGIF(WOLF_FILE_PATH),flippedGIF(getGIF(WOLF_FILE_PATH))));
        map.put(PlAYER_MOVING_FILE_PATH, new GifPair(getGIF(PlAYER_MOVING_FILE_PATH),flippedGIF(getGIF(PlAYER_MOVING_FILE_PATH))));
        map.put(PlAYER_IDLE_FILE_PATH, new GifPair(getGIF(PlAYER_IDLE_FILE_PATH),flippedGIF(getGIF(PlAYER_IDLE_FILE_PATH))));
        map.put(PICK_UP_ORB_FILE_PATH,new GifPair(getGIF(PICK_UP_ORB_FILE_PATH),flippedGIF(getGIF(PICK_UP_ORB_FILE_PATH))));
        map.put(KNIFE_FILE_PATH,new GifPair(getGIF(KNIFE_FILE_PATH),flippedGIF(getGIF(KNIFE_FILE_PATH))));
        return map;
    }


    /**
     * Flips original gif in horisontal direction
     * @param originalGIF
     * @return
     */
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

    /**
     * Returns gif from GifDecoder
     * @param fileName filename of gif
     * @return
     */
    public static Animation<TextureRegion> getGIF(String fileName) {
        return GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(fileName).read());
    }


}

