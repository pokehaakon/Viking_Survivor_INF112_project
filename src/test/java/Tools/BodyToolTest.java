package Tools;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class BodyToolTest {
    World world = new World(new Vector2(0, -9.81f), true);
    Vector2 position = new Vector2(10, 20);
    PolygonShape shape = new PolygonShape();
    Filter filter = new Filter();
    float density = 1.0f;
    float friction = 0.5f;
    float restitution = 0.3f;
    boolean isSensor = true;
    BodyDef.BodyType type = BodyDef.BodyType.DynamicBody;

    @Test
    public void testCreateBody() {
        shape.setAsBox(1.0f, 1.0f);
        Body body = BodyTool.createBody(world, position, shape, filter, density, friction, restitution, isSensor, type);

        assertNotNull(body, "Body should not be null");
        assertEquals(type, body.getType(), "Body type should match the input");
        assertEquals(position, body.getPosition(), "Body position should match the input");

        shape.dispose();
    }

    @Test
    public void testBodyFixture() {
        shape.setAsBox(1.0f, 1.0f);
        Body body = BodyTool.createBody(world, position, shape, filter, density, friction, restitution, isSensor, type);

        Fixture fixture = body.getFixtureList().get(0);
        assertNotNull(fixture, "Fixture should not be null");
        assertEquals(density, fixture.getDensity(), "Fixture density should match the input");
        assertEquals(friction, fixture.getFriction(), "Fixture friction should match the input");
        assertEquals(restitution, fixture.getRestitution(), "Fixture restitution should match the input");
        assertEquals(isSensor, fixture.isSensor(), "Fixture sensor status should match the input");

        shape.dispose();
    }

    @Test
    public void testDifferentBodyTypes() {
        shape.setAsBox(2.0f, 2.0f);
        BodyDef.BodyType[] types = {BodyDef.BodyType.StaticBody, BodyDef.BodyType.KinematicBody, BodyDef.BodyType.DynamicBody};
        for (BodyDef.BodyType testType : types) {
            Body body = BodyTool.createBody(world, position, shape, filter, density, friction, restitution, isSensor, testType);
            assertEquals(testType, body.getType(), "Body type should reflect input type: " + testType);
        }
        shape.dispose();
    }

    @Test
    public void testExtremeValues() {
        float maxDensity = Float.MAX_VALUE;
        float zeroFriction = 0.0f;
        float maxRestitution = 1.0f;

        shape.setAsBox(1.0f, 1.0f);  // Setup shape
        Body body = BodyTool.createBody(world, position, shape, filter, maxDensity, zeroFriction, maxRestitution, isSensor, type);

        Fixture fixture = body.getFixtureList().get(0);
        assertEquals(maxDensity, fixture.getDensity(), "Fixture density should handle extreme values");
        assertEquals(zeroFriction, fixture.getFriction(), "Fixture friction should handle zero correctly");
        assertEquals(maxRestitution, fixture.getRestitution(), "Fixture restitution should handle max value");

        shape.dispose();
    }
}


