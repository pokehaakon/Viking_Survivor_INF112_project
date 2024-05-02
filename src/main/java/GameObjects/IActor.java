package GameObjects;

import GameObjects.ObjectActions.Action;
import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.ShapeTools;
import com.badlogic.gdx.physics.box2d.Shape;
import org.apache.maven.surefire.shared.lang3.NotImplementedException;

import java.util.*;
import java.util.function.Supplier;

import static VikingSurvivor.app.HelloWorld.SET_FPS;

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

//    /**
//     * Performs the die actions added by the
//     * Actor.addDieAction
//     */
//    void dieAction();
//
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
     * Destroyes and kills the enemy
     */
   void kill();


    /**
     * Starts the cool down by setting the cooldown duration and setting the cooldown boolean to true
     * @param duration cooldown duration (in milliseconds)
     */
   void startCoolDown(long duration);


    /**
     *
     * @return true if actor is in cool down, false otherwise
     */
    boolean isInCoolDown();


    /**
     * Sets new actions for a set amount of frames. When the duration is complete,
     * the actor gets its original actions back
     * @param duration number of frames for the change to last
     * @param actions the new temporary actions
     */
    void setTemporaryActionChange(float duration, Action... actions);



}
