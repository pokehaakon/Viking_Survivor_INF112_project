package GameObjects.Actors;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;

public class Pickups extends GameObject {
    private final Runnable pickUpAction;

    public Pickups(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures, Runnable pickUpAction) {
        super(name, animationHandler, bodyFeatures);
        this.pickUpAction = pickUpAction;
    }

    public void doPickupAction() {
        pickUpAction.run();
    }
}

