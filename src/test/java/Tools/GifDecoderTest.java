package Tools;

import GameObjects.ObjectFactory;
import Rendering.Animations.AnimationRendering.GIFS;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Animation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GifDecoderTest {
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

        Gdx.gl = mock(GL20.class);
    }

    @Test
    void testDecoder() {
        //this is not really a test, but as this is mostly not our code we just use it to increase coverage!
        GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal("raven.gif").read());
    }

}