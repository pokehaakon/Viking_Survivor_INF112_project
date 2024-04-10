package GameObjects.Actors;

import GameObjects.Actors.ActorAction.ActorAction;
import Animations.MovementState;
import Animations.ActorMovement;
import Animations.AnimationConstants;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static Animations.AnimationConstants.getGIF;

public abstract class Actor<E extends Enum<E>> extends GameObject<E> implements IActor, IAnimation {
    public float speed, HP, damage, armour;

    private Set<ActorAction<E>> actions;
    private ActorMovement movement;


    protected Map<MovementState, String> animations;
    // unit vector, direction of movement
    public Vector2 velocityVector;


    protected DirectionState directionState;
    public boolean idle;

    protected MovementState movementState;


    public Animation<TextureRegion> currentGIF;
    public String currentSpritePath;

    public Actor(String spritePath, BodyFeatures bodyFeatures, float scale) {
        super(spritePath, bodyFeatures, scale);
        velocityVector = new Vector2();
        actions  = new HashSet<>();
        animations = new HashMap<>();
        directionState = DirectionState.RIGHT;

    }
    public Actor(){
        velocityVector = new Vector2();
        actions  = new HashSet<>();
        directionState = DirectionState.RIGHT;

    }

    public void setAnimations(Map<MovementState, String> animations) {
        this.animations = animations;
    }


    /**
     * Defines an action for the actor to perform
     * @param action
     */
    public void setAction(ActorAction<E> action) {
        actions.add(action);

    }
    /**
     * The actor performs its actions
     */
    public void doAction(){
        for(ActorAction<E> action : actions) {
            action.act();
        }
    }

    /**
     * Reset actions
     */
    public void resetActions() {
        actions = new HashSet<>();
    }

    @Override
    public void setAnimation(ActorMovement animation) {
        this.movement = animation;
    }

    @Override
    public void doAnimation(){
        movement.animate(this);
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
    public void setVelocityVector(Vector2 v) {
        velocityVector.set(v);
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


    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {

        TextureRegion region = currentGIF.getKeyFrame(elapsedTime);

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
            region.flip(true, false);
        }


        // for GIF
        currentGIF.setFrameDuration(AnimationConstants.FRAME_DURATION);
        batch.draw(
                region,
                body.getPosition().x,
                body.getPosition().y,
                currentSprite.getWidth()*scale,
                currentSprite.getHeight()*scale

        );

    }

    @Override
    public void setNewAnimationGIF(String gifPath) {
        //currentGIF = getGIF(gifPath);
        //currentSprite = new Texture(Gdx.files.internal(gifPath));
        //currentSpritePath = gifPath;
    }



    @Override
    public void setAnimationState(MovementState newState) {
        if(newState != movementState) {
            movementState = newState;
            currentGIF = getGIF(animations.get(movementState));

        }
    }

    @Override
    public MovementState getAnimationState() {
        return movementState;
    }

    public boolean isIdle() {
        return idle;
    }

    /**
     * Updates the direction state.
     * When the velocity vector has positive value, the direction state is set to RIGHT
     * and vice versa
     */
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



}
