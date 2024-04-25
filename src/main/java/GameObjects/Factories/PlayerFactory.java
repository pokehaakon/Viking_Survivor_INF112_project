package GameObjects.Factories;

import GameObjects.Animations.AnimationRendering.AnimationHandler;
import GameObjects.Animations.AnimationRendering.AnimationType;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.PlayerType;
import GameObjects.Actors.Player;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.Actors.Stats.Stats;
//import Animations.ActorAnimations;
import GameObjects.BodyFeatures;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static GameObjects.ObjectTypes.PlayerType.PLAYER1;
import static Tools.FilterTool.Category.PLAYER;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PlayerFactory extends Factory<Player, PlayerType> {
    private static final Filter DEFAULT_PLAYER_FILTER = createFilter(
            PLAYER,
            new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL, FilterTool.Category.PICKUP}
    );
    public PlayerFactory() {

        register(() -> new Player(
                PLAYER1,
                new AnimationHandler(Map.of(AnimationState.MOVING, PlAYER_MOVING_FILE_PATH,
                        AnimationState.IDLE, PlAYER_IDLE_FILE_PATH),
                        AnimationType.GIF,AnimationState.IDLE),
                defaultPlayerBodyFeatures(createCircleShape(0.3f * PLAYER_SCALE * PLAYER_WIDTH / 2)),
                PLAYER_SCALE,
                Stats.player()

        ));
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
