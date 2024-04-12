package GameObjects.Animations.AnimationRendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.HashMap;
import java.util.Map;

public abstract class Sprites {

    public static final String TREE_FILE_PATH = "tree.png";
    public static Map<String, Sprite> spriteMap() {
        Map<String, Sprite> map = new HashMap<>();
        map.put(TREE_FILE_PATH, new Sprite(new Texture(Gdx.files.internal(TREE_FILE_PATH))));
        return map;
    }
}
