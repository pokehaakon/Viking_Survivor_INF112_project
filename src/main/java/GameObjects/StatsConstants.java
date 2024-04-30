package GameObjects;


public abstract class StatsConstants {
    public static class Stats {
        public final float SPEED, HP, DAMAGE, RESISTANCE;
        public Stats(float speed, float hp, float damage, float resistance) {
            SPEED = speed;
            HP = hp;
            DAMAGE = damage;
            RESISTANCE = resistance;
        }
    }
}
