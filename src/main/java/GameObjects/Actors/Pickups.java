package GameObjects.Actors;

import GameObjects.Actors.Stats.PickupStats;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import GameObjects.ObjectTypes.PickupType;

public class Pickups extends GameObject {
    final PickupStats stats;
    public boolean isPickedUp;

    public Pickups(String name, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale, PickupStats stats) {
        super(name, animationHandler, bodyFeatures, scale);
        this.stats = stats;
        isPickedUp = false;
    }

    public boolean isPickedUp() {
        return isPickedUp;
    }

    public void setPickedUp(boolean pickedUp) {
        isPickedUp = pickedUp;
    }


}

