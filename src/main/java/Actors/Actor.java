package Actors;

import Actors.Enemy.Sprites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.*;
import java.util.function.Consumer;

public abstract class Actor implements IGameObject {
    public int HP;
    //public int speed;
    public int speedX, speedY;
    public int damage;
    private boolean destroyed = false;
    public Texture spriteImage;
    public Rectangle.Float hitBox;
    public int x, y;
    public Sprite sprite ;

    private Stats stats;


    public Actor(Stats stats) {
        this.stats = stats;
        HP = stats.HP;
        damage = stats.damage;
        speedX = stats.speedX;
        speedY = stats.speedY;
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
    public void initialize(String spriteName,int x, int y) {
        this.x = x;
        this.y = y;
        spriteImage = new Texture(Gdx.files.internal(spriteName));
        sprite = new Sprite(spriteImage);
        sprite.setSize(100,100);

        sprite = Sprites.getSprite(spriteName, 100,100);

        hitBox = new Rectangle.Float();
        hitBox.x = x;
        hitBox.y =  y;
        hitBox.width = sprite.getWidth();
        hitBox.height =  sprite.getHeight();

    }


    public void updateHitBox() {
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
