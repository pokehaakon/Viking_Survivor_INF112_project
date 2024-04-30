package GameObjects.Factories;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Stats.StatsConstants;

import GameObjects.BodyFeatures;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationState;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.EnumMap;
import java.util.Map;

import static Tools.FilterTool.Category.PLAYER;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PlayerFactory extends Factory<Actor> {
    private static final Filter DEFAULT_PLAYER_FILTER = createFilter(
            PLAYER,
            new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL, FilterTool.Category.PICKUP}
    );
    public PlayerFactory() {
        ExperimentalFactory.register("PLAYER1", () -> new Actor(
                "PLAYER1",
                new AnimationHandler(
                        Map.of(AnimationState.MOVING, "tree.png", AnimationState.IDLE, "tree.png"),
                        AnimationState.IDLE, 1),
                defaultPlayerBodyFeatures(createCircleShape(0.3f * 1.8f / 2)),
                StatsConstants.player()
        ));
        register(() -> new Actor(
                "PLAYER1",
                new AnimationHandler(
                        Map.of(AnimationState.MOVING, "tree.png", AnimationState.IDLE, "tree.png"),
                       AnimationState.IDLE, 1),
                defaultPlayerBodyFeatures(createCircleShape(0.3f * 1.8f / 2)),
                StatsConstants.player()
            )
        );
    }

    private BodyFeatures defaultPlayerBodyFeatures(Shape shape) {
        return new BodyFeatures(
                shape,
                DEFAULT_PLAYER_FILTER,
                10,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);

    }




}
