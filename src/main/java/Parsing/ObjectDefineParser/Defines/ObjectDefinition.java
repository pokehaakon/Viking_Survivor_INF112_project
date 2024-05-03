package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class ObjectDefinition {
    public static final Set<String> legalDefines = Set.of("Animation", "Structure");
    public final AnimationDefinition animationDefinition;
    public final StructureDefinition structureDefinition;

    private ObjectDefinition(AnimationDefinition animationDefinition, StructureDefinition structureDefinition) {
        this.animationDefinition = animationDefinition;
        this.structureDefinition = structureDefinition;
    }

    public static ObjectDefinition of(AnimationDefinition animationDefinition, StructureDefinition structureDefinition) {
        return new ObjectDefinition(animationDefinition, structureDefinition);
    }

    @Override
    public String toString() {
        return "ObjectDefinition("
                + "\n\tAnimationDefinition: "+ animationDefinition
                + "\n\tStructureDefinition" + structureDefinition + "\n)";
    }
}
