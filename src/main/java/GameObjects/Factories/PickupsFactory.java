package GameObjects.Factories;


import GameObjects.BodyFeatures;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.Map;

import static Rendering.Animations.AnimationRendering.GIFS.PICK_UP_ORB_FILE_PATH;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PickupsFactory extends Factory<Pickups> {

    private static final Filter DEFAULT_PICKUP_FILTER = createFilter(
            FilterTool.Category.PICKUP,
            new FilterTool.Category[]{
                    FilterTool.Category.PLAYER
            }
    );

    public PickupsFactory() {

        register(() -> new Pickups(
                "XP_PICKUP",
                new AnimationHandler(Map.of(AnimationState.IDLE,PICK_UP_ORB_FILE_PATH), AnimationState.IDLE,1),
                defaultPickUpBodyFeatures(createCircleShape(0.5f * 1 * 50 / 4)),
                null
        ));
//        register(() -> new Pickups(
//                "PickupType.HP_PICKUP",
//                new AnimationHandler(Map.of(AnimationState.IDLE,HP_ORB_FILE_PATH),AnimationType.GIF,AnimationState.IDLE),
//                defaultPickUpBodyFeatures(createCircleShape(0.5f * HP_PICKUP_SCALE * PICKUP_ORB_WIDTH / 6)),
//                HP_PICKUP_SCALE,
//                null
//        ));

//        register(() -> new Pickups(
//                PickupType.SCULL,
//                new AnimationHandler(Map.of(AnimationState.IDLE,SCULL_FILE_PATH), AnimationType.GIF,AnimationState.IDLE),
//                defaultPickUpBodyFeatures(createCircleShape(SCULL_WIDTH/3)),
//                1,
//                null
//        ));
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
