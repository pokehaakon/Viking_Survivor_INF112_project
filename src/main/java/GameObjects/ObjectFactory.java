package GameObjects;

import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.ExcludeFromGeneratedCoverage;
import Tools.ShapeTools;
import com.badlogic.gdx.physics.box2d.Shape;
import org.apache.maven.surefire.shared.lang3.NotImplementedException;

import java.util.*;
import java.util.function.Supplier;

public abstract class ObjectFactory {
    private static final Map<String, Supplier<GameObject>> factories = new HashMap<>();
    private static final Set<String> isActor = new HashSet<>();
    private static int UID = 0;


    static public List<String> getRegistered() {
        return factories.keySet().stream().toList();
    }

    static public List<String> getRegisteredActors() {
        return isActor.stream().toList();
    }

    static public void register(String name, ObjectDefinition definition) {

        var struct = definition.structureDefinition;
        var shape = getShapeFromShapeDefinition(struct.shapeDefinition);
        BodyFeatures bodyFeatures = getBodyFeaturesFromStructAndShape(struct, shape);
        AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition);

        Supplier<GameObject> supplier = () -> new GameObject(
                name,
                animationHandler,
                bodyFeatures
        );

        factories.put(name, supplier);
    }

    static public void register(String name, ActorDefinition definition) {
        isActor.add(name);
        var stats = definition.statsDefinition;
        //float scale = stats.scale;

        var struct = definition.structureDefinition;
        var shape = getShapeFromShapeDefinition(struct.shapeDefinition);
        BodyFeatures bodyFeatures = getBodyFeaturesFromStructAndShape(struct, shape);

        StatsConstants.Stats actorStats = new StatsConstants.Stats(
                stats.speed,
                stats.hp,
                stats.damage,
                stats.resistance
        );

        AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition);

        Supplier<GameObject> supplier = () -> new Actor(
                name,
                animationHandler,
                bodyFeatures,
                actorStats
        );

        factories.put(name, supplier);
    }

    @ExcludeFromGeneratedCoverage(reason = "not used")
    static public void register(String name, Supplier<GameObject> factory) {
        factories.put(name, factory);
    }


    static public void registerActor(String name, Supplier<Actor> factory) {
        isActor.add(name);
        factories.put(name, factory::get);
    }

    static private AnimationHandler animationHandlerFromAnimationDefinition(AnimationDefinition definition) {
        return new AnimationHandler(
                definition.stateStringMap,
                definition.initial,
                definition.scale
        );
    }

    private static BodyFeatures getBodyFeaturesFromStructAndShape(StructureDefinition struct, Shape shape) {
        return new BodyFeatures(
                shape,
                struct.filter,
                struct.density,
                struct.friction,
                0,
                struct.isSensor,
                struct.bodyType
        );
    }

    static private Shape getShapeFromShapeDefinition(ShapeDefinition definition) {
        if (definition instanceof SquareShapeDefinition squareShapeDefinition) {
            return ShapeTools.createSquareShape(squareShapeDefinition.width, squareShapeDefinition.height);
        }
        if (definition instanceof CircleShapeDefinition circleShapeDefinition) {
            return ShapeTools.createCircleShape(circleShapeDefinition.radius);
        }
        if (definition instanceof PolygonShapeDefinition polygonShapeDefinition) {
            throw new NotImplementedException("Cannot create polygon shapes yet!");
        }
        throw new IllegalArgumentException("Cannot create the given definition " + definition);
    }

    static public Actor createActor(String name) {
        if (!factories.containsKey(name))
            throw new IllegalArgumentException("Factory does not know how to create '" + name + "', \n can only create" + getRegistered());
        if (!isActor.contains(name)) throw new IllegalArgumentException("'" + name + "' is not an Actor!");
        return (Actor) factories.get(name).get();
    }

    static public GameObject create(String name) {
        if (!factories.containsKey(name))
            throw new IllegalArgumentException("Factory does not know how to create '" + name + "'");
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
