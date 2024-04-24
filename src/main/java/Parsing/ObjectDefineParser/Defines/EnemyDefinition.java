package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class EnemyDefinition {
    public static final Set<String> legalDefines = Set.of("Animation", "Stats", "Structure");
    public final AnimationDefinition animationDefinition;
    public final StatsDefinition statsDefinition;
    public final StructureDefinition structureDefinition;

    private EnemyDefinition(AnimationDefinition animationDefinition, StatsDefinition statsDefinition, StructureDefinition structureDefinition) {
        this.animationDefinition = animationDefinition;
        this.statsDefinition = statsDefinition;
        this.structureDefinition = structureDefinition;
    }

    public static EnemyDefinition of(AnimationDefinition animationDefinition, StatsDefinition statsDefinition, StructureDefinition structureDefinition) {
        return new EnemyDefinition(animationDefinition, statsDefinition, structureDefinition);
    }

    @Override
    public String toString() {
        return "EnemyDefinition("
                + "\n\tanimationDefinition: "+ animationDefinition
                + "\n\tstatsDefinition" + statsDefinition
                + "\n\tstructureDefinition" + structureDefinition + "\n)";
    }
}
