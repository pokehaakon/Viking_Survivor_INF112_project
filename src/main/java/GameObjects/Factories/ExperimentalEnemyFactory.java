package GameObjects.Factories;


import GameObjects.Actors.Enemy;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.ShapeTools;
import VikingSurvivor.app.Main;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import org.apache.maven.surefire.shared.lang3.NotImplementedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;



public abstract class ExperimentalEnemyFactory extends ExperimentalAbstractFactory {
    private static final Map<String, Supplier<Enemy>> factories = new HashMap<>();
    //private static final Map<String, Supplier<GameObject>> ofactories = new HashMap<>();


    static public List<String> getRegistered() {
        return factories.keySet().stream().toList();
    }

    static public void register(String name, EnemyDefinition definition) {
        //ExperimentalAbstractFactory.addFactory(ofactories);
        ExperimentalAbstractFactory.addFactory((Map<String, Supplier<GameObject>>)((Map<?, ?>) factories));

        var stats = definition.statsDefinition;
        float scale = stats.scale;

        var struct = definition.structureDefinition;
        var shape = getShapeFromShapeDefinition(struct.shapeDefinition, scale);
        System.out.println("Filter: " + struct.filter.categoryBits + ", " + struct.filter.maskBits);
        BodyFeatures bodyFeatures = new BodyFeatures(
                shape,
                struct.filter,
                struct.density,
                struct.friction,
                0,
                false,
                BodyDef.BodyType.DynamicBody
        );

        EnemyStats enemyStats = new EnemyStats(
                stats.hp,
                stats.speed,
                stats.damage,
                0,
                0
        );

        AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition);

        Supplier<Enemy> supplier = () -> new Enemy(
                    name,
                    animationHandler,
                    bodyFeatures,
                    scale,
                    enemyStats
        );

        Supplier<GameObject> osupplier = supplier::get;

        factories.put(name, supplier);
        //ofactories.put(name, osupplier);
    }

    static private AnimationHandler animationHandlerFromAnimationDefinition(AnimationDefinition definition) {
        return new AnimationHandler(
                definition.stateStringMap,
                definition.initial
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

    static public Enemy create(String name) {
        if (!factories.containsKey(name)) throw new IllegalArgumentException("Factory does not know how to create '" + name + "'");
        return factories.get(name).get();
    }

}

