package Rendering.Animations.AnimationRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public abstract class Sprites {
    private static final Map<String, Sprite> spriteMap = new HashMap<>();
    public static final String TREE_FILE_PATH = "tree.png";
    public static final float TREE_WIDTH = 1920;

    public static Sprite getSprite(String fileName) {
        if(!spriteMap.containsKey(fileName)) {
            var t = new Texture(Gdx.files.internal(fileName));
            spriteMap.put(fileName, new Sprite(t));
        }
        return spriteMap.get(fileName);
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
