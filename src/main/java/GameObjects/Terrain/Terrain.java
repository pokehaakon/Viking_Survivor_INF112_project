package GameObjects.Terrain;

import GameObjects.Actors.Actor;
import GameObjects.Actors.IGameObject;
import GameObjects.GameObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

import static InputProcessing.Coordinates.RandomCoordinates.DESPAWN_RADIUS;

public class Terrain extends GameObject {

    public Terrain(Body body, float scale) {
        super(body, scale);
    }

    public boolean outOfBounds(Actor player) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy =  body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return(dist > DESPAWN_RADIUS);
    }
}
