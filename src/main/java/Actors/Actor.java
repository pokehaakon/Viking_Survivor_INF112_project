package Actors;

import Actors.IGameObject;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Actor implements IGameObject{

    protected Body body;
    protected Texture spriteImage;

    protected float scale;

    protected Texture sprite;

    private boolean destroyed = false;

    public Actor(Body body, Texture sprite, float scale) {
        this.body = body;
        this.scale = scale;
        this.sprite = sprite;
    }


    @Override
    public void destroy() {
        destroyed = true;
    }


    @Override
    public Body getBody() {
        return body;
    }

    @Override
    public boolean isDestroyed() {
        return destroyed;
    }

    @Override
    public void draw(SpriteBatch batch) {
        Vector2 p = body.getPosition();
        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

}
