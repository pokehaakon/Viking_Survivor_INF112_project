package Parsing;

import GameObjects.ObjectTypes.EnemyType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


class MapParserTest {
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
                    ENEMY1 ENEMY2; BOSS 100 HARD
                    ENEMY1; WAVE 10 20 #this should be ignored
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
                    ENEMY1; WAVE 10 20
                300f:
                    ENEMY1 ENEMY2; BOSS 100 HARD
                """);

        Map<Long, List<SpawnFrame>> spawnFrames;
        SpawnFrame frame;
        spawnFrames = m.parseTimeFrames();

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
}