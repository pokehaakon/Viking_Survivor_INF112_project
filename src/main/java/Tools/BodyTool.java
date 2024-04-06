package Tools;

import GameObjects.BodyFeatures;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;

public abstract class BodyTool {
    private static Body createBody(World world, Vector2 pos, Filter filter, BodyDef bodyDef, FixtureDef fixtureDef) {
        bodyDef.position.set(pos);
        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setFilterData(filter);
        return body;
    }

    /**
     * Create a new Box2d body
     *
     * @param world the world in which the body is created
     * @param pos the starting position of the body
     * @param shape the shape of the body
     * @param filter the filter of the body
     * @param density the density of the body
     * @param friction the friction of the body
     * @param restitution the restitution of the body
     * @param isSensor decides if the body is a sensor
     * @return new body constructed using the parameters
     */
    public static Body createBody(World world, Vector2 pos, Shape shape, Filter filter, float density, float friction, float restitution, boolean isSensor, BodyDef.BodyType type) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = type;
        bodyDef.position.set(pos);
        bodyDef.fixedRotation = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;

        return createBody(world, pos, filter, bodyDef, fixtureDef);
    }

    ///**
    // * Create a new Box2d body
    // *
    // * @param world the world in which the body is created
    // * @param pos the starting position of the body
    // * @param shape the shape of the body
    // * @param filter the filter of the body
    // * @param density the density of the body
    // * @param friction the friction of the body
    // * @param restitution the restitution of the body
    // * @return new body constructed using the parameters
    // */
    //public static Body createBody(World world, Vector2 pos, Shape shape, Filter filter, float density, float friction, float restitution) {return createBody(world, pos, shape, filter, density, friction, restitution, false);}


    /**
     * creates new Box2d bodies, faster than calling 'createBody' multiple times!
     *
     * @param n number of bodies to create
     * @param world the world in which the bodies are created
     * @param poss an iterable of positions of where to create the bodies
     * @param shape the shape of the bodies
     * @param filter the filter of the bodies
     * @param density the density of the bodies
     * @param friction the friction of the bodies
     * @param restitution the restitution of the bodies
     * @param isSensor decides if the bodies are sensors
     * @return an array of the created bodies
     */
    public static Array<Body> createBodies(int n, World world, Iterable<Vector2> poss, Shape shape, Filter filter, float density, float friction, float restitution, boolean isSensor) {
        Array<Body> bodies = new Array<>(n);
        Iterator<Vector2> posIter = poss.iterator();

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        //bodyDef.position.set(pos); //this is set later
        bodyDef.fixedRotation = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution;
        fixtureDef.isSensor = isSensor;

        for (int i = 0; i<n; i++) {
            if (!posIter.hasNext()) throw new RuntimeException("Iterable ran out of elements: n=" + n + ", " + poss);
            bodies.add(createBody(world, posIter.next(), filter, bodyDef, fixtureDef));
        }
        return bodies;
    }

//    public  static Body createEnemyBody(World world, Vector2 pos, Shape shape) {
//        Filter enemyFilter = createFilter(
//                FilterTool.Category.ENEMY,
//                new FilterTool.Category[]{
//                        FilterTool.Category.WALL,
//                        FilterTool.Category.ENEMY,
//                        FilterTool.Category.PLAYER
//                }
//        );
//
//        return createBody(world, pos, shape, enemyFilter, 1, 0, 0);
//    }

    public static Body createTerrainBody(World world, Vector2 pos, Shape shape) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(pos);
        bodyDef.fixedRotation = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1;
        fixtureDef.friction = 1;
        fixtureDef.restitution = 0;
        fixtureDef.isSensor = false;

        Filter filter = createFilter(
                FilterTool.Category.WALL,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER
                }
        );

        return createBody(world, pos, filter, bodyDef, fixtureDef);

    }
}
