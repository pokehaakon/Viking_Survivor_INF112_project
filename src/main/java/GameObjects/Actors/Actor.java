package GameObjects.Actors;

import Rendering.Animations.AnimationRendering.AnimationHandler;

import Rendering.Animations.AnimationState;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Actor<E extends Enum<E>> extends GameObject<E> implements IActor {
    public float speed, HP, damage, armour;

    protected List<ActorAction> actions;
    public boolean underAttack = false;
    private long lastAttackedTime;

    public Actor(E type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(type, animationHandler, bodyFeatures, scale);

        //velocityVector = new Vector2();
        actions  = new ArrayList<>();
        directionState = DirectionState.RIGHT;
    }

    /**
     * Defines an action for the actor to perform
     * @param action
     */
    public <T extends Actor<E>> void setAction(ActorAction<T> action) {
        actions.add(action);
    }

    /**
     * Defines an actions for the actor to perform
     * @param actions
     */
    public <T extends Actor<E>> void setActions(List<ActorAction<T>> actions) {
        this.actions.addAll(actions);
    }

    /**
     * Defines an actions for the actor to perform
     * @param actions
     */
    @SafeVarargs
    public final <T extends Actor<E>> void setActions(ActorAction<T>... actions) {
        this.actions.addAll(List.of(actions));
    }

    /**
     * The actor performs its actions
     */
    @SuppressWarnings({"unchecked"})
    public void doAction(){
        int i = 0;
        while (i < actions.size()) {
            actions.get(i++).act(this);
        }

        updateDirectionState();
        updateAnimationState();
    }

    /**
     * Reset actions
     */
    public void resetActions() {
        actions.clear();
    }

    /**
     * Resets the actor and calls GameObject::destroy
     */
    public void kill() {
        if (destroyed) return;
        destroy();
        resetActions();
    }

    @Override
    public void setSpeed(float speedMultiplier) {
        speed *= speedMultiplier;
    }

    private void updateDirectionState() {
        float vx = body.getLinearVelocity().x;
        if (vx == 0) return;
        directionState = vx < 0 ? DirectionState.LEFT : DirectionState.RIGHT;
    }

    private void updateAnimationState() {
        var newState = body.getLinearVelocity().len() == 0.0f ? AnimationState.IDLE : AnimationState.MOVING;

        if(animationHandler.getAnimationState() != newState) {
            setAnimationState(newState);
            setAnimation(newState);
        }
    }

    @Override
    public void attack(Actor<?> actor, float damage) {
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
}
