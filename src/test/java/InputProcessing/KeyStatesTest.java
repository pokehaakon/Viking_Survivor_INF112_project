package InputProcessing;

import Tools.Tuple;
import org.javatuples.Pair;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KeyStatesTest {

    @Test
    void TestDefaultStatesAndBinds() {
        var keyStates = new KeyStates();

        assertFalse(keyStates.getState(KeyStates.GameKey.UP));
        assertFalse(keyStates.getState(KeyStates.GameKey.DOWN));
        assertFalse(keyStates.getState(KeyStates.GameKey.LEFT));
        assertFalse(keyStates.getState(KeyStates.GameKey.RIGHT));
        assertFalse(keyStates.getState(KeyStates.GameKey.QUIT));

        var pairs = List.of(Tuple.of(1, KeyStates.GameKey.UP), Tuple.of(2, KeyStates.GameKey.UP));
        var pairs2 = List.of(Tuple.of(1, KeyStates.GameKey.UP), Tuple.of(1, KeyStates.GameKey.DOWN));

        assertThrowsExactly(RuntimeException.class, () -> new KeyStates(pairs));
        assertThrowsExactly(RuntimeException.class, () -> new KeyStates(pairs2));

        var newPairs = List.of(Tuple.of(1, KeyStates.GameKey.UP), Tuple.of(2, KeyStates.GameKey.DOWN));

        keyStates = new KeyStates(newPairs);
        keyStates.setInputKey(1);

        assertTrue(keyStates.getState(KeyStates.GameKey.UP));
        assertFalse(keyStates.getState(KeyStates.GameKey.DOWN));

        keyStates.setInputKey(2);

        assertTrue(keyStates.getState(KeyStates.GameKey.UP));
        assertTrue(keyStates.getState(KeyStates.GameKey.DOWN));

        keyStates.unsetInputKey(1);

        assertFalse(keyStates.getState(KeyStates.GameKey.UP));
        assertTrue(keyStates.getState(KeyStates.GameKey.DOWN));

        keyStates.updateInputKey(2, true);

        assertFalse(keyStates.getState(KeyStates.GameKey.UP));
        assertTrue(keyStates.getState(KeyStates.GameKey.DOWN));

        assertFalse(keyStates.noKeyIsPressed());

        keyStates.unsetInputKey(2);

        assertTrue(keyStates.noKeyIsPressed());

        assertTrue(keyStates.updateInputKey(1, true));
        assertFalse(keyStates.updateInputKey(3, true));
    }

}