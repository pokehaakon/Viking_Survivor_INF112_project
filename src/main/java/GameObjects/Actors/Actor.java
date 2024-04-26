package GameObjects.Actors;

import GameObjects.Actors.Stats.StatsConstants;
import Rendering.Animations.AnimationRendering.AnimationHandler;

import Rendering.Animations.AnimationState;
import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;

import java.util.*;

public class Actor extends GameObject implements IActor {
    private float speed;
    private float HP;
    private float damage;
    private float resistance;

    protected final List<ActorAction> actions;
    protected final List<ActorAction> dieActions;
    public boolean underAttack = false;

    private final Map<Integer, Long> hitByIDs;

    public Actor(String type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, StatsConstants.Stats stats) {
        super(type, animationHandler, bodyFeatures);

        speed = stats.SPEED;
        HP = stats.HP;
        damage = stats.DAMAGE;
        resistance = stats.RESISTANCE;

        actions = new ArrayList<>();
        dieActions = new ArrayList<>();
        hitByIDs = new HashMap<>();
    }


    @Override
    public void addAction(ActorAction action) {
        actions.add(action);
    }
    @Override
    public void addAction(ActorAction... actions) {
        this.actions.addAll(List.of(actions));
    }
    @Override
    public void addAction(Collection<ActorAction> actions) {
        this.actions.addAll(actions);
    }

    @Override
    public void doAction(){
        int i = 0;
        //use while loop just in case the list changes!
        while (i < actions.size()) {
            actions.get(i++).act(this);
        }

        updateDirectionState();
        updateAnimationState();
    }

    @Override
    public void resetActions() {
        actions.clear();
    }

//    @Override
//    public void dieAction() {
//        int i = 0;
//        //use while loop just in case the list changes!
//        while (i < dieActions.size()) {
//            dieActions.get(i++).act(this);
//        }
//
//        updateDirectionState();
//        updateAnimationState();
//    }
//
//    @Override
//    public void addDieAction(ActorAction action) {
//        dieActions.add(action);
//    }
//
//    @Override
//    public void addDieAction(ActorAction... actions) {
//        dieActions.addAll(List.of(actions));
//    }
//
//    @Override
//    public void addDieAction(Collection<ActorAction> actions) {
//        dieActions.addAll(actions);
//    }
//
//    @Override
//    public void resetDieActions() {
//        dieActions.clear();
//    }

    @Override
    public void setSpeed(float speed) {this.speed = speed; }
    @Override
    public float getSpeed() {return speed;}

    @Override
    public float getHP() {return HP;}
    @Override
    public void setHP(float hp) {HP = hp;}

    @Override
    public float getDamage() {return damage;}
    @Override
    public void setDamage(float damage) {this.damage = damage;}

    public float getResistance() {return resistance;}
    public void setResistance(float armour) {this.resistance = armour;}


    @Override
    public void attack(Actor actor) {
        if(actor.attackedBy(this)) return;
        // TODO add attack action
        actor.HP -= damage;
    }

    @Override
    public boolean attackedBy(Actor actor) {
        if (hitByIDs.containsKey(actor.getID())) return false;
        hitByIDs.put(actor.getID(), 30L);
        // TODO change coolDowns from constant 30 to
        // something like actor.getAttackCoolDown * this.coolDownScalar
        return true;
    }

    @Override
    public boolean isUnderAttack() {
        return underAttack;
    }


    private void updateDirectionState() {
        float vx = getBody().getLinearVelocity().x;
        if (vx == 0) return;
        setDirectionState(vx < 0 ? DirectionState.LEFT : DirectionState.RIGHT);
    }

    private void updateAnimationState() {
        var newState = getBody().getLinearVelocity().len() == 0.0f ? AnimationState.IDLE : AnimationState.MOVING;

        if(animationHandler.getAnimationState() != newState) {
            setAnimationState(newState);
            setAnimation(newState);
        }
    }

    /**
     * Resets the actor and calls GameObject::destroy
     */
    public void kill() {
        if (isDestroyed()) return;
        destroy();
        resetActions();
        // resetDieActions();
    }


}
