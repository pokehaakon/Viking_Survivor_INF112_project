package Rendering.Animations.AnimationRendering;

import Tools.ExcludeFromGeneratedCoverage;
import Tools.GifDecoder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

@ExcludeFromGeneratedCoverage
public abstract class GIFS {
    static private final Map<String, GifPair> gifPairMap = new HashMap<>();
    private static final Queue<Runnable> todos = new ArrayDeque<>();


    protected static final float FRAME_DURATION = 0.15f;


    public static GifPair getGIF(String path, Consumer<GifPair> location) {
        if(!gifPairMap.containsKey(path)) {
            gifPairMap.put(path, null);
            todos.add(() -> {
                gifPairMap.put(path, GifPairImplementation.of(path));
                location.accept(gifPairMap.get(path));
            });
        }
        if(gifPairMap.get(path) == null) {
            todos.add(() -> location.accept(gifPairMap.get(path)));
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

