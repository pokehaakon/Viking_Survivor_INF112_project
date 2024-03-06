package Actors;

import Actors.Enemy.EnemyTypes.Sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Actor implements IGameObject {
    public int HP;
    public float speedX, speedY;
    public int damage;
    private boolean destroyed = false;
    public Texture spriteImage;
    public Rectangle.Float hitBox;
    public float x, y;
    public float width, height;
    public Sprite sprite ;

    private Stats stats;


    public Actor(Stats stats) {
        this.stats = stats;
        HP = stats.HP;
        damage = stats.damage;
        speedX = stats.speed;
        speedY = stats.speed;
    }
    public Actor() {

    }


    @Override
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
        spriteBatch.draw(sprite, hitBox.x, hitBox.y, hitBox.width, hitBox.height);
        updateHitBox();
    }

    @Override
    public void initialize(String spriteName,float x, float y, float width, float height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        sprite = Sprites.getSprite(spriteName, (int)width, (int)height);

        hitBox = new Rectangle.Float();
        hitBox.x = x;
        hitBox.y =  y;
        hitBox.width = sprite.getWidth();
        hitBox.height =  sprite.getHeight();

    }


    private void updateHitBox() {
        hitBox.x = this.x;
        hitBox.y = this.y;
    }

    @Override
    public void attack(Actor actor) {

    }

    @Override
    public boolean collision(Actor actor) {

        return this.hitBox.intersects(actor.hitBox);
    }


}
