package Simulation;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Body;

/**
 * Interface for all game objects
 * (all objects rendered on the screen in the game)
 */
public interface IGameObject {
    void draw(SpriteBatch batch);
    Body getBody();
    boolean isDestroyed();
}
