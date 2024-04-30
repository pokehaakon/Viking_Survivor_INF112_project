package GameObjects.Factories;

import GameObjects.Actors.Stats.PickupStats;
import GameObjects.Actors.Stats.StatsConstants;
import GameObjects.BodyFeatures;
import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Rendering.Animations.AnimationRendering.AnimationRender;
import GameObjects.GameObject;
import Rendering.Animations.AnimationRendering.AnimationType;
import Rendering.Animations.AnimationState;
import Simulation.Simulation;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;

import java.util.EnumMap;
import java.util.Map;

import static Rendering.Animations.AnimationRendering.GIFS.*;
import static Rendering.Animations.AnimationRendering.GIFS.PLAYER_IDLE_FILE_PATH;
import static Tools.FilterTool.Category.*;
import static Tools.FilterTool.Category.WALL;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public abstract class AbstractFactory<T extends GameObject> implements IFactory<T> {

    public static void register() {
        registerWeapon();
        //registerPlayer();
        registerOld();
    }

    private static void registerPlayer() {
        var stats = StatsConstants.player();
        float scale = 1.8f;
        var definition = ActorDefinition.of(
                AnimationDefinition.of(
                        Map.of(AnimationState.MOVING, PLAYER_MOVING_FILE_PATH, AnimationState.IDLE, PLAYER_IDLE_FILE_PATH),
                        AnimationState.IDLE
                ),
                StatsDefinition.of(
                        stats.HP, stats.SPEED, stats.DAMAGE, stats.RESISTANCE, scale
                ),
                StructureDefinition.of(
                        FilterTool.createFilter(PLAYER, ENEMY, WALL),
                        CircleShapeDefinition.of(scale * 1f / 2f),
                        10,
                        0,
                        false,
                        BodyDef.BodyType.DynamicBody
                )
        );
        ExperimentalFactory.registerActor("PlayerType:PLAYER1", definition);
    }

    private static void registerWeapon() {
        var stats = StatsConstants.player();
        float scale = 2f;
        var definition = ActorDefinition.of(
                AnimationDefinition.of(
                        Map.of(AnimationState.MOVING, KNIFE_FILE_PATH, AnimationState.IDLE, KNIFE_FILE_PATH),
                        AnimationState.MOVING
                ),
                StatsDefinition.of(
                        stats.HP, stats.SPEED, stats.DAMAGE, stats.RESISTANCE, scale
                ),
                StructureDefinition.of(
                        FilterTool.createFilter(WEAPON, ENEMY),
                        CircleShapeDefinition.of(scale * 1f / 2f),
                        1,
                        0,
                        true,
                        BodyDef.BodyType.DynamicBody
                )
        );
        ExperimentalFactory.registerActor("WeaponType:KNIFE", definition);
    }

    static private void registerOld() {
        ExperimentalFactory.registerFromOldFactory(AbstractFactory::createTerrain, "TerrainType:TREE"); //TODO fix Terrain factory
        ExperimentalFactory.registerFromOldFactory(AbstractFactory::createPickup, "PickupType:PICKUPORB");
    }

    static private GameObject createPickup(String name) {
        if (name == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Filter filter = createFilter(
                FilterTool.Category.PICKUP,
                FilterTool.Category.PLAYER
        );

        Pickups pickup;
        PickupStats stats = null;
        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        AnimationType animationType;
        AnimationState spawnState;
        float scale = 0.2f; //In Meters


        Shape shape;
        switch (name) {
            case "PickupType:PICKUPORB" -> {
                stats = StatsConstants.pickupStats();
                animations.put(AnimationState.MOVING, PICK_UP_ORB_FILE_PATH);
                shape = createCircleShape(scale / 2);
                animationType = AnimationType.GIF;
                spawnState = AnimationState.MOVING;
                break;
            }
            default -> throw new IllegalStateException("Unexpected value: " + name);
        }

        BodyFeatures bodyfeatures = new BodyFeatures(
                shape,
                filter,
                1,
                0,
                0,
                true,
                BodyDef.BodyType.DynamicBody);



        pickup = new Pickups(name, new AnimationHandler(animations, spawnState, scale), bodyfeatures, () -> Simulation.EXP.addAndGet(10)); //TODO add other exp values than 10

        return pickup;
    }

    static private GameObject createTerrain(String name) {
        if(name == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        float scale;
        BodyFeatures bodyFeatures;

        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        Map<AnimationState, String> gifAnimations = new EnumMap<>(AnimationState.class);

        AnimationRender render;
        Shape shape;

        AnimationState spawnState;
        AnimationType animationType;

        boolean isGif;
        switch (name) {
            case "TerrainType:TREE": {
                scale = 3f;
                animations.put(AnimationState.STATIC, "tree.png");
                shape = createCircleShape(scale / 2);
                animationType = AnimationType.SPRITE;
                spawnState = AnimationState.STATIC;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid terrain type: " + name);
        }
        Filter filter = createFilter(
                FilterTool.Category.WALL,
                new FilterTool.Category[]{
                        //FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER,
                        FilterTool.Category.WEAPON
                }
        );


        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                1,
                1,
                0,
                false,
                BodyDef.BodyType.StaticBody);

        return new GameObject(name, new AnimationHandler(animations, spawnState, scale), bodyFeatures);
    }

}
