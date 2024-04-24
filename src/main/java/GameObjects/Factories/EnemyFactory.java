//package GameObjects.Factories;
//
//
//import Rendering.Animations.AnimationRendering.AnimationHandler;
//import Rendering.Animations.AnimationRendering.AnimationType;
//import Rendering.Animations.AnimationState;
//import GameObjects.ObjectTypes.EnemyType;
//import GameObjects.Actors.Stats.EnemyStats;
//import GameObjects.Actors.Stats.Stats;
//import GameObjects.Actors.Enemy;
//import GameObjects.BodyFeatures;
//import Tools.FilterTool;
//import com.badlogic.gdx.physics.box2d.*;
//
//import java.util.EnumMap;
//import java.util.Map;
//
//import static Rendering.Animations.AnimationRendering.GIFS.*;
//import static Tools.FilterTool.createFilter;
//import static Tools.ShapeTools.createCircleShape;
//
//
//public class EnemyFactory extends AbstractFactory<Enemy> {
//
//    private final Filter filter;
//
//    public EnemyFactory() {
//        super();
//
//
//        filter  = createFilter(
//                FilterTool.Category.ENEMY,
//                new FilterTool.Category[]{
//                        FilterTool.Category.WALL,
//                        FilterTool.Category.ENEMY,
//                        FilterTool.Category.PLAYER,
//                        FilterTool.Category.BULLET
//                }
//        );
//
//    }
//
//    /**
//     * Creates an instance of an enemy
//     * @param name the desired enemy type
//     * @return an enemy object
//     */
//    @Override
//    public Enemy create(String name) {
//
//        if(name == null) {
//            throw new NullPointerException("Type cannot be null!");
//        }
//
//        Enemy enemy;
//        float scale;
//        EnemyStats stats;
//        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
//
//
//        AnimationType animationType;
//        Shape shape;
//        AnimationState spawnState;
//
//        switch (type) {
//            case RAVEN: {
//                scale = RAVEN_SCALE;
//                stats = Stats.enemy1();
//                animations.put(AnimationState.MOVING, RAVEN_FILE_PATH);
//                shape = createCircleShape(0.5f*scale*RAVEN_WIDTH/2);
//                animationType = AnimationType.GIF;
//                spawnState = AnimationState.MOVING;
//                break;
//            }
//            case ORC: {
//                scale = ORC_SCALE;
//                stats = Stats.enemy2();
//                animations.put(AnimationState.MOVING, ORC_FILE_PATH);
//                shape = createCircleShape(0.3f*scale*ORC_WIDTH/2);
//                animationType = AnimationType.GIF;
//                spawnState = AnimationState.MOVING;
//                break;
//            }
//            case WOLF: {
//                scale = ORC_SCALE;
//                stats = Stats.enemy2();
//
//                animations.put(AnimationState.MOVING, WOLF_FILE_PATH);
//                shape = createCircleShape(0.5f*scale*WOLF_WIDTH/2);
//                animationType = AnimationType.GIF;
//                spawnState = AnimationState.MOVING;
//                break;
//            }
//            default:
//                throw new IllegalArgumentException("Invalid enemy type");
//        }
//
//
//        BodyFeatures bodyFeatures = new BodyFeatures(
//                shape,
//                filter,
//                1,
//                0,
//                0,
//                false,
//                BodyDef.BodyType.DynamicBody);
//
//        enemy = new Enemy(type, new AnimationHandler(animations, animationType, spawnState), bodyFeatures, scale, stats);
//
//        return enemy;
//    }
//
//}
//
