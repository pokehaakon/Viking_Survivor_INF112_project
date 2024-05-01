package InputProcessing;

import Tools.Tuple;
import com.badlogic.gdx.Input;
import org.javatuples.Pair;

import java.util.*;

public class KeyStates {
    private Map<Integer, GameKey> keyBindsMap;
    private Map<GameKey, Integer> ikeyBindsMap;
    private Map<GameKey, Boolean> stateOfKey;

    /**
     *
     * @param initialKeyBindsList list of pairs of input key_values (int) and GameKey value
     *                            the list should represent a bijection (1 to 1)
     */
    public KeyStates(List<Pair<Integer, GameKey>> initialKeyBindsList) {
        build(initialKeyBindsList);
    }

    /**
     * Creates a default keybinding!
     */
    public KeyStates() {
        //TODO load key binds from config?
        List<Pair<Integer, GameKey>> keyBinds = new ArrayList<>();
        keyBinds.add(Tuple.of(Input.Keys.W, GameKey.UP));
        keyBinds.add(Tuple.of(Input.Keys.A, GameKey.LEFT));
        keyBinds.add(Tuple.of(Input.Keys.S, GameKey.DOWN));
        keyBinds.add(Tuple.of(Input.Keys.D, GameKey.RIGHT));
        keyBinds.add(Tuple.of(Input.Keys.ESCAPE, GameKey.QUIT));

        build(keyBinds);
    }

    private void build(List<Pair<Integer, GameKey>> keyBinds) {
        keyBindsMap = new HashMap<>();
        ikeyBindsMap = new EnumMap<>(GameKey.class);
        Set<GameKey> values = new HashSet<>();
        for (Pair<Integer, GameKey> p : keyBinds) {
            if (keyBindsMap.get(p.getValue0()) != null) {
                throw new RuntimeException("not all input keys are unique!");
            }
            if (values.contains(p.getValue1())) {
                throw new RuntimeException("several keys bound to same key");
            }
            values.add(p.getValue1());
            keyBindsMap.put(p.getValue0(), p.getValue1());
            ikeyBindsMap.put(p.getValue1(), p.getValue0());
        }

        stateOfKey = new EnumMap<>(GameKey.class);
        for (GameKey gk : GameKey.values()) {
            stateOfKey.put(gk, false);
        }


    }

    /**
     *
     * @param gk
     * @return the boolean state of the GameKey gk
     */
    public boolean getState(GameKey gk) {
        return stateOfKey.get(gk);
    }

    /**
     * Sets the state of the GameKey bound to 'keyCode' to 'state'
     *
     * @param keyCode
     * @param state
     * @return true if the keyCode is bound to a GameKey, false if it isn't
     */
    public boolean updateInputKey(int keyCode, boolean state) {
        if (!keyBindsMap.containsKey(keyCode)) {
            return false;
        }
        stateOfKey.put(keyBindsMap.get(keyCode), state);
        return true;
    }

    /**
     * Sets the GameKey bound to 'keyCode' to true
     * @param keyCode
     * @return true if keyCode is bound, false if it isn't
     */
    public boolean setInputKey(int keyCode) {
        return updateInputKey(keyCode, true);
    }

    /**
     * Sets the GameKey bound to 'keyCode' to false
     * @param keyCode
     * @return true if keyCode is bound, false if it isn't
     */
    public boolean unsetInputKey(int keyCode) {
        return updateInputKey(keyCode, false);
    }

    public boolean noKeyIsPressed() {
        boolean noKeyIsPressed = true;
        for(GameKey gk:KeyStates.GameKey.values()) {
            if(stateOfKey.get(gk)) {
                noKeyIsPressed = false;
                break;
            }

        }
        return noKeyIsPressed;
    }

    public enum GameKey {
        UP,RIGHT,DOWN,LEFT,QUIT
    }
}
