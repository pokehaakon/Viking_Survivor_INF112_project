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
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static Tools.ShapeTools.createSquareShape;

public class PickupsFactory extends Factory<Pickups,PickupType> {

    private static final Filter DEFAULT_PICKUP_FILTER = createFilter(
            FilterTool.Category.PICKUP,
            new FilterTool.Category[]{
                    FilterTool.Category.PLAYER
            }
    );

    public PickupsFactory() {

        register(() -> new Pickups(
                PickupType.XP_PICKUP,
                new AnimationHandler(Map.of(AnimationState.IDLE,PICK_UP_ORB_FILE_PATH),AnimationType.GIF,AnimationState.IDLE),
                defaultPickUpBodyFeatures(createCircleShape(0.5f * XP_PICKUP_SCALE * PICKUP_ORB_WIDTH / 2)),
                XP_PICKUP_SCALE,
                null
        ));
    }

    private BodyFeatures defaultPickUpBodyFeatures(Shape shape) {
        return new BodyFeatures(
                shape,
                DEFAULT_PICKUP_FILTER,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);
    }


}
