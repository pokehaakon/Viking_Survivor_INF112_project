package GameObjects.Factories;

import Animations.MovementState;
import GameObjects.Actors.ObjectTypes.PlayerType;
import GameObjects.Actors.Player.Player;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.Actors.Stats.Stats;
import Animations.ActorMovement;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import GameObjects.BodyFeatures;
import TextureHandling.GdxTextureHandler;
import TextureHandling.TextureHandler;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;

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
        Shape shape;
        String spawnGIF;
        Texture texture;
        ActorMovement animation;
        PlayerStats stats;
        BodyFeatures bodyFeatures;
        Map<MovementState,String> gifs = new HashMap<>();

        switch (type) {
            case PLAYER1: {
                scale = AnimationConstants.PLAYER_SCALE;
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = textureHandler.loadTexture(spawnGIF);
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)

                );
                animation = ActorAnimations.playerMovement();

                stats = Stats.player();
                gifs.put(MovementState.WALKING, AnimationConstants.PLAYER_RIGHT);
                gifs.put(MovementState.IDLE, AnimationConstants.PLAYER_IDLE_RIGHT);

                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        Filter filter = createFilter(
                FilterTool.Category.PLAYER,
                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
        );

        bodyFeatures = new BodyFeatures(
                shape,
                filter,
                10,
                0,
                0,
                false,
                BodyDef.BodyType.DynamicBody);


        player = new Player(textureHandler);
        player.setStats(stats);
        player.setScale(scale);
        player.setBodyFeatures(bodyFeatures);
        player.setSprite(texture);
        player.setAnimation(animation);
        player.setType(type);
        player.setAnimations(gifs);

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
