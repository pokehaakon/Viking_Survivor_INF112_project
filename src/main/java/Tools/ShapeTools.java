package Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import static java.lang.Math.min;


/**
 * Contains functions for creating box2d shapes
 * REMEMBER to call shape.dispose() after using the shape!!!
 */
public abstract class ShapeTools {

    /**
     * Create a square shape with the given width and height.
     * Remember to call '.dispose()' when finished with the shape.
     * @param width the width of the square
     * @param height the height of the square
     * @return a polygon shape with the given parameters
     */
    public static PolygonShape createSquareShape(float width, float height) {
        PolygonShape shape = new PolygonShape();
        shape.set(new float[] {0,0, width,0, width,height, 0,height});
        return shape;
    }

    /**
     * Create a circle shape with the given radius.
     * Remember to call '.dispose()' when finished with the shape.
     * @param radius radius of the circle
     * @return a circle shape with the given parameters
     */
    public static CircleShape createCircleShape(float radius) {
        CircleShape shape = new CircleShape();
        shape.setRadius(radius * 2);
        return shape;
    }

    /**
     * Get the vector pointing from the massCenter (center) of a circle to the bottomLeft of its circumscribed square.
     * @param c the circle shape
     * @return correction vector
     */
    private static Vector2 getBottomLeftCorrection(CircleShape c) {
        return new Vector2(-c.getRadius(), -c.getRadius());
    }

    /**
     * Get the vector pointing from the massCenter of a polygon to the bottomLeft of its circumscribed square.
     * @param p the polygon shape
     * @return correction vector
     */
    private static Vector2 getBottomLeftCorrection(PolygonShape p) {
        Vector2 massCenter = getMassCenter(p);

        float minX = Float.POSITIVE_INFINITY, minY = Float.POSITIVE_INFINITY;
        Vector2 temp = new Vector2();
        for(int i = 0; i < p.getVertexCount(); i++) {
            p.getVertex(i, temp);
            minX = Math.min(minX, temp.x);
            minY = Math.min(minY, temp.y);
        }
        return new Vector2(minX - massCenter.x, minY- massCenter.y);
    }

    /**
     * Get the vector pointing from the massCenter of a shape to the bottomLeft of its circumscribed square.
     * @param s the shape
     * @return the correction vector
     */
    public static Vector2 getBottomLeftCorrection(Shape s) {
        if (s.getType() == Shape.Type.Circle) {
            return getBottomLeftCorrection((CircleShape) s);
        }
        if (s.getType() == Shape.Type.Polygon) {
            return getBottomLeftCorrection((PolygonShape) s);
        }
        throw new RuntimeException("Not implemented for this shape: " + s.getType() + " : " + s);
    }

    /**
     * Get the mass center (centroid) of a polygon shape
     * @param p the shape
     * @return the vector pointing to the mass center of the shape
     */
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
