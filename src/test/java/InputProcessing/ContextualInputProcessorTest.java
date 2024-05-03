package InputProcessing;

import Contexts.Context;
import Contexts.ContextFactory;
import Tools.Tuple;
import com.badlogic.gdx.InputProcessor;
import org.javatuples.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContextualInputProcessorTest {
    static class TestingInputProcessor implements InputProcessor{
        public Unit<Integer> lastKeyDownCallValue;
        public Unit<Integer> lastKeyUpCallValue;
        public Unit<Character> lastKeyTypedCallValue;
        public Quartet<Integer, Integer, Integer, Integer> lastTouchDownCallValue;
        public Quartet<Integer, Integer, Integer, Integer> lastTouchUpCallValue;
        public Quartet<Integer, Integer, Integer, Integer> lastTouchCancelledCallValue;
        public Triplet<Integer, Integer, Integer> lastTouchDraggedCallValue;
        public Pair<Integer, Integer> lastMouseMovedCallValue;
        public Pair<Float, Float> lastScrolledCallValue;
        private final boolean returnValue;

        public TestingInputProcessor(boolean ret) {returnValue = ret;}


        @Override
        public boolean keyDown(int keycode) {
            lastKeyDownCallValue = Tuple.of(keycode);
            return returnValue;
        }

        @Override
        public boolean keyUp(int keycode) {
            lastKeyUpCallValue = Tuple.of(keycode);
            return returnValue;
        }

        @Override
        public boolean keyTyped(char character) {
            lastKeyTypedCallValue = Tuple.of(character);
            return returnValue;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            lastTouchDownCallValue = Tuple.of(screenX, screenY, pointer, button);
            return returnValue;
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            lastTouchUpCallValue = Tuple.of(screenX, screenY, pointer, button);
            return returnValue;
        }

        @Override
        public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
            lastTouchCancelledCallValue = Tuple.of(screenX, screenY, pointer, button);
            return returnValue;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            lastTouchDraggedCallValue = Tuple.of(screenX, screenY, pointer);
            return returnValue;
        }

        @Override
        public boolean mouseMoved(int screenX, int screenY) {
            lastMouseMovedCallValue = Tuple.of(screenX, screenY);
            return returnValue;
        }

        @Override
        public boolean scrolled(float amountX, float amountY) {
            lastScrolledCallValue = Tuple.of(amountX, amountY);
            return returnValue;
        }
    }

    @Test
    void testInputProcessor() {
        Context c1 = mock(Context.class);
        TestingInputProcessor i1 = new TestingInputProcessor(false);
        when(c1.getInputProcessor())
                .thenReturn(i1);

        Context c2 = mock(Context.class);
        TestingInputProcessor i2 = new TestingInputProcessor(true);
        when(c2.getInputProcessor())
                .thenReturn(i2);

        Context c3 = mock(Context.class);
        TestingInputProcessor i3 = new TestingInputProcessor(true);
        when(c3.getInputProcessor())
                .thenReturn(i3);

        ContextFactory f = mock(ContextFactory.class);
        when(f.getContext("1"))
                .thenReturn(c1);
        when(f.getContext("2"))
                .thenReturn(c2);

        doAnswer(x -> {
            when(c3.getInputProcessor())
                    .thenReturn(i3);
            when(f.getContext("1")).thenReturn(c3);
            return null;
        }).when(f).deleteContext("1");



        ContextualInputProcessor inProc = new ContextualInputProcessor(x -> f);

        assertNull(inProc.getCurrentContext());
        assertThrowsExactly(NullPointerException.class, () -> inProc.keyDown(1));

        inProc.setContext("1");
        assertSame(c1, inProc.getCurrentContext());
        assertFalse(inProc.keyDown(0));
        assertFalse(inProc.keyUp(0));
        assertFalse(inProc.keyTyped('a'));
        assertFalse(inProc.touchDown(0, 1, 2, 3));
        assertFalse(inProc.touchUp(0, 1, 2, 3));
        assertFalse(inProc.touchCancelled(0, 1, 2, 3));
        assertFalse(inProc.touchDragged(0, 1, 2));
        assertFalse(inProc.mouseMoved(0, 1));
        assertFalse(inProc.scrolled(0.0f, 1.0f));

        assertEquals(Tuple.of(0), i1.lastKeyDownCallValue);
        assertEquals(Tuple.of(0), i1.lastKeyUpCallValue);
        assertEquals(Tuple.of('a'), i1.lastKeyTypedCallValue);
        assertEquals(Tuple.of(0, 1, 2, 3), i1.lastTouchDownCallValue);
        assertEquals(Tuple.of(0, 1, 2, 3), i1.lastTouchUpCallValue);
        assertEquals(Tuple.of(0, 1, 2, 3), i1.lastTouchCancelledCallValue);
        assertEquals(Tuple.of(0, 1, 2), i1.lastTouchDraggedCallValue);
        assertEquals(Tuple.of(0, 1), i1.lastMouseMovedCallValue);
        assertEquals(Tuple.of(0f, 1f), i1.lastScrolledCallValue);

        inProc.setContext("2");
        assertSame(c2, inProc.getCurrentContext());

        assertTrue(inProc.keyDown(10));
        assertTrue(inProc.keyUp(10));
        assertTrue(inProc.keyTyped('b'));
        assertTrue(inProc.touchDown(10, 9, 8, 7));
        assertTrue(inProc.touchUp(10, 9, 8, 7));
        assertTrue(inProc.touchCancelled(10, 9, 8, 7));
        assertTrue(inProc.touchDragged(10, 9, 8));
        assertTrue(inProc.mouseMoved(10, 9));
        assertTrue(inProc.scrolled(10.0f, 9.0f));

        assertEquals(Tuple.of(10), i2.lastKeyDownCallValue);
        assertEquals(Tuple.of(10), i2.lastKeyUpCallValue);
        assertEquals(Tuple.of('b'), i2.lastKeyTypedCallValue);
        assertEquals(Tuple.of(10, 9, 8, 7), i2.lastTouchDownCallValue);
        assertEquals(Tuple.of(10, 9, 8, 7), i2.lastTouchUpCallValue);
        assertEquals(Tuple.of(10, 9, 8, 7), i2.lastTouchCancelledCallValue);
        assertEquals(Tuple.of(10, 9, 8), i2.lastTouchDraggedCallValue);
        assertEquals(Tuple.of(10, 9), i2.lastMouseMovedCallValue);
        assertEquals(Tuple.of(10.f, 9.f), i2.lastScrolledCallValue);

        inProc.removeContext("1");
        inProc.setContext("1");
        assertSame(c3, inProc.getCurrentContext());

        assertTrue(inProc.keyDown(10));
        assertEquals(Tuple.of(0), i1.lastKeyDownCallValue);
        assertEquals(Tuple.of(10), i3.lastKeyDownCallValue);

        inProc.dispose();
        verify(f, times(1)).dispose();

    }

}