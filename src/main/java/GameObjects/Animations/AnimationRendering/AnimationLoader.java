package GameObjects.Animations.AnimationRendering;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.gifMap;
import static GameObjects.Animations.AnimationRendering.Sprites.spriteMap;

public class AnimationLoader {

    Map<String, GifPair> gifs;

    Map<String, Sprite> sprites;
    public AnimationLoader() {
        gifs = gifMap();
        sprites = spriteMap();
    }

    public GifPair getGif(String filePath) {
        return gifs.get(filePath);
    }


    public Sprite getSprite(String filePath) {
        return sprites.get(filePath);
    }

}
