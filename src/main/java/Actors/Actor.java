package Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Actor implements IGameObject {
    private boolean destroyed = false;
    private String name;
    private Texture spriteImage;
    public Rectangle spriteRect;
    public int x, y, width, height;


    public Actor(String name) {
        this.name = name;

    }
    public Actor() {

    }

    public String getName() {
        return name;
    }

    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    void handleDamage(int dmg) {
        //TODO
    }

    void doMove(Consumer<Actor> a)  { //?
        //TODO
    }

    @Override
    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);

    }

    @Override
    public void init(String spriteName, int x, int y) {

        spriteImage = new Texture(Gdx.files.internal(spriteName));
        spriteRect = new Rectangle();
        spriteRect.x = x;
        spriteRect.y =  y;
        spriteRect.width = spriteImage.getWidth();
        spriteRect.height = spriteImage.getHeight();
    }
    //enum collisionDetected(obj 1, obj2) //TODO
}
