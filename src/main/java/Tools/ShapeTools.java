package Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static java.lang.Math.min;


/**
 * Contains functions for creating box2d shapes
 *
 * REMEMBER to call shape.dispose() after using the shape!!!
 */
public abstract class ShapeTools {
    public static PolygonShape createSquareShape(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.set(new float[] {0,0, width,0, width,height, 0,height});
        return shape;
    }

    public static CircleShape createCircleShape(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius);
        return shape;
    }

    private static Vector2 getBottomLeftCorrection(CircleShape c) {
        return new Vector2(-c.getRadius(), -c.getRadius());
    }

    private static Vector2 getBottomLeftCorrection(PolygonShape p) {
        Vector2 massCenter = getMassCenter(p);

        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY;
        Vector2 temp = new Vector2();
        for(int i = 0; i < p.getVertexCount(); i++) {
            p.getVertex(i, temp);
            minX = Math.min(minX, temp.x);
            minY = Math.min(minY, temp.y);
        }
        return new Vector2(massCenter.x - minX, massCenter.y - minY);
    }
    public static Vector2 getBottomLeftCorrection(Shape s) {
        if (s.getType() == Shape.Type.Circle) {
            return getBottomLeftCorrection((CircleShape) s);
        }
        if (s.getType() == Shape.Type.Polygon) {
            return getBottomLeftCorrection((PolygonShape) s);
        }
        throw new RuntimeException("Not implemented for this shape: " + s.getType() + " : " + s);
    }

    private static Vector2 getMassCenter(PolygonShape p) {
        Vector2 temp = new Vector2();
        Vector2 tempNext = new Vector2();
        float A = 0, Cx = 0, Cy = 0;
        float expr;
        int vertexes = p.getVertexCount();
        for(int i = 0; i < vertexes; i++) {
            p.getVertex(i, temp);
            p.getVertex((i+1) % vertexes, tempNext);
            expr = (temp.x * tempNext.y - tempNext.x * temp.y);
            A += expr;
            Cx += (temp.x + tempNext.x) * expr;
            Cy += (temp.y + tempNext.y) * expr;
        }
        A = A / 2;
        Cx = Cx / A / 6;
        Cy = Cy / A / 6;
        return new Vector2(Cx, Cy);
    }


}
