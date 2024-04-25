package GameObjects.Actors;

import GameObjects.Animations.AnimationRendering.AnimationHandler;

import GameObjects.Animations.AnimationState;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public abstract class Actor<E extends Enum<E>> extends GameObject<E> implements IActor {
    public float speed, HP, damage, armour;

    private long lastAttack;

    private boolean isKilled = false;

    // unit vector, direction of movement
    public Vector2 velocityVector;

    public boolean idle;

    public boolean underAttack = false;


    private long lastAttackedTime;

    public Actor(E type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(type,animationHandler,bodyFeatures,scale);
        velocityVector = new Vector2();


        directionState = DirectionState.RIGHT;
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


    @Override
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

    @Override
    public void updateAnimationState() {
        AnimationState newState;
        if(idle) {
            newState = AnimationState.IDLE;
        }
        else {
            newState = AnimationState.MOVING;
        }
        if(animationHandler.getAnimationState() != newState) {
            setAnimationState(newState);
            setAnimation(newState);
        }


    }



    @Override
    public void attack(Actor actor,float damage) {

        actor.HP -= damage;
        actor.setUnderAttack(true);
        actor.setLastAttackedTime(TimeUtils.millis());
    }

    @Override
    public boolean isUnderAttack() {
        return underAttack;
    }

    @Override
    public long getLastAttackedTime() {
        return lastAttackedTime;
    }

    @Override
    public void setLastAttackedTime(long newAttack) {
        lastAttackedTime = newAttack;
    }

    @Override
    public void setUnderAttack(boolean bool) {
        underAttack = bool;
    }

    public long getLastAttack() {
        return lastAttack;
    }

    public void setLastAttack(long newAttack) {
        lastAttack = newAttack;
    }

    public boolean isKilled() {
        return isKilled;
    }

    public void kill() {
        isKilled = true;
    }

    @Override
    public void revive() {
        destroyed = false;
        isKilled = false;
    }


}
