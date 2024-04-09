package GameObjects.Actors;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

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
