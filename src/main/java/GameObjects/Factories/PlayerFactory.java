package GameObjects.Factories;

import GameObjects.Animations.AnimationRendering.AnimationHandler;
import GameObjects.Animations.AnimationRendering.AnimationType;
import GameObjects.Animations.AnimationState;
import GameObjects.ObjectTypes.PlayerType;
import GameObjects.Actors.Player;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.Actors.Stats.Stats;
//import Animations.ActorAnimations;
import GameObjects.BodyFeatures;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static GameObjects.Animations.AnimationRendering.GIFS.*;
import static Tools.FilterTool.Category.PLAYER;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PlayerFactory extends AbstractFactory<Player,PlayerType>{

    public PlayerFactory() {
    }
    @Override
    public Player create(PlayerType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Player player;
        float scale;
        PlayerStats stats;
        BodyFeatures bodyFeatures;
        Map<AnimationState, String> animations = new EnumMap<>(AnimationState.class);
        AnimationType animationType;
        AnimationState spawnState;
        switch (type) {
            case PLAYER1: {
                scale = PLAYER_SCALE;
                stats = Stats.player();
                animations.put(AnimationState.MOVING, PlAYER_MOVING_FILE_PATH);
                animations.put(AnimationState.IDLE, PlAYER_IDLE_FILE_PATH);
                animationType = AnimationType.GIF;
                spawnState = AnimationState.IDLE;
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid player type");
        }

        Filter filter = createFilter(
                PLAYER,
                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL, FilterTool.Category.PICKUP}
        );

        Shape shape = createCircleShape(0.3f*scale*PLAYER_WIDTH/2);

        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                10,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);


        player = new Player(type,new AnimationHandler(animations,animationType,spawnState),bodyFeatures,scale,stats);


        return player;
    }

    @Override
    public List<Player> create(int n, PlayerType type) {
        return null;
    }


}
