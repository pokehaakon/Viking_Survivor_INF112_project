package Parsing;

import GameObjects.ObjectTypes.EnemyType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


class MapParserTest {

    @BeforeAll
    static void setUpBeforeAll() {
        HeadlessApplicationConfiguration config = new HeadlessApplicationConfiguration();
        ApplicationListener listener = new ApplicationListener() {

            @Override
            public void create() {
            }

            @Override
            public void resize(int width, int height) {
                // TODO Auto-generated method stub

            }

            @Override
            public void render() {
                // TODO Auto-generated method stub

            }

            @Override
            public void pause() {
                // TODO Auto-generated method stub

            }

            @Override
            public void resume() {
                // TODO Auto-generated method stub

            }

            @Override
            public void dispose() {
                // TODO Auto-generated method stub

            }};
        new HeadlessApplication(listener, config);
    }

    public static MapParser mapParserFromString(String s) {
        return new MapParser(s.toCharArray());
    }
    @Test
    void parseDefines() {
        MapParser m = mapParserFromString("""
                !mapname=Springs
                !other=this
                """);

        Map<String, String> defines = m.parseDefines();

        assertTrue(defines.containsKey("mapname"));
        assertTrue(defines.containsKey("other"));

        assertEquals("Springs", defines.get("mapname"));
        assertEquals("this", defines.get("other"));

        m = mapParserFromString("""
                !mapname=Springs
                !other=
                """);
        assertThrowsExactly(ParserException.class, m::parseDefines);
    }


    @Test
    void parseFrameBody() {
        MapParser m = mapParserFromString("""
                    RAVEN ORC; BOSS 100 HARD
                    RAVEN; WAVE 10 20 #this should be ignored
                """);

        List<SpawnFrame> frames = m.parseFrameBody();
        SpawnFrame frame;

        assertEquals(2, frames.size());
        frame = frames.get(0);
        assertEquals(2, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable().get(0));
        assertEquals(EnemyType.ORC, frame.spawnable().get(1));
        assertEquals(SpawnType.BOSS, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));

        frame = frames.get(1);
        assertEquals(1, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable().get(0));
        assertEquals(SpawnType.WAVE, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("10", frame.args().get(0));
        assertEquals("20", frame.args().get(1));

        //test error handling in incorrect SpawnType 'Wave'
        m = mapParserFromString("""
                    ENEMY1 ENEMY2; BOSS 100 HARD
                    ENEMY1; Wave 10 20 #this should be ignored
                """);
        assertThrowsExactly(ParserException.class, m::parseFrameBody);

        //test error handling in incorrect EnemyType 'notEnemy'
        m = mapParserFromString("""
                    ENEMY1 notEnemy; BOSS 100 HARD
                    ENEMY1; Wave 10 20 #this should be ignored
                """);
        assertThrowsExactly(ParserException.class, m::parseFrameBody);

    }


    @Test
    void parseTimeFrames() {
        MapParser m = mapParserFromString("""
                00:01:
                    RAVEN; WAVE 10 20
                300f:
                    RAVEN ORC; BOSS 100 HARD
                """);


        SpawnFrame frame;
        var spawnFramesList = m.parseTimeFrames();
        var spawnFrames = spawnFramesList.stream().collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

        assertTrue(spawnFrames.containsKey(60L));
        assertTrue(spawnFrames.containsKey(60L));
        assertEquals(1, spawnFrames.get(60L).size());
        frame = spawnFrames.get(60L).get(0);
        assertEquals(1, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable().get(0));
        assertEquals(SpawnType.WAVE, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("10", frame.args().get(0));
        assertEquals("20", frame.args().get(1));

        assertTrue(spawnFrames.containsKey(300L));
        assertEquals(1, spawnFrames.get(300L).size());
        frame = spawnFrames.get(300L).get(0);
        assertEquals(2, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable().get(0));
        assertEquals(EnemyType.ORC, frame.spawnable().get(1));
        assertEquals(SpawnType.BOSS, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));
    }

    @Test
    void testWorld() {
        MapParser p = new MapParser("mapdefines/test.wdef");
        p.doParse();
        var defines = p.getDefines();

        assertEquals(1, defines.size());
        assertTrue(defines.containsKey("MAP_PATH"));
        assertEquals("assets/damaged_roads_map.tmx", defines.get("MAP_PATH"));

        var frames = p.getTimeFrames();
        assertEquals(1, frames.size());


    }
}