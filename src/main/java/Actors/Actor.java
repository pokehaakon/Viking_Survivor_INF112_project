package Actors;

import Actors.ActorAction.ActorAction;
import Animations.AnimationStates;
import Animations.ActorAnimation;
import Animations.AnimationConstants;
import FileHandling.GdxFileHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;

import static Tools.BodyTool.createEnemyBody;

public abstract class Actor implements IGameObject,IActor, IAnimation{

    public float HP, speed, damage, armour;

    protected Body body;

    protected float scale;

    public Shape shape;


    private Set<ActorAction> actions;
    private ActorAnimation animation;
    private boolean destroyed = false;


    // unit vector, direction of movement
    public Vector2 velocityVector;


    protected DirectionState directionState;
    public boolean idle;

    protected AnimationStates animationState;

    public Texture currentSprite;

    public Animation<TextureRegion> currentGIF;



    public Actor(Body body, String spawnGIF, float scale) {
        this.body = body;
        this.scale = scale;
        currentSprite = new Texture(Gdx.files.internal(spawnGIF));
        currentGIF = AnimationConstants.getGIF(spawnGIF);

        velocityVector = new Vector2();
        actions  = new HashSet<>();

    }

    public Actor(String spawnGIF, float scale) {
        this.scale = scale;
        //currentSprite = new Texture(Gdx.files.internal(spawnGIF));
        //currentGIF = AnimationConstants.getGIF(spawnGIF);
        velocityVector = new Vector2();
        actions  = new HashSet<>();

    }

    public Actor() {

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
        actions = new HashSet<>();
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
    public void destroy() {
        destroyed = true;
    }

    public void revive() {
        destroyed = false;
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
       batch.draw(currentSprite,p.x,p.y, currentSprite.getWidth()*scale,  currentSprite.getHeight()*scale);
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


    @Override
    public void draw(SpriteBatch batch, float elapsedTime) {
        // for GIF
        currentGIF.setFrameDuration(AnimationConstants.FRAME_DURATION);
        batch.draw(
                currentGIF.getKeyFrame(elapsedTime),
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


    public void setPosition(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }



}
