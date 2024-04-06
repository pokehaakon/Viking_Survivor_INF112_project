package Parsing;

import Actors.Enemy.EnemyType;

public enum SpawnType {
    BOSS, WAVE;

    public static String[] stringValues() {
        int len = SpawnType.values().length;
        String[] strings = new String[len];
        int i = 0;
        for (SpawnType e : SpawnType.values()) {
            strings[i++] = e.toString();
        }
        return strings;
    }
}
