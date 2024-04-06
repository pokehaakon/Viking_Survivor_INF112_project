package GameObjects.Terrain;

import GameObjects.Actors.Actor;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;


public class Terrain extends GameObject {

    public Terrain(String spritePath, BodyFeatures bodyFeatures, float scale) {
        super(spritePath, bodyFeatures,scale);
    }

    public Terrain() {

    }
    public boolean outOfBounds(Actor player, double deSpawnRadius) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy =  body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx*dx + dy*dy);
        return(dist > deSpawnRadius);
    }
}
