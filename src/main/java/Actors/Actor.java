package Actors;

import Actors.ActorAction.ActorAction;
import Animations.AnimationStates;
import Animations.ActorAnimation;
import Animations.GIF;
import com.badlogic.gdx.Gdx;
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

    private ActorAction action;
    private ActorAnimation animation;
    private boolean destroyed = false;


    // unit vector, direction of movement
    public Vector2 velocityVector;


    protected DirectionState directionState;
    public boolean idle;

    protected AnimationStates animationState;

    public Texture currentSprite;

    Animation<TextureRegion> currentGIF;

    Texture sprite;


    public Actor(Body body, String spawnGIF, float scale) {
        this.body = body;
        this.scale = scale;
        currentSprite = new Texture(Gdx.files.internal(spawnGIF));
        currentGIF = GIF.getGIF(spawnGIF);
    }

    /**
     * Defines a set of actions for the actor
     * @param action the ActorAction object which represents the action
     */
    public void setAction(ActorAction action) {
        this.action = action;
    }

    public void setAnimation(ActorAnimation animation) {
        this.animation = animation;
    }

    public void updateAnimation(){
        animation.animate(this);
    }


    /**
     * The actor performs its actions
     */
    public void updateAction(){
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
    public void move(){
        velocityVector.setLength(speed);
        body.setLinearVelocity(velocityVector);

        updateDirectionState();

    }

    @Override
    public void setSpeed(int speedMultiplier) {
        speed *= speedMultiplier;
    }

    public void draw(SpriteBatch batch, float elapsedTime) {

        Vector2 bodyPosition = body.getPosition();
        currentGIF.setFrameDuration(GIF.FRAME_DURATION);
        batch.draw(
                currentGIF.getKeyFrame(elapsedTime),
                bodyPosition.x,
                bodyPosition.y,
                currentSprite.getWidth()*scale,
                currentSprite.getHeight()*scale

        );
    }

    public void setNewAnimationGIF(String gifPath) {
        currentGIF = GIF.getGIF(gifPath);
        currentSprite = new Texture(gifPath);
    }


    private void updateDirectionState() {
        DirectionState newState;
        if (velocityVector.x > 0) {
            newState = DirectionState.RIGHT;
        }
        else if (velocityVector.x < 0) {
            newState = DirectionState.LEFT;
        }
        else {
            newState = directionState;
        }

        if(newState != directionState) {
            directionState = newState;
        }
    }



    public DirectionState getDirectionState() {
        return directionState;
    }

    public void setAnimationState(AnimationStates newState, String gifPath) {
        if(newState != animationState) {
            animationState = newState;
            setNewAnimationGIF(gifPath);
        }
    }

    public AnimationStates getAnimationState() {
        return animationState;
    }

    public boolean isIdle() {
        return idle;
    }
}
