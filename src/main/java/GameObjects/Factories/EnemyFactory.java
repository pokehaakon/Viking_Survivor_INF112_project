package GameObjects.Factories;


import GameObjects.Animations.AnimationRendering.*;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.EnemyType;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Actors.Enemy;
import GameObjects.BodyFeatures;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;


public class EnemyFactory extends Factory<Enemy,EnemyType> {

    private static final Filter DEFAULT_ENEMY_FILTER = createFilter(
            FilterTool.Category.ENEMY,
            new FilterTool.Category[]{
                    FilterTool.Category.WALL,
                    FilterTool.Category.ENEMY,
                    FilterTool.Category.PLAYER,
                    FilterTool.Category.BULLET
            });


    public EnemyFactory() {

        register(() -> new Enemy(
                EnemyType.RAVEN,
                new AnimationHandler(Map.of(AnimationState.MOVING, RAVEN_FILE_PATH), AnimationType.GIF, AnimationState.MOVING),
                defaultEnemyBodyFeatures(createCircleShape(0.5f * RAVEN_SCALE * RAVEN_WIDTH / 2)),
                RAVEN_SCALE,
                Stats.enemy1()

        ));

        register(() -> new Enemy(
                EnemyType.ORC,
                new AnimationHandler(Map.of(AnimationState.MOVING, ORC_FILE_PATH), AnimationType.GIF, AnimationState.MOVING),
                defaultEnemyBodyFeatures(createCircleShape(0.3f * ORC_SCALE * ORC_WIDTH / 2)),
                ORC_SCALE,
                Stats.enemy2()
        ));

        register(() -> new Enemy(
                EnemyType.WOLF,
                new AnimationHandler(Map.of(AnimationState.MOVING, WOLF_FILE_PATH), AnimationType.GIF, AnimationState.MOVING),
                defaultEnemyBodyFeatures(createCircleShape(0.5f * ORC_SCALE * WOLF_WIDTH / 2)),
                ORC_SCALE,
                Stats.enemy2()
        ));


    }

    private static BodyFeatures defaultEnemyBodyFeatures(Shape shape) {
        return new BodyFeatures(
                shape,
                DEFAULT_ENEMY_FILTER,
                1,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);
    }


}