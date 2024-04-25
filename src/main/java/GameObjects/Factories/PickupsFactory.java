package GameObjects.Factories;

import GameObjects.Actors.Pickups;
import GameObjects.Actors.Stats.PickupStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Animations.AnimationRendering.AnimationHandler;
import GameObjects.Animations.AnimationRendering.AnimationType;
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
        AnimationType animationType;
        AnimationState spawnState;
        float scale = 0;
        float xp_scale = XP_PICKUP_SCALE;
        float hp_scale = HP_PICKUP_SCALE;


        CircleShape shape;
        switch (type) {
            case XP_PICKUP -> {
                stats = Stats.XP_Pickup_stats();
                animations.put(AnimationState.MOVING, PICK_UP_ORB_FILE_PATH);
                shape = createCircleShape(0.2f * xp_scale * PICKUP_ORB_WIDTH / 2);
                animationType = AnimationType.GIF;
                spawnState = AnimationState.MOVING;
                scale = xp_scale;
                break;
            }
            case HP_PICKUP -> {
                stats = Stats.XP_Pickup_stats();
                animations.put(AnimationState.MOVING, HP_ORB_FILE_PATH);
                shape = createCircleShape(0.2f * hp_scale * PICKUP_ORB_WIDTH / 2);
                animationType = AnimationType.GIF;
                spawnState = AnimationState.MOVING;
                scale = hp_scale;
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



        pickup = new Pickups(type, new AnimationHandler(animations, animationType, spawnState), bodyfeatures, scale, stats);
        pickup.setAnimationState(AnimationState.MOVING);




        return pickup;
    }

}
