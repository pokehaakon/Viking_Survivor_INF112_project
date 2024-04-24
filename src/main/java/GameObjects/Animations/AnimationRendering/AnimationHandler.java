package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;
import java.util.Objects;

public class AnimationHandler {

    private AnimationState animationState;

    private AnimationType type;

    private Map<AnimationState,String> animationMap;


    private AnimationRender animationRender;

    /**
     * Handles object animations
     * @param animationMap map of animation state as key and string (filepath) as value
     * @param type sprite, gif, etc?
     * @param spawnState spawn state of object
     */
    public AnimationHandler(Map<AnimationState,String> animationMap, AnimationType type, AnimationState spawnState) {
        this.animationMap = animationMap;
        this.type = type;

        if(animationMap.containsKey(spawnState)) {
            this.animationState = spawnState;
        }
        else {
            throw new IllegalArgumentException("The state " + spawnState + " is not in the objects animation map");
        }



    }
    public AnimationState getAnimationState() {
        return animationState;
    }

    public void setAnimationState(AnimationState animationState) {
        this.animationState = animationState;
    }

    public Map<AnimationState, String> getAnimationMap() {
        return animationMap;
    }


    /**
     * Sets an animation render depending on its animation type (currently either sprite or GIF)
     * The animation render is used for the draw() method, as different animation types requires different implementations
     * @param animationLibrary the collection of animations where it gets the animations from
     */
    public void renderAnimations(AnimationLibrary animationLibrary) {
        // avoid rendering multiple times
        if(Objects.isNull(animationRender)) {
            if(type == AnimationType.GIF) {
                animationRender = new GIFRender<>(animationLibrary,animationMap);
            }
            else {
                animationRender = new SpriteRender(animationLibrary,animationMap);
            }
            animationRender.setAnimation(animationState);
        }
    }

    public AnimationRender getAnimationRenderer() {
        return animationRender;
    }


}
