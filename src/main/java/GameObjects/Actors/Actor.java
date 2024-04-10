package GameObjects.Actors;

import Animations.AnimationState;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import Rendering.AnimationRender;
import com.badlogic.gdx.math.Vector2;

import java.util.HashSet;
import java.util.Set;

public abstract class Actor<E extends Enum<E>> extends GameObject<E> implements IActor {
    public float speed, HP, damage, armour;

    private Set<ActorAction> actions;


    // unit vector, direction of movement
    public Vector2 velocityVector;

    public boolean idle;




    public Actor(E type,AnimationRender render, BodyFeatures bodyFeatures, float scale) {
        super(type,render,bodyFeatures,scale);
        velocityVector = new Vector2();

        actions  = new HashSet<>();
        directionState = DirectionState.RIGHT;
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
    }

    @Override
    public void setSpeed(int speedMultiplier) {
        speed *= speedMultiplier;
    }


    public boolean isIdle() {
        return idle;
    }

    /**
     * Updates the direction state.
     * When the velocity vector has positive value, the direction state is set to RIGHT
     * and vice versa
     */
    public void updateDirectionState() {
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

    public void updateMovement() {
        AnimationState newState;
        if(idle) {
            newState = AnimationState.IDLE;
        }
        else {
            newState = AnimationState.MOVING;
        }
        setAnimationState(newState);
    }




}
