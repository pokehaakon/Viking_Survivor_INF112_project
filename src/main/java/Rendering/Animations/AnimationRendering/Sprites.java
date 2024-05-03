package Rendering.Animations.AnimationRendering;

import Tools.ExcludeFromGeneratedCoverage;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.function.Consumer;

@ExcludeFromGeneratedCoverage
public abstract class Sprites {
    private static final Map<String, Sprite> spriteMap = new HashMap<>();
    private static final Queue<Runnable> todos = new ArrayDeque<>();

    /**
     * Returns the sprite at "fileName", if it is not loaded it waits until render loop
     * @param fileName the file name of the wanted sprite
     * @param location Consumer that takes the sprite when it is eventually loaded.
     * @return Either returns the sprite, or null if the sprite was not loaded
     */
    public static Sprite getSprite(String fileName, Consumer<Sprite> location) {
        if(!spriteMap.containsKey(fileName)) {
            var file = Gdx.files.internal(fileName);
            spriteMap.put(fileName, null);
            todos.add(() -> {
                var t = new Texture(file);
                spriteMap.put(fileName, new Sprite(t));
                location.accept(spriteMap.get(fileName));
            });
        }
        if(spriteMap.get(fileName) == null) {
            todos.add(() -> location.accept(spriteMap.get(fileName)));
        }
        return spriteMap.get(fileName);
    }

    public static void loadWaiting() {
        while (!todos.isEmpty())
            todos.poll().run();
    }

    /**
     * Clears the 'spriteMap'
     */
    public static void clear() {
        for (var e : spriteMap.entrySet()) {
            e.getValue().getTexture().dispose();
        }
        //spriteMap.get("").getTexture().dispose();
        spriteMap.clear();
    }
}
