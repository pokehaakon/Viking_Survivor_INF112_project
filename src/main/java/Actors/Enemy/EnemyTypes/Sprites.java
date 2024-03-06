package Actors.Enemy.EnemyTypes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Sprites {

    public static final String ENEMY_1_PNG = "img.png";
    public static final String ENEMY_2_PNG = "img_3.png";


    public static Sprite getSprite(String name, int width, int height) {
        Texture spriteImage = new Texture(Gdx.files.internal(name));
        Sprite sprite = new Sprite(spriteImage);
        sprite.setSize(width, height);
        return sprite;
    }
}
