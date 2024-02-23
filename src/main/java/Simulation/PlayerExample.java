package Simulation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;

import java.util.IllegalFormatWidthException;

public class PlayerExample extends Actor {
    private Texture spriteImage;
    public Rectangle spriteRect;

    public PlayerExample(String name) {
        super(name);
        spriteImage = new Texture(Gdx.files.internal("obligator.png"));
        spriteRect = new Rectangle();
        spriteRect.x = 100;
        spriteRect.y = 100;
        spriteRect.width = spriteImage.getWidth();
        spriteRect.height = spriteImage.getHeight();
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
    }

    @Override
    public Body getBody() {
        return null;
    }
}
