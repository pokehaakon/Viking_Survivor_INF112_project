package Rendering.Animations.AnimationRendering;

import Rendering.Animations.AnimationState;

import java.util.Map;
import java.util.Objects;

public class AnimationHandler {

    private AnimationState animationState;

    private AnimationRender animationRender;

    /**
     * Handles object animations
     * @param animationMap map of animation state as key and string (filepath) as value
     * @param type sprite, gif, etc?
     * @param spawnState spawn state of object
     */
    @Deprecated
    public AnimationHandler(Map<AnimationState, String> animationMap, AnimationType type, AnimationState spawnState) {
        if(animationMap.containsKey(spawnState)) {
            this.animationState = spawnState;
        }
        else {
            throw new IllegalArgumentException("The state " + spawnState + " is not in the key of the animationMap: " + animationMap);
        }

        animationRender = AnimationRender.of(type, animationMap);
        animationRender.setAnimation(animationState);

    }

    /**
     * Handles object animations
     * Guesses the AnimationType based on file ending
     * @param animationMap map of animation state as key and string (filepath) as value
     * @param spawnState spawn state of object
     */
    public AnimationHandler(Map<AnimationState, String> animationMap, AnimationState spawnState) {
        if(animationMap.containsKey(spawnState)) {
            this.animationState = spawnState;
        }
        else {
            throw new IllegalArgumentException("The state " + spawnState + " is not in the key of the animationMap: " + animationMap);
        }

        var type = animationMap.get(spawnState).endsWith(".gif")
                ? AnimationType.GIF
                : AnimationType.SPRITE;

        animationRender = AnimationRender.of(type, animationMap);
        animationRender.setAnimation(animationState);

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
