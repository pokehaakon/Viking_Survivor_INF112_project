package Parsing.ObjectDefineParser.Defines;

import com.badlogic.gdx.physics.box2d.Filter;

import java.util.Set;

public class StructureDefinition {
    public static final Set<String> legalDefines = Set.of("Filter", "Shape", "density", "friction");

    public final Filter filter;
    public final ShapeDefinition shapeDefinition;
    public final float density, friction;

    private StructureDefinition(Filter filter, ShapeDefinition shapeDefinition, float density, float friction) {

        this.filter = filter;
        this.shapeDefinition = shapeDefinition;
        this.density = density;
        this.friction = friction;
    }

    public static StructureDefinition of(Filter filter, ShapeDefinition shapeDefinition, float density, float friction) {
        return new StructureDefinition(filter, shapeDefinition, density, friction);
    }

    @Override
    public String toString() {
        return "StructureDefinition(Filter: " + filter.maskBits
                + ", ShapeDefinition: " + shapeDefinition.toString()
                + ", density:" + density
                + ", friction:" + friction + ")";
    }

}
