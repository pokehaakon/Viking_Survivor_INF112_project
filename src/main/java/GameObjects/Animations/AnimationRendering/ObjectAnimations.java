package GameObjects.Animations.AnimationRendering;

import GameObjects.Animations.AnimationState;

import java.util.Map;

/**
 *
 * @param animationMap object animations
 * @param type gif, sprite, etc?
 * @param spawnState set the animationstate when spawning
 */
public record ObjectAnimations(Map<AnimationState,String> animationMap,AnimationType type, AnimationState spawnState) {
}
