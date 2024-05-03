//package Rendering;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//
//public class Shapes {
//    /**
//     * Creates a new rectangular texture with the given attributes
//     * The padding does not change the final width or height of the rectangle
//     * @param width the width of the rectangle
//     * @param height the height of the rectangle
//     * @param topPadding padding on the top of the rectangle
//     * @param rightPadding padding on the right of the rectangle
//     * @param bottomPadding padding on the bottom of the rectangle
//     * @param leftPadding padding on the left of the rectangle
//     * @param paddingColor the color of the padding (border)
//     * @param fillColor the color of the center of the rectangle
//     * @return Texture of the specified rectangle
//     */
//    public static Texture makeRectangle(int width, int height, int topPadding, int rightPadding, int bottomPadding, int leftPadding, Color paddingColor, Color fillColor) {
//        Pixmap rectanglePixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
//        rectanglePixmap.setBlending(Pixmap.Blending.None);
//        rectanglePixmap.setColor(paddingColor);
//        rectanglePixmap.fill();
//        rectanglePixmap.setColor(fillColor);
//        int x0 = leftPadding;
//        int y0 = topPadding;
//        int dx = width - leftPadding - rightPadding;
//        int dy = height - topPadding - bottomPadding;
//        rectanglePixmap.fillRectangle(x0, y0, dx, dy);
//        Texture t = new Texture(rectanglePixmap);
//        rectanglePixmap.dispose();
//
//        return t;
//    }
//
//    /**
//     * Creates a circle Texture with the given attributes
//     * @param radius
//     * @param padding
//     * @param paddingColor
//     * @param fillColor
//     * @return
//     */
//    public static Texture makeCircle(int radius, int padding, Color paddingColor, Color fillColor) {
//        System.out.println(radius);
//
//        Pixmap circlePixmap = new Pixmap(radius, radius, Pixmap.Format.RGBA8888);
//        circlePixmap.setBlending(Pixmap.Blending.None);
//
//        circlePixmap.setColor(paddingColor);
//        circlePixmap.fill();
//        circlePixmap.setColor(fillColor);
//
//        circlePixmap.fillCircle(radius/2, radius/2,radius - 2 * padding);
//        Texture t = new Texture(circlePixmap);
//        circlePixmap.dispose();
//
//        return t;
//    }
//}
