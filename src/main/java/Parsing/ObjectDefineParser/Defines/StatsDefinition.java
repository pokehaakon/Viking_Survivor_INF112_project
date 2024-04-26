package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class StatsDefinition {
    public static final Set<String> legalDefines = Set.of("HP", "Speed", "Damage", "resistance", "scale");

    public final float hp, speed, damage, resistance, scale;

    private StatsDefinition(float hp, float speed, float damage, float resistance, float scale) {
        this.hp = hp;
        this.speed = speed;
        this.damage = damage;
        this.resistance = resistance;
        this.scale = scale;
    }

    public static StatsDefinition of(float hp, float speed, float damage, float resistance, float scale) {
        return new StatsDefinition(hp, speed, damage, resistance, scale);
    }

    @Override
    public String toString() {
        return "StatsDefinition(HP: " + hp + ", Speed: " + speed + ", Damage: " + damage + ", resistance: " + resistance + ", scale: " + scale + ")";
    }
}
