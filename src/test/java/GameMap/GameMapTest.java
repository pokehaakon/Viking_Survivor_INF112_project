package GameMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameMapTest {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {
            @Override
            public void create() {}
            @Override
            public void resize(int width, int height) {}
            @Override
            public void render() {}
            @Override
            public void pause() {}
            @Override
            public void resume() {}

            @Override
            public void dispose() {}
        };
        new HeadlessApplication(listener, config);
        Box2D.init();
        Gdx.gl = mock(GL20.class);
    }

    @Test
    void testGetMiddleOfMapPosition() {
        GameMap map = new GameMap("TiledMap/damaged_roads_map.tmx", 1/4f);
        // testMap.tmx has height/width of 1024px, with the scale 1/4 this turn out to be 256px
        Vector2 middleOfMap = new Vector2(256f, 256f);
        assertEquals(middleOfMap, map.getMiddleOfMapPosition());
    }

    @Test
    void testMapBorder() {
        GameMap map = new GameMap("TiledMap/damaged_roads_map.tmx", 1/4f);
        World world = new World(new Vector2(0,0),true);
        map.createMapBorder(world);

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        ArrayList<Vector2> vertices = new ArrayList<>();
        vertices.add(new Vector2(0,0));
        vertices.add(new Vector2(0,512));
        vertices.add(new Vector2(512,512));
        vertices.add(new Vector2(512,0));
        vertices.add(new Vector2(0,0));

        ArrayList<Vector2> mapVertices = new ArrayList<>();
        ChainShape shape = (ChainShape) bodies.get(0).getFixtureList().get(0).getShape();
        for(int i=0; i<shape.getVertexCount(); i++) {
            Vector2 vertex = new Vector2(0,0);
            shape.getVertex(i, vertex);
            mapVertices.add(vertex);
        }
        assertEquals(1, world.getBodyCount());
        assertEquals(5, shape.getVertexCount());
        assertEquals(vertices, mapVertices);
    }
}
