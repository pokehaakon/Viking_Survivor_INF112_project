package GameObjects.Factories;

import GameObjects.Actors.Player.Player;
import GameObjects.Actors.Stats.PlayerStats;
import GameObjects.Actors.Stats.Stats;
import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import FileHandling.FileHandler;
import FileHandling.GdxFileHandler;
import GameObjects.GameObject;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;

public class PlayerFactory implements IFactory<Player>{
    private World world;
    private FileHandler fileHandler;
    public PlayerFactory(World world) {
        this.world = world;
        fileHandler = new GdxFileHandler();
    }
    @Override
    public Player create(String type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Player player;
        float scale;
        Shape shape;
        String spawnGIF;
        Texture texture;
        ActorAnimation animation;
        Body body;
        PlayerStats stats;

        switch (type.toUpperCase()) {
            case "PLAYER1": {
                scale = AnimationConstants.PLAYER_SCALE;
                spawnGIF = AnimationConstants.PLAYER_IDLE_RIGHT;
                texture = fileHandler.loadTexture(spawnGIF);
                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)

                );
                animation = ActorAnimations.playerMoveAnimation();
                body = createBody(
                        world,
                        new Vector2(),
                        shape,
                        createFilter(
                                FilterTool.Category.PLAYER,
                                new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
                        ),
                        1f,
                        0,
                        0
                );
                stats = Stats.player();
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }


        player = new Player(body, scale, stats);

        // setting animations
        player.setAnimation(animation);

        player.setType(type);
        player.setNewAnimationGIF(spawnGIF);
        //enemy.shape = shape;

        return player;
    }

    @Override
    public List<Player> create(int n, String type) {
        return null;
    }

    @Override
    public void setFileHandler(FileHandler newFileHandler) {
        fileHandler = newFileHandler;

    }
}
