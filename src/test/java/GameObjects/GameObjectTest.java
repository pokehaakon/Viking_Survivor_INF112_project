package GameObjects;

import Rendering.Animations.AnimationRendering.AnimationHandler;
import Tools.FilterTool;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameObjectTest {

    World world;
    GameObject obj;

    private static Actor createTestObject(World world) {

        BodyFeatures bodyFeatures = new BodyFeatures(
                createCircleShape(1),
                mock(Filter.class),
                1,
                1,
                1,
                false,
                BodyDef.BodyType.DynamicBody);
        Actor a = new Actor("TEST",mock(AnimationHandler.class),bodyFeatures, new StatsConstants.Stats(1,1,1,1));
        a.addToWorld(world);
        return a;
    }

    @BeforeEach
    void setUp() {
        world = new World(new Vector2(0,0),true);
        obj = createTestObject(world);

    }

    @Test
    void outOfBounds_withinBounds() {
        GameObject centerObject = createTestObject(world);
        centerObject.setPosition(new Vector2(0,0));
        obj.setPosition(new Vector2(1,0));
        assertFalse(obj.outOfBounds(centerObject,new Vector2(2f,2f)));
    }
    @Test
    void outOfBounds_notWithinBounds() {
        GameObject centerObject = createTestObject(world);
        centerObject.setPosition(new Vector2(0,0));
        obj.setPosition(new Vector2(0.5f,1));
        assertTrue(obj.outOfBounds(centerObject,new Vector2(1f,1f)));
    }
    @Test
    void outOfBounds_onTheEdge() {
        GameObject centerObject = createTestObject(world);
        centerObject.setPosition(new Vector2(0,0));
        obj.setPosition(new Vector2(0.5f,0.5f));
        assertFalse(obj.outOfBounds(centerObject,new Vector2(1f,1f)));
    }
}