package Parsing;

import Parsing.ObjectDefineParser.ObjectDefineParser;
import Parsing.Parser.ParsingException;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ObjectDefineParserTest {
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
            public void dispose() {}};
        new HeadlessApplication(listener, config);
    }

    @Test
    void testObjectDefineParser() throws ParsingException {
        ObjectDefineParser parser = new ObjectDefineParser("ObjectDefines/test.obj");
        var e = parser.parseDocument();
    }

}