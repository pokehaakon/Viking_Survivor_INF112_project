package GameObjects.Terrain;

import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.TerrainType;
import GameObjects.Actors.Player.Player;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import Rendering.AnimationRender;
import com.badlogic.gdx.graphics.Texture;


public class Terrain extends GameObject<TerrainType> {

    public Terrain(TerrainType type,AnimationRender render, BodyFeatures bodyFeatures, float scale) {
        super(type,render, bodyFeatures, scale);
    }

    public boolean outOfBounds(Player player, double deSpawnRadius) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy = body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return (dist > deSpawnRadius);
    }
}

