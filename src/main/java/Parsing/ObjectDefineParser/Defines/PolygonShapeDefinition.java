package Parsing.ObjectDefineParser.Defines;

import java.util.Arrays;
import java.util.Set;

public class PolygonShapeDefinition extends ShapeDefinition {
    public static final Set<String> legalDefines = Set.of("Type", "point");

    public final float[] points;

    private PolygonShapeDefinition(float[] points) {
        this.points = points;
    }

    public static ShapeDefinition of(float[] points) {
        return new PolygonShapeDefinition(points);
    }

    @Override
    public String toString() {
        return "PolygonShapeDefinition(points: " + Arrays.toString(points) + ")";
    }
}
