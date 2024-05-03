package GameObjects;

import GameObjects.ObjectActions.Action;

import java.util.*;


public interface IActor {

    /**
     * Performs the actions added by the
     * Actor.addAction
     */
    void doAction();

    /**
     * Adds the action to the actions performed by this Actor
     * @param action the action to add
     */
    void addAction(Action action);

    /**
     * Adds the actions to the actions performed by this Actor
     * @param actions the actions to add
     */
    void addAction(Action... actions);

    /**
     * Adds the actions to the actions performed by this Actor
     * @param actions the actions to add
     */
    void addAction(Collection<Action> actions);

    /**
     * Clears all the set actions
     */
    void resetActions();

    /**
     * Adds an action to the die actions performed by this Actor
     * @param action the action to add
     */
    void addDieAction(Action action);

    /**
     * Adds actions to the die actions performed by this Actor
     * @param actions the actions to add
     */
    void addDieAction(Action... actions);

    /**
     * Adds actions to the die actions performed by this Actor
     * @param actions the actions to add
     */
    void addDieAction(Collection<Action> actions);

    /**
     * Clears all the set die actions
     */
    void resetDieActions();


    void setSpeed(float speed);
    float getSpeed();

    void setHP(float hp);
    float getHP();

    void setDamage(float damage);
    float getDamage();

    void setResistance(float resistance);
    float getResistance();

    /**
     * Called when an actor is to attack another actor
     * @param actor the actor which is attacked
     */
   void attack(Actor actor);

    /**
     * Called when an actor gets attacked by another actor
     * returns false if attack should be ignored
     * @param actor the actor that attacks
     * @return true if attack should be ignored
     */
   boolean attackedBy(Actor actor);


    /**
     * @return true if actor is under attack.
     */
   boolean isUnderAttack();

    /**
     * Destroys and kills the enemy
     */
   void kill();


    /**
     * Starts the cool down by setting the cooldown duration and setting the cooldown boolean to true
     * @param frameDuration cooldown duration (in frames
     */
   void startCoolDown(int frameDuration);


    /**
     *
     * @return true if actor is in cool down, false otherwise
     */
    boolean isInCoolDown();


    /**
     * Sets new actions for a set amount of frames. When the duration is complete,
     * the actor gets its original actions back
     * @param frameDuration number of frames for the change to last
     * @param actions the new temporary actions
     */
    void setTemporaryActionChange(int frameDuration, Action... actions);



}
