package Parsing;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static Parsing.ParserTest.*;

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
        assertThrowsExactly(Parser.ParserException.class, m::parseDefines);
    }


    @Test
    void parseFrameBody() {
        MapParser m = mapParserFromString("""
                    EnemyName AnotherEnemy; BOSS 100 HARD
                    Bat; Wave 10 20 #this should be ignored
                """);

        List<MapParser.SpawnFrame> frames = m.parseFrameBody();
        MapParser.SpawnFrame frame;

        assertEquals(2, frames.size());
        frame = frames.get(0);
        assertEquals(2, frame.spawnable().size());
        assertEquals("EnemyName", frame.spawnable().get(0));
        assertEquals("AnotherEnemy", frame.spawnable().get(1));
        assertEquals("BOSS", frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));

        frame = frames.get(1);
        assertEquals(1, frame.spawnable().size());
        assertEquals("Bat", frame.spawnable().get(0));
        assertEquals("Wave", frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("10", frame.args().get(0));
        assertEquals("20", frame.args().get(1));
    }


    @Test
    void parseTimeFrames() {
        MapParser m = mapParserFromString("""
                00:01:
                    Bat; Wave 10 20
                300f:
                    EnemyName AnotherEnemy; BOSS 100 HARD
                """);

        Map<Long, List<MapParser.SpawnFrame>> spawnFrames;
        MapParser.SpawnFrame frame;
        spawnFrames = m.parseTimeFrames();

        assertTrue(spawnFrames.containsKey(60L));
        assertEquals(1, spawnFrames.get(60L).size());
        frame = spawnFrames.get(60L).get(0);
        assertEquals(1, frame.spawnable().size());
        assertEquals("Bat", frame.spawnable().get(0));
        assertEquals("Wave", frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("10", frame.args().get(0));
        assertEquals("20", frame.args().get(1));

        assertTrue(spawnFrames.containsKey(300L));
        assertEquals(1, spawnFrames.get(300L).size());
        frame = spawnFrames.get(300L).get(0);
        assertEquals(2, frame.spawnable().size());
        assertEquals("EnemyName", frame.spawnable().get(0));
        assertEquals("AnotherEnemy", frame.spawnable().get(1));
        assertEquals("BOSS", frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));
    }
}