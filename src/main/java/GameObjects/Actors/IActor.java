package GameObjects.Actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;

public interface IActor {
//    /**
//     * resets velocity vector to default conditions
//     */
//    void resetVelocity();
//
//
//    /**
//     * changes velocity direction
//     * @param x
//     * @param y
//     */
//    void setVelocityVector(float x, float y);
//
//    /**
//     * changes velocity direction
//     * @param v
//     */
//    void setVelocityVector(Vector2 v);
//
//    /**
//     * moves actor according to its velocity vector and speed
//     */
//    void move();


    /**
     * changes actor speed
     * @param speedMultiplier
     */
    void setSpeed(float speedMultiplier);

    /**
     * Attacks an actor
     * @param actor the actor which is attacked
     * @param damage the damage inflicted on the actor
     */
   void attack(Actor actor,float damage);


    /**
     *
     * @return true if actor is under attack.
     */
   boolean isUnderAttack();

    /**
     *
     * @return the last time the actor was  attacked
     */
    long getLastAttackedTime();

    /**
     * Updates the last attacked long
     * @param newAttack new long
     */
    void setLastAttackedTime(long newAttack);

    /**
     *
     * @param bool
     */

    /**
     * Changes the underAttack boolean
     * @param bool the new booleanw
     */
    void setUnderAttack(boolean bool);



}
