package GameObjects.Factories;

import Animations.AnimationState;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.Player.Player;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.Actors.Stats.Stats;
//import Animations.ActorAnimations;
import Animations.AnimationConstants;
import GameObjects.BodyFeatures;
import GameObjects.AnimationRendering.AnimationRender;
import GameObjects.AnimationRendering.GIFRender;
import GameObjects.AnimationRendering.GifPair;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static GameObjects.AnimationRendering.GIFS.PLAYER_IDLE_GIF;
import static GameObjects.AnimationRendering.GIFS.PLAYER_MOVING_GIF;
import static Tools.FilterTool.Category.PLAYER;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createCircleShape;

public class PlayerFactory implements IFactory<Player, PlayerType>{
    private TextureHandler textureHandler;
    public PlayerFactory() {
        textureHandler = new GdxTextureHandler();
    }
    @Override
    public Player create(PlayerType type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Player player;
        float scale;
        //ActorMovement animation;
        PlayerStats stats;
        BodyFeatures bodyFeatures;
        Map<AnimationState, GifPair> gifs = new HashMap<>();

        switch (type) {
            case PLAYER1: {
                scale = AnimationConstants.PLAYER_SCALE;
                stats = Stats.player();
                gifs.put(AnimationState.MOVING, PLAYER_MOVING_GIF);
                gifs.put(AnimationState.IDLE, PLAYER_IDLE_GIF);
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        Filter filter = createFilter(
                PLAYER,
                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
        );

        AnimationRender render = new GIFRender<>(gifs);
        Shape shape = createCircleShape(scale*render.getWidth(AnimationState.IDLE)/2);

        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                10,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);


        player = new Player(type,new GIFRender<>(gifs),bodyFeatures,scale,stats);
        player.setAnimationState(AnimationState.IDLE);

        return player;
    }

    @Override
    public List<Player> create(int n, PlayerType type) {
        return null;
    }

    @Override
    public void setTextureHandler(TextureHandler newTextureHandler) {
        textureHandler = newTextureHandler;
    }
}
