package Actors;

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
    void moveWithConstantSpeed();


    /**
     * changes actor speed
     * @param speedMultiplier
     */
    void setSpeed(int speedMultiplier);

}
