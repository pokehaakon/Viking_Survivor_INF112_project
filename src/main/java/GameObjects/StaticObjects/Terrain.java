package GameObjects.StaticObjects;

import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Actors.Player;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;
import GameObjects.Animations.AnimationRendering.AnimationRender;

import java.util.Map;


public class Terrain extends GameObject<TerrainType> {

    public Terrain(TerrainType type, Map<AnimationState,String> animations, BodyFeatures bodyFeatures, float scale) {
        super(type,animations, bodyFeatures, scale);
    }

    public boolean outOfBounds(Player player, double deSpawnRadius) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy = body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return (dist > deSpawnRadius);
    }
}

