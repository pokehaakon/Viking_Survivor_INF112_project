package GameObjects.Actors;

import GameObjects.Actors.ActorAction.ActorAction;
import Animations.AnimationStates;
import Animations.ActorAnimation;
import Animations.AnimationConstants;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.*;

public abstract class Actor <E extends Enum<E>> extends GameObject<E> implements IActor, IAnimation {
    public float speed, HP, damage, armour;

    private List<ActorAction> actions;
    private ActorAnimation animation;


    // unit vector, direction of movement
    public Vector2 velocityVector;


    protected DirectionState directionState;
    public boolean idle;

    protected AnimationStates animationState;


    public Animation<TextureRegion> currentGIF;
    public String currentSpritePath;

    public Actor(String spritePath, BodyFeatures bodyFeatures, float scale) {
        super(spritePath, bodyFeatures, scale);
        velocityVector = new Vector2();
        actions = new ArrayList<>();

    }
    public Actor(){
        velocityVector = new Vector2();
        actions = new ArrayList<>();
    }

    public String getSpritePath() {
        return currentSpritePath;
    }


    /**
     * Defines an action for the actor to perform
     * @param action
     */
    public void setAction(ActorAction action) {
        actions.add(action);

    }
    /**
     * The actor performs its actions
     */
    public void doAction(){
        for(ActorAction action : actions) {
            action.act(this);
        }
    }

    /**
     * Reset actions
     */
    public void resetActions() {
        actions.clear();
    }

    @Override
    public void setAnimation(ActorAnimation animation) {
        this.animation = animation;
    }

    @Override
    public void doAnimation(){
        animation.animate(this);
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

        if(directionState == DirectionState.LEFT) {
            if(!region.isFlipX())
                region.flip(true, false);
        }

        if(directionState == DirectionState.RIGHT) {
            if(!region.isFlipX())
                region.flip(false, false);
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
        currentGIF = AnimationConstants.getGIF(gifPath);
        currentSprite = new Texture(Gdx.files.internal(gifPath));
        currentSpritePath = gifPath;
    }



    @Override
    public void setAnimationState(AnimationStates newState, String gifPath) {
        if(newState != animationState) {
            animationState = newState;
            setNewAnimationGIF(gifPath);
        }
    }

    @Override
    public AnimationStates getAnimationState() {
        return animationState;
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
