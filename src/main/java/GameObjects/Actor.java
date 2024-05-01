package GameObjects;

import GameObjects.ObjectActions.Action;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;

import java.util.*;

public class Actor extends GameObject implements IActor {
    private float speed;
    private float HP;
    private float damage;
    private float resistance;

    private boolean isUnderAttack;
    protected final List<Action> actions;
    protected final List<Action> dieActions;

    private final Map<Integer, Long> hitByIDs;
    private StatsConstants.Stats stats;
    public Actor(String type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, StatsConstants.Stats stats) {
        super(type, animationHandler, bodyFeatures);
        this.stats = stats;
        speed = stats.SPEED;
        HP = stats.HP;
        damage = stats.DAMAGE;
        resistance = stats.RESISTANCE;

        actions = new ArrayList<>();
        dieActions = new ArrayList<>();
        hitByIDs = new HashMap<>();
    }


    @Override
    public void addAction(Action action) {
        actions.add(action);
    }
    @Override
    public void addAction(Action... actions) {
        this.actions.addAll(List.of(actions));
    }
    @Override
    public void addAction(Collection<Action> actions) {
        this.actions.addAll(actions);
    }

    @Override
    public void doAction(){
        int i = 0;
        //use while loop just in case the list changes!
        while (i < actions.size()) {
            actions.get(i++).act(this);
        }
        for (var iter = hitByIDs.keySet().iterator(); iter.hasNext();) {
            var key = iter.next();
            hitByIDs.put(key, hitByIDs.get(key) - 1);
            if (hitByIDs.get(key) > 0) continue;
            iter.remove();
        }


        updateDirectionState();
        updateAnimationState();
    }

    @Override
    public void addDieAction(Action action) {
        actions.add(action);
    }
    @Override
    public void addDieAction(Action... actions) {
        this.actions.addAll(List.of(actions));
    }
    @Override
    public void addDieAction(Collection<Action> actions) {
        this.actions.addAll(actions);
    }

    @Override
    public void resetActions() {
        actions.clear();
    }

    @Override
    public void resetDieActions() {
        dieActions.clear();
    }

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
        actor.hitByIDs.put(this.getID(), 30L);
        actor.setUnderAttack(true);
        actor.HP -= damage;
    }

    public void setUnderAttack(boolean bool) {
        isUnderAttack = bool;
    }

    @Override
    public boolean attackedBy(Actor actor) {

        return hitByIDs.containsKey(actor.getID());

        // TODO change coolDowns from constant 30 to
        // something like actor.getAttackCoolDown * this.coolDownScalar
    }

    @Override
    public boolean isUnderAttack() {
        return isUnderAttack;
    }



    private void updateDirectionState() {
        float vx = getBody().getLinearVelocity().x;
        if (vx == 0) return;
        setMovingLeft(vx < 0);
    }

    @Override
    public void revive() {
        super.revive();
        HP = stats.HP;
        isUnderAttack = false;

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
        for (var a : dieActions)
            a.act(this);
        destroy();
        resetActions();
        resetDieActions();
        hitByIDs.clear();
    }

    public Map<Integer, Long> getHitByIDs() {
        return hitByIDs;
    }


}
