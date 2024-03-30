package Actors;

import Animations.AnimationStates;
import Animations.ActorAnimation;

public interface IAnimation {

    /**
     * Defines a new animation for the actor
     * @param animation IActorAnimation object
     */
    void setAnimation(ActorAnimation animation);

    /**
     * Executes the set animation
     */
    void doAnimation();

    /**
     * Changes the current GIF of the actor
     * @param gifPath path of the new GIF
     */
    void setNewAnimationGIF(String gifPath);

    /**
     * The animation state of the actor
     * @return an AnimationState enum
     */
    AnimationStates getAnimationState();

    /**
     * Set the new animation state and the corresponding GIF
     * Only changes the animation state if it is different from the last
     * @param newState
     * @param gifPath
     */
    void setAnimationState(AnimationStates newState, String gifPath);
}
