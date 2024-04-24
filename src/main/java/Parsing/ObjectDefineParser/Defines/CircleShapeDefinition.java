package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class CircleShapeDefinition extends ShapeDefinition {
    public static final Set<String> legalDefines = Set.of("Type", "Radius");
    public final float radius;

    private CircleShapeDefinition(float radius) {
        this.radius = radius;
    }

    public static ShapeDefinition of(float radius) {
        return new CircleShapeDefinition(radius);
    }

    @Override
    public String toString() {
        return "CircleShapeDefinition(Radius: " + radius + ")";
    }
}
