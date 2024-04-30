package GameObjects;

import GameObjects.ObjectActions.Action;
import Parsing.ObjectDefineParser.Defines.*;
import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.ShapeTools;
import com.badlogic.gdx.physics.box2d.Shape;
import org.apache.maven.surefire.shared.lang3.NotImplementedException;

import java.util.*;
import java.util.function.Supplier;

public interface IActor {

    /**
     * Performs the actions added by the
     * Actor.addAction
     */
    void doAction();

    /**
     * Adds the action to the actions performed by this Actor
     * @param action the action to add
     */
    void addAction(Action action);

    /**
     * Adds the actions to the actions performed by this Actor
     * @param actions the actions to add
     */
    void addAction(Action... actions);

    /**
     * Adds the actions to the actions performed by this Actor
     * @param actions the actions to add
     */
    void addAction(Collection<Action> actions);

    /**
     * Clears all the set actions
     */
    void resetActions();

//    /**
//     * Performs the die actions added by the
//     * Actor.addDieAction
//     */
//    void dieAction();
//
    /**
     * Adds an action to the die actions performed by this Actor
     * @param action the action to add
     */
    void addDieAction(Action action);

    /**
     * Adds actions to the die actions performed by this Actor
     * @param actions the actions to add
     */
    void addDieAction(Action... actions);

    /**
     * Adds actions to the die actions performed by this Actor
     * @param actions the actions to add
     */
    void addDieAction(Collection<Action> actions);

    /**
     * Clears all the set die actions
     */
    void resetDieActions();


    void setSpeed(float speed);
    float getSpeed();

    void setHP(float hp);
    float getHP();

    void setDamage(float damage);
    float getDamage();

    void setResistance(float resistance);
    float getResistance();

    /**
     * Called when an actor is to attack another actor
     * @param actor the actor which is attacked
     */
   void attack(Actor actor);

    /**
     * Called when an actor gets attacked by another actor
     * returns false if attack should be ignored
     * @param actor the actor that attacks
     * @return true if attack should be ignored
     */
   boolean attackedBy(Actor actor);


    /**
     * @return true if actor is under attack.
     */
   boolean isUnderAttack();

    /**
     * Destroyes and kills the enemy
     */
   void kill();

    abstract class ExperimentalFactory {
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

        static public void register(String name, ObjectDefinition definition) {
            float scale = definition.scale;

            var struct = definition.structureDefinition;
            var shape = getShapeFromShapeDefinition(struct.shapeDefinition);
            BodyFeatures bodyFeatures = getBodyFeaturesFromStructAndShape(struct, shape);
            AnimationHandler animationHandler = animationHandlerFromAnimationDefinition(definition.animationDefinition, scale);

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
            float scale = stats.scale;

            var struct = definition.structureDefinition;
            var shape = getShapeFromShapeDefinition(struct.shapeDefinition);
            BodyFeatures bodyFeatures = getBodyFeaturesFromStructAndShape(struct, shape);

            StatsConstants.Stats actorStats = new StatsConstants.Stats(
                    stats.speed,
                    stats.hp,
                    stats.damage,
                    stats.resistance
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



        static public void register(String name, Supplier<GameObject> factory) {
            factories.put(name, factory);
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
}
