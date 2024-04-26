package Parsing.ObjectDefineParser.Defines;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;

import java.util.Set;

public class StructureDefinition {
    public static final Set<String> legalDefines = Set.of("Category", "Filter", "Shape", "BodyType", "isSensor", "density", "friction");

    public final Filter filter;
    public final ShapeDefinition shapeDefinition;
    public final float density, friction;
    public final boolean isSensor;
    public final BodyDef.BodyType bodyType;

    private StructureDefinition(Filter filter, ShapeDefinition shapeDefinition, float density, float friction, boolean isSensor, BodyDef.BodyType bodyType) {

        this.filter = filter;
        this.shapeDefinition = shapeDefinition;
        this.density = density;
        this.friction = friction;
        this.isSensor = isSensor;
        this.bodyType = bodyType;
    }

    public static StructureDefinition of(Filter filter, ShapeDefinition shapeDefinition, float density, float friction, boolean isSensor, BodyDef.BodyType bodyType) {
        return new StructureDefinition(filter, shapeDefinition, density, friction, isSensor, bodyType);
    }

    @Override
    public String toString() {
        return "StructureDefinition(Filter: " + "{C:" + filter.categoryBits + " M:" + filter.maskBits + "}"
                + ", ShapeDefinition: " + shapeDefinition.toString()
                + ", density:" + density
                + ", friction:" + friction + ")";
    }

}
