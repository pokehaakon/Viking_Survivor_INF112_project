package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class StatsDefinition {
    public static final Set<String> legalDefines = Set.of("HP", "Speed", "Damage", "resistance");

    public final float hp, speed, damage, resistance;

    private StatsDefinition(float hp, float speed, float damage, float resistance) {
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.resistance = resistance;
    }

    public static StatsDefinition of(float hp, float speed, float damage, float resistance) {
        return new StatsDefinition(hp, speed, damage, resistance);
    }

    @Override
    public String toString() {
        return "StatsDefinition(HP: " + hp + ", Speed: " + speed + ", Damage: " + damage + ", resistance: " + resistance + ")";
    }
}
