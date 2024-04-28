package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public interface AnimationRender {

    /**
     * Draws the animation
     * @param batch sprite batch
     * @param elapsedTime for gifs
     * @param object the object to draw
     */
    void draw(SpriteBatch batch, float elapsedTime, GameObject object);

    /**
     * Sets a new animation according to the animation state
     * @param state object state
     */
    void setAnimation(AnimationState state);

    /**
     *
     * @param state
     * @return animation width of desired state
     */
    float getWidth(AnimationState state);

    /**
     *
     * @param state
     * @return animation height of desired state
     */
    float getHeight(AnimationState state);

    void setAnimations(Map<AnimationState,String> animationMap);

    /**
     * Rotates the animation at its axis
     * @param rotationSpeed incrementation value for each frame
     */
    void rotate(float rotationSpeed);

    /**
     * Stops the animation rotation by setting its rotation speed to zero
     */
    void stopRotation();


}
