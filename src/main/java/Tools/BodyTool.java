package Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public abstract class BodyTool {

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

        bodyDef.position.set(pos);
        Body body = world.createBody(bodyDef);
        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setFilterData(filter);
        return body;
    }
}
