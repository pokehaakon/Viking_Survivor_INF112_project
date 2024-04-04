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
import com.badlogic.gdx.physics.box2d.*;

import java.util.ArrayList;
import java.util.List;

import static Tools.BodyTool.*;
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
        Body body;
        Shape shape;


        switch (type.toUpperCase()) {
            case "TREE": {
                texture = fileHandler.loadTexture("img_2.png");

                scale = 0.6f;

                shape = createSquareShape(
                        (float)(texture.getWidth())*scale,
                        (float) (texture.getHeight()*scale)
                );
                body = createTerrainBody(world, new Vector2(),shape);
                break;
            }

            default:
                throw new IllegalArgumentException("Invalid enemy type");
        }

        terrain = new Terrain(body,scale);

        terrain.setType(type);
        terrain.setSprite(texture);

        return terrain;
    }

    @Override
    public List<Terrain> create(int n, String type) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of enemies must be greater than zero");
        }

        List<Terrain> terrain = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            terrain.add(create(type));
        }
        return terrain;
    }

    @Override
    public void setFileHandler(FileHandler newFileHandler) {
        fileHandler =  newFileHandler;

    }
}
