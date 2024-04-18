package GameObjects.Factories;

import GameObjects.Actors.Pickups;
import GameObjects.Actors.Stats.PickupStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Animations.AnimationState;
import GameObjects.BodyFeatures;
import GameObjects.ObjectTypes.PickupType;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Filter;

import java.util.EnumMap;
import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PickupsFactory extends AbstractFactory<Pickups, PickupType>{
    private final Filter filter;
    public PickupsFactory() {
        super();
        filter = createFilter(
                FilterTool.Category.PICKUP,
                new FilterTool.Category[]{
                        FilterTool.Category.PLAYER
                }
        );



    }

    @Override
    public Pickups create(PickupType type) {
        if (type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Pickups pickup;
        PickupStats stats = null;
        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        float scale = PICKUP_ORB_SCALE;

        boolean isGif;

        CircleShape shape;
        switch (type) {
            case XP_PICKUP -> {
                stats = Stats.pickupStats();
                animations.put(AnimationState.MOVING, PICK_UP_ORB_FILE_PATH);
                shape = createCircleShape(0.5f * scale * PICKUP_ORB_WIDTH / 2);
                isGif = true;
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + type);
        }

        BodyFeatures bodyfeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);



        pickup = new Pickups(type, animations, bodyfeatures, scale, stats);
        pickup.setAnimationState(AnimationState.MOVING);
        pickup.isGif = true;



        return pickup;
    }

}
