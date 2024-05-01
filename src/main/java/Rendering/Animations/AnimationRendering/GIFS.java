package Rendering.Animations.AnimationRendering;

import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.google.common.annotations.VisibleForTesting;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;


public abstract class GIFS {
    static private final Map<String, GifPair> gifPairMap = new HashMap<>();
    private static final Queue<Runnable> todos = new ArrayDeque<>();


    protected static final float FRAME_DURATION = 0.15f;

    // enemies
//    public static final String RAVEN_FILE_PATH = "raven.gif";
//    public static final String ORC_FILE_PATH = "orc.gif";
//
//    public static final String WOLF_FILE_PATH = "wolf.gif";
//    public static final float PLAYER_WIDTH = 600,RAVEN_WIDTH = 600, WOLF_WIDTH = 600, ORC_WIDTH = 600, PICKUP_ORB_WIDTH = 600, KNIFE_WIDT = 600;
//    public static final String PLAYER_MOVING_FILE_PATH = "vikingleft-crop.gif";
//
//    public static final String PLAYER_IDLE_FILE_PATH = "viking_idle_left-crop.gif";
//
//    public static final String PICK_UP_ORB_FILE_PATH= "pickupOrb.gif";
//
//    public static final String KNIFE_FILE_PATH= "knife.gif";


//    // scale
//    public static final float PLAYER_SCALE = 0.7f;
//    public static final float RAVEN_SCALE = 0.3f;
//    public static final float ORC_SCALE = 0.3f;
//
//    public static final float PICKUPORB_SCALE = 0.3f;


    public static GifPair getGIF(String path, Consumer<GifPair> location) {
        if(!gifPairMap.containsKey(path)) {
            var file = Gdx.files.internal(path);
            gifPairMap.put(path, null);
            todos.add(() -> {
                var t = new Texture(file);
                gifPairMap.put(path, GifPairImplementation.of(path));
                location.accept(gifPairMap.get(path));
            });
        }
        if(gifPairMap.get(path) == null) {
            todos.add(() -> {
                location.accept(gifPairMap.get(path));
            });
        }
        return gifPairMap.get(path);
    }

    public static void loadWaiting() {
        while (!todos.isEmpty())
            todos.poll().run();
    }

    public static void clear() {
        for (var e : gifPairMap.entrySet()) {
            e.getValue().left().getKeyFrame(0).getTexture().dispose();
            e.getValue().right().getKeyFrame(0).getTexture().dispose();
        }
        gifPairMap.clear();
    }

    @VisibleForTesting
    protected record GifPairImplementation(Animation<TextureRegion> left, Animation<TextureRegion> right) implements GifPair {
        static GifPair of(Animation<TextureRegion> left) {
            Object[] keyFrames = left.getKeyFrames();
            TextureRegion[] textureRegions = new TextureRegion[keyFrames.length];
            for (int i = 0; i < keyFrames.length; i++) {
                TextureRegion originalRegion = (TextureRegion) keyFrames[i];
                TextureRegion flippedRegion = new TextureRegion(originalRegion);
                flippedRegion.flip(true, false); // Flip horizontally
                textureRegions[i] = flippedRegion;
            }

            Animation<TextureRegion> right = new Animation<>(left.getFrameDuration(), textureRegions);
            right.setPlayMode(left.getPlayMode());

            return new GifPairImplementation(left, right);
        }

        static GifPair of(String fileName) {
            return of(GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(fileName).read()));
        }
    }

}

