package Rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Shapes {
    public static Texture makeRectangle(int width, int height, int topPadding, int rightPadding, int bottomPadding, int leftPadding, Color paddingColor, Color fillColor) {
        Pixmap rectanglePixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        rectanglePixmap.setBlending(Pixmap.Blending.None);
        rectanglePixmap.setColor(paddingColor);
        rectanglePixmap.fill();
        rectanglePixmap.setColor(fillColor);
        int x0 = leftPadding;
        int y0 = topPadding;
        int dx = width - leftPadding - rightPadding;
        int dy = height - topPadding - bottomPadding;
        rectanglePixmap.fillRectangle(x0, y0, dx, dy);
        Texture t = new Texture(rectanglePixmap);
        rectanglePixmap.dispose();

        return t;
    }
}
