package GameObjects.StaticObjects;

import Rendering.Animations.AnimationRendering.AnimationHandler;

import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Actors.Player;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;


public class Terrain extends GameObject<TerrainType> {

    public Terrain(TerrainType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(type,animationHandler, bodyFeatures, scale);
    }

    public boolean outOfBounds(Player player, double deSpawnRadius) {
        float dx = body.getPosition().x - player.getBody().getPosition().x;
        float dy = body.getPosition().y - player.getBody().getPosition().y;
        float dist = (float) Math.sqrt(dx * dx + dy * dy);
        return (dist > deSpawnRadius);
    }
}

