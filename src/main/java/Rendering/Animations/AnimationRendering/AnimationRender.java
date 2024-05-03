package Rendering.Animations.AnimationRendering;

import GameObjects.GameObject;
import Rendering.Animations.AnimationState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public interface AnimationRender {

    static AnimationRender of(AnimationType type, Map<AnimationState, String> animationMap, float scale) {
        if (type == AnimationType.GIF) {
            return new GIFRender(animationMap, scale);
        } else {
            return new SpriteRender(animationMap, scale);
        }
    }

    /**
     * Draws the animation
     *
     * @param batch  sprite batch
     * @param frame  the current frame (used by gif)
     * @param object the object to draw
     */
    void draw(SpriteBatch batch, long frame, GameObject object);

    /**
     * Sets a new animation according to the animation state
     *
     * @param state object state
     */
    void setAnimation(AnimationState state);


    /**
     * @param state
     * @return animation width of desired state
     */
    float getWidth(AnimationState state);

    /**
     * @param state
     * @return animation height of desired state
     */
    float getHeight(AnimationState state);

    /**
     * Rotates animation at its axis
     * @param rotationSpeed
     */
    void rotate(float rotationSpeed);

    /**
     * Stops rotation by setting rotationspeed to zero
     */
    void stopRotation();

}
