package Simulation;

import GameObjects.Actor;
import GameObjects.ObjectFactory;
import Simulation.SpawnHandler.SpawnHandlerFactory;
import Tools.Pool.ObjectPool;
import Tools.Pool.SmallPool;
import Tools.ShapeTools;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.utils.Array;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GameWorldTest {

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
    void gameWorldTest() throws NoSuchFieldException, IllegalAccessException {
        ObjectPool<Actor> pool = mock(ObjectPool.class);
        SmallPool<Actor> smallPool = mock(SmallPool.class);
        Actor fakeActor = mock(Actor.class);
        Body fakeBody = mock(Body.class);
        Fixture fakeFixture = mock(Fixture.class);
        Array<Fixture> fixtures = new Array<>();
        fixtures.add(fakeFixture);


        when(pool.getSmallPool("RAVEN")).thenReturn(smallPool);
        when(pool.getSmallPool("ORC")).thenReturn(smallPool);
        when(pool.get("RAVEN")).thenReturn(fakeActor);
        when(pool.get("ORC")).thenReturn(fakeActor);
        when(smallPool.get()).thenReturn(fakeActor);
        when(smallPool.get(20)).thenReturn(Stream.generate(() -> fakeActor).limit(20).toList());
        when(smallPool.get("ORC")).thenReturn(fakeActor);
        when(smallPool.get("RAVEN")).thenReturn(fakeActor);
        when(fakeActor.getBody()).thenReturn(fakeBody);
        when(fakeBody.getPosition()).thenReturn(Vector2.Zero);
        when(fakeBody.getFixtureList()).thenReturn(fixtures);
        when(fakeFixture.getShape()).thenReturn(ShapeTools.createCircleShape(1));

        List<Actor> actors = new ArrayList<>();
        GameWorld gw = new GameWorld("mapdefines/test.wdef", pool, actors);

        assertEquals(1, actors.size());
        assertEquals("RAVEN", actors.get(0).getName());

        //need to set player to fake actor inside the spawnHandlerFactory
        var spawnHandlerFactoryField = GameWorld.class.getDeclaredField("handlerFactory");
        spawnHandlerFactoryField.setAccessible(true);
        SpawnHandlerFactory factory = (SpawnHandlerFactory) spawnHandlerFactoryField.get(gw);
        var playerField = SpawnHandlerFactory.class.getDeclaredField("player");
        playerField.setAccessible(true);
        playerField.set(factory, fakeActor);

        assertNotNull(gw.getGameMap());
        var frameIndexField = GameWorld.class.getDeclaredField("frameIndex");
        frameIndexField.setAccessible(true);

        assertEquals(0, (int) frameIndexField.get(gw));
        gw.act(0L);
        assertEquals(1, (int) frameIndexField.get(gw));
        gw.act(599L);
        assertEquals(1, (int) frameIndexField.get(gw));
        gw.act(600L);
        assertEquals(2, (int) frameIndexField.get(gw));


    }

}