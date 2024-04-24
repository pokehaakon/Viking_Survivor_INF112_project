package Parsing.ObjectDefineParser.Defines;

import java.util.Set;

public class SquareShapeDefinition extends ShapeDefinition {
    public static final Set<String> legalDefines = Set.of("Type", "Width", "Height");

    public final float width, height;

    private SquareShapeDefinition(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public static ShapeDefinition of(float width, float height) {
        return new SquareShapeDefinition(width, height);
    }

    @Override
    public String toString() {
        return "SquareShapeDefinition(Width: " + width + ", Height: " + height + ")";
    }
}
