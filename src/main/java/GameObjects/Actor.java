package GameObjects;

import GameObjects.ObjectActions.Action;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import Tools.ExcludeFromGeneratedCoverage;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


import java.util.*;

import static Tools.FilterTool.isInCategory;

public class Actor extends GameObject implements IActor {
    private float speed;
    private float HP;
    private float damage;
    private float resistance;

    private boolean startTempChange = false;
    private boolean temporaryActionCountDown = false;

    //used to store the original actions if action change
    private List<Action> originalActions = new ArrayList<>();

    private int framesLeftOfActionChange;
    private boolean inCoolDown = false;

    private int framesLeftOfCoolDown;
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

   public List<Action> getActions() {
        return actions;
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

        // cool down feature
        inCoolDown = (inCoolDown && --framesLeftOfCoolDown >= 0);

        if(temporaryActionCountDown) doTemporaryActionChange();

    }


    @Override
    public void setTemporaryActionChange(int frameDuration, Action... tempActions) {
        if(frameDuration <= 0) {
            throw new IllegalArgumentException("Duration must be a positive number of frames");
        }
        if(originalActions.isEmpty()) originalActions.addAll(actions);

        resetActions();
        addAction(tempActions);
        temporaryActionCountDown = true;
        framesLeftOfActionChange = frameDuration;
    }

    /**
     * Starts a temporary action change. Stores the original action in its own list.
     */
    private void doTemporaryActionChange() {
        if(framesLeftOfActionChange-- <= 0) {;
            resetActions();
            addAction(originalActions);
            originalActions.clear();
            temporaryActionCountDown = false;
        }

    }


    @Override
    public void addDieAction(Action action) {
        dieActions.add(action);
    }
    @Override
    public void addDieAction(Action... actions) {
        this.dieActions.addAll(List.of(actions));
    }
    @Override
    public void addDieAction(Collection<Action> actions) {
        this.dieActions.addAll(actions);
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
    public void startCoolDown(int frameDuration) {
        if(frameDuration <= 0) {
            throw new IllegalArgumentException("Frame duration must be a positive number of frames");
        }
        inCoolDown = true;
        framesLeftOfCoolDown = frameDuration;
    }


    @Override
    public void attack(Actor actor) {
        actor.hitByIDs.put(this.getID(), 30L);
        actor.HP -= damage;
    }




    @Override
    public boolean isInCoolDown() {
        return inCoolDown;
    }

    @Override
    public boolean attackedBy(Actor actor) {

        return hitByIDs.containsKey(actor.getID());

    }

    @Override
    public boolean isUnderAttack() {

        return !hitByIDs.isEmpty();

    }


    /**
     * updates the boolean movingLeft
     */
    public void updateDirectionState() {
        float vx = getBody().getLinearVelocity().x;
        if (vx == 0) return;
        setMovingLeft(vx < 0);
    }

    @Override
    public void revive() {
        super.revive();
        HP = stats.HP;

        hitByIDs.clear();
    }

    /**
     * Updates the animation if needed
     */
    public void updateAnimationState() {
        var newState = getBody().getLinearVelocity().len() == 0.0f ? AnimationState.IDLE : AnimationState.MOVING;

        if(animationHandler.getAnimationState() != newState) {
            setAnimationState(newState);
            setAnimation(newState);
        }
    }

    /**
     * Resets the actor and calls GameObject::destroy
     */
    @Override
    public void kill() {
        if (isDestroyed()) return;
        for (var a : dieActions)
            a.act(this);
        destroy();
    }

    @Override
    public void destroy() {
        super.destroy();
        resetActions();
        resetDieActions();
        inCoolDown = false;
    }

    @ExcludeFromGeneratedCoverage
    @Override
    public void draw(SpriteBatch batch, long frame) {
        if(isInCoolDown()) {batch.setColor(Color.RED);}
        super.draw(batch, frame);
        if(isInCoolDown()) {batch.setColor(Color.WHITE);}
    }

    public Map<Integer, Long> getHitByIDs() {
        return hitByIDs;
    }

}
