package GameObjects.StaticObjects;

import GameObjects.Animations.AnimationRendering.AnimationHandler;

import GameObjects.ObjectTypes.TerrainType;
import GameObjects.Actors.Player;
import GameObjects.BodyFeatures;
import GameObjects.GameObject;


public class Terrain extends GameObject<TerrainType> {

    public Terrain(TerrainType type, AnimationHandler animationHandler, BodyFeatures bodyFeatures, float scale) {
        super(type,animationHandler, bodyFeatures, scale);
    }

}

