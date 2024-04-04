package GameObjects.Factories;

import Animations.ActorAnimation;
import Animations.ActorAnimations;
import Animations.AnimationConstants;
import FileHandling.FileHandler;
import FileHandling.GdxFileHandler;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.Stats.EnemyStats;
import GameObjects.Actors.Stats.Stats;
import GameObjects.Terrain.Terrain;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import java.util.List;

import static Tools.BodyTool.createEnemyBody;
import static Tools.ShapeTools.createSquareShape;

public class TerrainFactory implements IFactory<Terrain>{

    private final World world;
    private FileHandler fileHandler;

    public TerrainFactory(World world) {
        this.world = world;

        //default
        fileHandler = new GdxFileHandler();
    }
    @Override
    public Terrain create(String type) {
        if(type == null) {
            throw new NullPointerException("Type cannot be null!");
        }

        Terrain terrain;
        float scale;
        Texture texture;


//        switch (type.toUpperCase()) {
//            case "TREE": {
//                scale = AnimationConstants.ENEMY1_SCALE;
//                texture = fileHandler.loadTexture(spawnGIF);
//                shape = createSquareShape(
//                        (float)(texture.getWidth())*scale,
//                        (float) (texture.getHeight()*scale)
//
//                );
//                animation = ActorAnimations.enemyMoveAnimation();
//                stats = Stats.enemy1();
//                break;
//            }
//
//            default:
//                throw new IllegalArgumentException("Invalid enemy type");
//        }
//
//        enemy = new Enemy(createEnemyBody(world, new Vector2(),shape),spawnGIF, scale, stats);
//        enemy.setNewAnimationGIF(spawnGIF);
//        // setting animations
//        enemy.setAnimation(animation);
//
//        enemy.setType(type);
//        //enemy.shape = shape;
//
//        return enemy;
        return null;
    }

    @Override
    public List<Terrain> create(int n, String type) {
        return null;
    }

    @Override
    public void setFileHandler(FileHandler newFileHandler) {
        fileHandler =  newFileHandler;

    }
}
