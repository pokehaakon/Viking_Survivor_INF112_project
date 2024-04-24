package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;
import GameObjects.GameObject;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;

public interface AnimationRender {

    static AnimationRender of(AnimationType type, Map<AnimationState, String> animationMap) {
        if(type == AnimationType.GIF) {
            return new GIFRender(animationMap);
        }
        else {
            return new SpriteRender(animationMap);
        }
    }

    /**
     * Draws the animation
     * @param batch sprite batch
     * @param frame the current frame (used by gif)
     * @param object the object to draw
     */
    void draw(SpriteBatch batch, long frame, GameObject object);

    /**
     * Sets a new animation according to the animation state
     * @param state object state
     */
    void setAnimation(AnimationState state);

//    /**
//     * Initializes the animations for the renderer
//     * @param animationMap map for what to draw for each 'AnimationState'
//     */
//    void initAnimations(Map<AnimationState, String> animationMap);

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

}
