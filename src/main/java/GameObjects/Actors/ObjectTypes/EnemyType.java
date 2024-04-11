package GameObjects.Actors.ObjectTypes;

public enum EnemyType {
    RAVEN, ORC, WOLF;

    public static String[] stringValues() {
        int len = EnemyType.values().length;
        String[] strings = new String[len];
        int i = 0;
        for (EnemyType e : EnemyType.values()) {
            strings[i++] = e.toString();
        }
        return strings;
    }
}
