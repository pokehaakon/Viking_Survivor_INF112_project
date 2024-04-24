package Rendering.Animations.AnimationRendering;

import VikingSurvivor.app.HelloWorld;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.*;
import java.util.function.Consumer;

public abstract class Sprites {
    private static final Map<String, Sprite> spriteMap = new HashMap<>();
    public static final String TREE_FILE_PATH = "tree.png";
    public static final float TREE_WIDTH = 1920;

    private static final Queue<Runnable> todos = new ArrayDeque<>();

    /**
     * Returns the sprite at "fileName", if it is not loaded it waits until render loop
     * @param fileName
     * @param location Consumer that takes the sprite eventually.
     * @return
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
        return spriteMap.get(fileName);
    }

    public static void loadWaiting() {
        while (!todos.isEmpty())
            todos.poll().run();
    }
//    public static void initialize() {
//        var t = new Texture(Gdx.files.internal(TREE_FILE_PATH));
//        spriteMap.put(TREE_FILE_PATH, new Sprite(t));
//        t.dispose();
//    }

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
