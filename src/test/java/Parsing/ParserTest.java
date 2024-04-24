package Parsing;

import GameObjects.ObjectTypes.EnemyType;
import Parsing.Parser.ParserException;
import Parsing.Parser.ParsingException;
import Parsing.Parser.TextParser;
import Parsing.Stream.CharArrayStream;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.javatuples.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


class ParserTest {

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
    void parseFrameBody() throws ParsingException {
        MapParser m = mapParserFromString("""
                    RAVEN; BOSS 100 HARD
                    RAVEN; WAVE 10 20 #this should be ignored
                """);

        List<SpawnFrame> frames = m.parseFrameBody();
        SpawnFrame frame;

        assertEquals(2, frames.size());
        frame = frames.get(0);
        //assertEquals(2, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable());
        //assertEquals(EnemyType.ORC, frame.spawnable().get(1));
        assertEquals(SpawnType.BOSS, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));

        frame = frames.get(1);
        //assertEquals(1, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable());
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
                    ORC; BOSS 100 HARD
                """);


        SpawnFrame frame;
        var spawnFramesList = m.parseTimeFrames();
        var spawnFrames = spawnFramesList.stream().collect(Collectors.toMap(Pair::getValue0, Pair::getValue1));

        assertTrue(spawnFrames.containsKey(60L));
        assertEquals(1, spawnFrames.get(60L).size());
        frame = spawnFrames.get(60L).get(0);
        //assertEquals(1, frame.spawnable().size());
        assertEquals(EnemyType.RAVEN, frame.spawnable());
        assertEquals(SpawnType.WAVE, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("10", frame.args().get(0));
        assertEquals("20", frame.args().get(1));

        assertTrue(spawnFrames.containsKey(300L));
        assertEquals(1, spawnFrames.get(300L).size());
        frame = spawnFrames.get(300L).get(0);
        //assertEquals(2, frame.spawnable().size());
        assertEquals(EnemyType.ORC, frame.spawnable());
        //assertEquals(EnemyType.ORC, frame.spawnable().get(1));
        assertEquals(SpawnType.BOSS, frame.spawnType());
        assertEquals(2, frame.args().size());
        assertEquals("100", frame.args().get(0));
        assertEquals("HARD", frame.args().get(1));
    }

    @Test
    void testWorld() {
        MapParser p = mapParserFromString("""
                !MAP_PATH=assets/damaged_roads_map.tmx
                                
                00:00:
                    #ORC; BOSS 100 HARD
                    #RAVEN; WAVE 10 20
                    RAVEN; CONTINUOUS size:10 maxSpawnEachFrame:1
                                
                00:10:
                    ORC; WAVE size:20 maxSpawnEachFrame:1
                                
                """);
        //MapParser p = new MapParser("mapdefines/test.wdef");
        p.doParse();
        var defines = p.getDefines();

        assertEquals(1, defines.size());
        assertTrue(defines.containsKey("MAP_PATH"));
        assertEquals("assets/damaged_roads_map.tmx", defines.get("MAP_PATH"));

        var frames = p.getTimeFrames();
        assertEquals(2, frames.size());


    }

    @Test
    void testTextParser() throws ParsingException {
        TextParser parser = new TextParser("aab112".toCharArray());

        assertEquals("a", parser.letter());
        assertEquals("ab", parser.letters());
        assertThrowsExactly(ParsingException.class, parser::letter);

        assertEquals("1", parser.number());
        assertEquals("12", parser.numbers());
        assertThrowsExactly(ParsingException.class, parser::number);


        parser = new TextParser("\t\t  abc".toCharArray());
        assertEquals("\t\t  ", parser.space());


        parser = new TextParser("\t\t \nabc\n".toCharArray());
        assertEquals("\t\t \n", parser.skipLine());
        assertThrowsExactly(ParsingException.class, parser::skipLine);

        parser = new TextParser("\n\ra".toCharArray());
        assertEquals("\n", parser.parseNewLineLiteral());
        assertEquals("\r", parser.parseNewLineLiteral());
        assertThrowsExactly(ParsingException.class, parser::parseNewLineLiteral);

        parser = new TextParser("\t\t \n".toCharArray());
        assertEquals("\t\t \n", parser.parseEmptyLine());
        assertThrowsExactly(ParsingException.class, parser::parseEmptyLine);
    }


    @Test
    void testGenericParser() throws ParsingException {
        TextParser parser1 = new TextParser("parsed".toCharArray());

        assertThrowsExactly(ParsingException.class, () -> parser1.error(""));
        assertThrowsExactly(ParsingException.class, () -> parser1.shouldError(() -> null));

        assertEquals("parsed", parser1.iorElse(parser1.iparseStringLiteral("parsed"), "default").get());
        assertEquals("default", parser1.iorElse(parser1.iparseStringLiteral("parsed"), "default").get());


        TextParser parser2 = new TextParser("undo".toCharArray());
        assertThrowsExactly(ParsingException.class, () -> parser2.undo(() -> {
                parser2.parseLiteral('u');
                parser2.error("");
                return null;
        }));
        assertEquals("u", parser2.undo(parser2.iparseLiteral('u')));
        assertEquals("undo", parser2.parseStringLiteral("undo"));


        TextParser parser3 = new TextParser("___stripped___next".toCharArray());
        assertEquals("stripped", parser3.istrip(parser3.iparseStringLiteral("stripped"), '_').get());
        assertEquals("next", parser3.parseStringLiteral("next"));

        parser3.reset();
        assertEquals("_", parser3.next());
        assertEquals("__s", parser3.next(3));
    }

    @Test
    void fileNameTester() {
        MapParser p = new MapParser("mapdefines/test.wdef");
        var a = p.stream;
        var b = p.stream.copy();

        assertEquals(a, b);
        assertEquals(a, a);

//        p.doParse();
//
//        System.out.println(p.getDefines());
//        System.out.println(p.getTimeFrames());
    }

    @Test
    void charStreamTest() {
        CharArrayStream stream = new CharArrayStream("this is a stream\nwith multiple\nlines");
        assertEquals(0, stream.getLine());
        assertEquals(0, stream.getLinePos());

        for (int i = 0; i < 16; i++) {
            assertDoesNotThrow(stream::next);
        }

        assertEquals(0, stream.getLine());
        assertEquals(16, stream.getLinePos());

        assertDoesNotThrow(stream::next);

        assertEquals(1, stream.getLine());
        assertEquals(0, stream.getLinePos());

        assertDoesNotThrow(stream::next);

        assertEquals("with multiple\n ^", stream.getDebugInfo());
        assertEquals('i', stream.getCurrent());


        assertNotEquals("", stream);

    }
}