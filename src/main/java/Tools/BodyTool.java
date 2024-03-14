package Tools;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import java.util.Iterator;

import static Tools.ShapeTools.createSquareShape;

public abstract class BodyTool {
    private static Body createBody(World world, Vector2 pos, Filter filter, BodyDef bodyDef, FixtureDef fixtureDef) {
        bodyDef.position.set(pos);
        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setFilterData(filter);
        return body;
    }
    public static Body createBody(World world, Vector2 pos, Shape shape, Filter filter, float density, float friction, float restitution, boolean isSensor) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
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

    public static Body createBody(World world, Vector2 pos, Shape shape, Filter filter, float density, float friction, float restitution) {return createBody(world, pos, shape, filter, density, friction, restitution, false);}

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
}