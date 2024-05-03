package Rendering.Animations.AnimationRendering;

import Parsing.ObjectDefineParser.Defines.AnimationDefinition;
import Rendering.Animations.AnimationState;
import com.badlogic.gdx.graphics.Color;

import java.util.Map;

public class AnimationHandler {
    final private AnimationRender animationRender;

    private AnimationState animationState;


//    /**
//     * Handles object animations
//     * @param animationMap map of animation state as key and string (filepath) as value
//     * @param type sprite, gif, etc?
//     * @param spawnState spawn state of object
//     */
//    @Deprecated
//    private AnimationHandler(Map<AnimationState, String> animationMap, AnimationType type, AnimationState spawnState) {
//        if(animationMap.containsKey(spawnState)) {
//            this.animationState = spawnState;
//        }
//        else {
//            throw new IllegalArgumentException("The state " + spawnState + " is not in the key of the animationMap: " + animationMap);
//        }
//
//        animationRender = AnimationRender.of(type, animationMap, 1);
//        animationRender.setAnimation(animationState);
//
//    }

    /**
     * Handles object animations
     * Guesses the AnimationType based on file ending
     * @param animationMap map of animation state as key and string (filepath) as value
     * @param spawnState spawn state of object
     */
    public AnimationHandler(Map<AnimationState, String> animationMap, AnimationState spawnState, float scale) {
        if(animationMap.containsKey(spawnState)) {
            this.animationState = spawnState;
        }
        else {
            throw new IllegalArgumentException("The state " + spawnState + " is not in the key of the animationMap: " + animationMap);
        }

        var type = animationMap.get(spawnState).endsWith(".gif")
                ? AnimationType.GIF
                : AnimationType.SPRITE;

        animationRender = AnimationRender.of(type, animationMap, scale);
        animationRender.setAnimation(animationState);

    }

    public AnimationHandler(AnimationDefinition definition) {
        this(definition.stateStringMap, definition.initial, definition.scale);
    }

    /**
     * Rotates animation at its axis
     * @param rotationSpeed the angle incrementation of the rotation
     */
    public void rotate(float rotationSpeed) {
        animationRender.rotate(rotationSpeed);
    }

    /**
     * Stops rotation by setting the rotation speed  to 0
     */
    public void stopRotation() {
        animationRender.stopRotation();
    }
    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    public AnimationRender getAnimationRenderer() {
        return animationRender;
    }


}
