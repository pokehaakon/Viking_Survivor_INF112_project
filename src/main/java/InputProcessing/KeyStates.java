package InputProcessing;

import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.*;

public class KeyStates {
    private Map<Integer, GameKey> keyBindsMap;
    private Map<GameKey, Integer> ikeyBindsMap;
    private Map<GameKey, Boolean> stateOfKey;
    public KeyStates(List<Pair<Integer, GameKey>> initialKeyBindsList) {
        keyBindsMap = new HashMap<>();
        ikeyBindsMap = new HashMap<>();
        Set<GameKey> values = new HashSet<>();
        for (Pair<Integer, GameKey> p : initialKeyBindsList) {
            if (keyBindsMap.get(p.getLeft()) != null) {
                throw new RuntimeException("not all input keys are unique!");
            }
            if (values.contains(p.getRight())) {
                throw new RuntimeException("several keys bound to same key");
            }
            values.add(p.getRight());
            keyBindsMap.put(p.getLeft(), p.getRight());
            ikeyBindsMap.put(p.getRight(), p.getLeft());
        }

        stateOfKey = new HashMap<>();
        for (GameKey gk : GameKey.values()) {
            stateOfKey.put(gk, false);
        }
    }

    public boolean getState(GameKey gk) {
        return stateOfKey.get(gk);
    }

    public boolean updateInputKey(int keyCode, boolean state) {
        if (!keyBindsMap.containsKey(keyCode)) {
            return false;
        }
        stateOfKey.put(keyBindsMap.get(keyCode), state);
        return true;
    }

    public boolean setInputKey(int keyCode) {
        return updateInputKey(keyCode, true);
    }

    public boolean unsetInputKey(int keyCode) {
        return updateInputKey(keyCode, false);
    }



}
