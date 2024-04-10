package GameObjects.Actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public interface IActor {
    /**
     * resets velocity vector to default conditions
     */
    void resetVelocity();


    /**
     * changes velocity direction
     * @param x
     * @param y
     */
    void setVelocityVector(float x, float y);

    /**
     * changes velocity direction
     * @param v
     */
    void setVelocityVector(Vector2 v);

    /**
     * moves actor according to its velocity vector and speed
     */
    void move();


    /**
     * changes actor speed
     * @param speedMultiplier
     */
    void setSpeed(int speedMultiplier);

    /**
     * Draws GIF
     * @param batch
     * @param elapsedTime for GIF handling
     */
    void draw(SpriteBatch batch, float elapsedTime);

}
