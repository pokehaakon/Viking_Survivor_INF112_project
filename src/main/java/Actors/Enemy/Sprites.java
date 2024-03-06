package Actors.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Sprites {

    public static final String ENEMY_1_SPRITE = "img.png";
    public static final String ENEMY_2_SPRITE = "img_3.png";

    public static Sprite getSprite(String spriteName, int width, int height) {
        Texture spriteImage = new Texture(Gdx.files.internal(spriteName));
        Sprite sprite = new Sprite(spriteImage);
        sprite.setSize(width,height);
        return sprite;
    }
}
