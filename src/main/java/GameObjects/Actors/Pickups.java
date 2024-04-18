package GameObjects.Actors;

import GameObjects.Actors.Stats.PickupStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Animations.AnimationRendering.AnimationHandler;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import GameObjects.ObjectTypes.PickupType;

import java.util.Map;

public class Pickups extends GameObject<PickupType> {
    final PickupStats stats;
    public boolean isPickedUp;

    public Pickups(PickupType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale, PickupStats stats) {
        super(type, animationHandler, bodyFeatures, scale);
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

