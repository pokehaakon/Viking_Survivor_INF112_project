package Tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static Tools.ShapeTools.*;
import static org.junit.jupiter.api.Assertions.*;

public class ShapeToolsTest {
    @BeforeAll
    static void initBox2d() {
        Box2D.init();
    }



    @Test
    void massCenterOffsetWhereExpected() {
        float radius = 1f, width = 10f, height = 30f;
        CircleShape circle = createCircleShape(radius);
        PolygonShape square = createSquareShape(width, height);
        
        Vector2 expectedCircle = new Vector2(-radius, -radius);
        Vector2 actualCircle = getBottomLeftCorrection(circle);
        assertEquals(expectedCircle.x, actualCircle.x);
        assertEquals(expectedCircle.y, actualCircle.y);

        Vector2 expectedSquare = new Vector2(-width/2, -height/2);
        Vector2 actualSquare = getBottomLeftCorrection(square);
        assertEquals(expectedSquare.x, actualSquare.x);
        assertEquals(expectedSquare.y, actualSquare.y);

        circle.dispose();
        square.dispose();
    }

    @Test
    void createSquareShapeTest() {
        float width = 10f, height = 20f;
        PolygonShape square = createSquareShape(width, height);
        assertEquals(4, square.getVertexCount());
        Vector2 v = new Vector2();


        //check that corners are where expected
        square.getVertex(3, v);
        assertEquals(0f, v.x);
        assertEquals(0f, v.y);

        square.getVertex(0, v);
        assertEquals(width, v.x);
        assertEquals(0f, v.y);

        square.getVertex(1, v);
        assertEquals(width, v.x);
        assertEquals(height, v.y);

        square.getVertex(2, v);
        assertEquals(0f, v.x);
        assertEquals(height, v.y);

        //the answer from square.getRadius() does not make sense!
        //assertEquals(Math.sqrt(width*width + height*height) / 2, square.getRadius());

        square.dispose();
    }

    @Test
    void createCircleShapeTest() {
        float radius = 10f;
        CircleShape circle = createCircleShape(radius);

        assertEquals(radius, circle.getRadius());

        circle.dispose();
    }

    @Test
    void getBottomLeftCorrectionTest() {
    }
}
