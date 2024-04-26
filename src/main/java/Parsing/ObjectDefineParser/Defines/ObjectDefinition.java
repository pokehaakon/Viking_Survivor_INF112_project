package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class ObjectDefinition {
    public static final Set<String> legalDefines = Set.of("Animation", "Structure", "Scale");
    public final AnimationDefinition animationDefinition;
    public final StructureDefinition structureDefinition;
    public final float scale;

    private ObjectDefinition(AnimationDefinition animationDefinition, StructureDefinition structureDefinition, float scale) {
        this.animationDefinition = animationDefinition;
        this.structureDefinition = structureDefinition;
        this.scale = scale;
    }

    public static ObjectDefinition of(AnimationDefinition animationDefinition, StructureDefinition structureDefinition, float scale) {
        return new ObjectDefinition(animationDefinition, structureDefinition, scale);
    }

    @Override
    public String toString() {
        return "ObjectDefinition("
                + "\n\tanimationDefinition: "+ animationDefinition
                + "\n\tstructureDefinition" + structureDefinition + "\n)";
    }
}
