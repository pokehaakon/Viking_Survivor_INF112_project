package Actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class Actor implements IGameObject,IActor{

    protected float HP, speed, damage, armour;

    protected Body body;
    protected float scale;

    protected Animation<TextureRegion> sprite;
    protected TextureRegion currentFrame;
    private float animationTime = 0;
    private boolean isSpriteFlipped = false;
    private boolean destroyed = false;

    // unit vector, direction of movement
    protected Vector2 velocityVector;

    public Actor(Body body, Animation<TextureRegion> sprite, float scale) {
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
    public void draw(SpriteBatch batch, float delta){
        // Added delta as a parameter to draw() to animate the sprite
        animationTime += delta;
        currentFrame = sprite.getKeyFrame(animationTime);
        Vector2 p2 = body.getPosition();

        batch.draw(currentFrame,p2.x,p2.y, sprite.getKeyFrame(0).getRegionWidth()*scale,  sprite.getKeyFrame(0).getRegionHeight()*scale);
    }
    @Override
    public void resetVelocity(){
        velocityVector = new Vector2();
    }

    @Override
    public void setVelocityVector(float x, float y) {
        velocityVector.x += x;
        velocityVector.y += y;
    }
    public Vector2 getVelocityVector(){
        return velocityVector;
    }

    @Override
    public void move(){
        velocityVector.setLength(speed);
        body.setLinearVelocity(velocityVector);
    }

    @Override
    public void setSpeed(int speedMultiplier) {
        speed *= speedMultiplier;
    }

}
