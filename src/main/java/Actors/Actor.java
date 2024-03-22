package Actors;

import Actors.ActorAction.ActorAction;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import javax.swing.event.AncestorEvent;

public abstract class Actor implements IGameObject,IActor{

    protected float HP, speed, damage, armour;

    protected Body body;

    protected float scale;

    protected Texture sprite;

    private ActorAction action;
    private boolean destroyed = false;

    // unit vector, direction of movement
    public Vector2 velocityVector;

    private Animation<TextureRegion> currentAnimation;

    public Actor(Body body, Texture sprite, float scale) {
        this.body = body;
        this.scale = scale;
        this.sprite = sprite;
    }

    /**
     * Defines a set of actions for the actor
     * @param action the ActorAction object which represents the action
     */
    public void setAction(ActorAction action) {
        this.action = action;
    }

    /**
     * The actor performs its actions
     */
    public void step(){
        action.act(this);
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

    @Override
    public void resetVelocity(){
        velocityVector = new Vector2();
    }

    @Override
    public void setVelocityVector(float x, float y) {
        velocityVector.x += x;
        velocityVector.y += y;
    }



    @Override
    public void moveWithConstantSpeed(){
        velocityVector.setLength(speed);
        body.setLinearVelocity(velocityVector);
    }

    @Override
    public void setSpeed(int speedMultiplier) {
        speed *= speedMultiplier;
    }

    public void draw(SpriteBatch batch, float elapsedTime) {
        Vector2 p = body.getPosition();
        currentAnimation.setFrameDuration(0.2f);

        batch.draw(currentAnimation.getKeyFrame(elapsedTime), p.x, p.y, 500.0f, 500.0f);
        //batch.d
//        Vector2 p = body.getPosition();
//        batch.draw(sprite,p.x,p.y, sprite.getWidth()*scale,  sprite.getHeight()*scale);
    }

    public void setNewAnimation(Animation<TextureRegion> newAnimation) {
        currentAnimation = newAnimation;
    }

}
