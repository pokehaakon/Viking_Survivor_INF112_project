package GameObjects.Factories;

import GameObjects.Actors.Actor;
import GameObjects.Actors.Stats.StatsConstants;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.ShapeTools;
import com.badlogic.gdx.physics.box2d.Shape;
import org.apache.maven.surefire.shared.lang3.NotImplementedException;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class ExperimentalFactory {
    private static final Map<String, Supplier<GameObject>> factories = new HashMap<>();
    private static final Set<String> isActor = new HashSet<>();
    private static int UID = 0;


    static public List<String> getRegistered() {
        return factories.keySet().stream().toList();
    }
    static public List<String> getRegisteredActors() {
        //System.out.println(isActor);
        //System.out.println(factories.keySet());
        return isActor.stream().toList();
    }

    static public void registerActor(String name, ActorDefinition definition) {
        isActor.add(name);
        register(name, definition);
    }

    static private void register(String name, ActorDefinition definition) {
        //isActor.add(name);
        var stats = definition.statsDefinition;
        float scale = stats.scale;

        var struct = definition.structureDefinition;
        var shape = getShapeFromShapeDefinition(struct.shapeDefinition, scale);
        //System.out.println("Filter: " + struct.filter.categoryBits + ", " + struct.filter.maskBits);
        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                struct.filter,
                struct.density,
                struct.friction,
                0,
                struct.isSensor,
                struct.bodyType
        );
        StatsConstants.Stats actorStats = new StatsConstants.Stats(
                stats.speed,
                stats.hp,
                stats.damage,
                0
        );

        AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition, scale);

        Supplier<GameObject> supplier = () -> new Actor(
                name,
                animationHandler,
                bodyFeatures,
                actorStats
        );

        factories.put(name, supplier);
    }

    static public void register(String name, ObjectDefinition definition) {
        float scale = definition.scale;

        var struct = definition.structureDefinition;
        var shape = getShapeFromShapeDefinition(struct.shapeDefinition, scale);
        //System.out.println("Filter: " + struct.filter.categoryBits + ", " + struct.filter.maskBits);
        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                struct.filter,
                struct.density,
                struct.friction,
                0,
                struct.isSensor,
                struct.bodyType
        );

        AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition, scale);

        Supplier<GameObject> supplier = () -> new GameObject(
                name,
                animationHandler,
                bodyFeatures
        );

        factories.put(name, supplier);
    }

    static public void register(String name, Supplier<GameObject> factory) {
        factories.put(name, factory);
    }

    static public void registerFromOldFactory(Function<String, GameObject> oldFactory, String... names) {
        for (String name : names) {
            register(name, () -> oldFactory.apply(name));
        }
    }

    static public void registerActor(String name, Supplier<Actor> factory) {
        isActor.add(name);
        factories.put(name, factory::get);
    }

    static private AnimationHandler animationHandlerFromAnimationDefinition(AnimationDefinition definition, float scale) {
        return new AnimationHandler(
                definition.stateStringMap,
                definition.initial,
                scale
        );
    }

    static private Shape getShapeFromShapeDefinition(ShapeDefinition definition, float scale) {
        if (definition instanceof SquareShapeDefinition squareShapeDefinition) {
            return ShapeTools.createSquareShape(squareShapeDefinition.width * scale, squareShapeDefinition.height * scale);
        }
        if (definition instanceof CircleShapeDefinition circleShapeDefinition) {
            return ShapeTools.createCircleShape(circleShapeDefinition.radius * scale);
        }
        if (definition instanceof PolygonShapeDefinition polygonShapeDefinition) {
            throw new NotImplementedException("Cannot create polygon shapes yet!");
        }
        throw new IllegalArgumentException("Cannot create the given definition " + definition);
    }

    static public Actor createActor(String name) {
        if (!factories.containsKey(name)) throw new IllegalArgumentException("Factory does not know how to create '" + name + "', \n can only create" + getRegistered());
        if (!isActor.contains(name)) throw new IllegalArgumentException("'" + name + "' is not an Actor!");
        return (Actor) factories.get(name).get();
    }

    static public GameObject create(String name) {
        if (!factories.containsKey(name)) throw new IllegalArgumentException("Factory does not know how to create '" + name + "'");
        return factories.get(name).get();
    }

    public static void empty() {
        factories.clear();
        isActor.clear();
        UID = 0;
    }

    synchronized public static int getUID() {
        return UID++;
    }
}
