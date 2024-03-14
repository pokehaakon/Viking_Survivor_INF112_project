package InputProcessing.Contexts;

import Actors.Enemy.Enemy;
import Actors.Enemy.EnemyFactory;
import Actors.Player.Player;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
import Simulation.SimulationThread;
import Tools.RollingSum;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Rendering.Shapes.makeRectangle;
import static Tools.ShapeTools.createSquareShape;
import static Tools.ShapeTools.getBottomLeftCorrection;

public class TrainingContext extends Context {
    Player player;

    private World world;
    private SpriteBatch batch;
    private Camera camera;

    private Box2DDebugRenderer debugRenderer;

    private static final int meterToPixels = 32;
    public TrainingContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        this.camera = camera;
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(500,500);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(0.8f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0;

        Body playerBody = world.createBody(ballDef);
        Fixture playerFixture = playerBody.createFixture(fixtureDef);
        player = new Player(playerBody,new Texture(Gdx.files.internal("obligator.png")),1);
        ballShape.dispose();
        world.step(1/60f, 10, 10);


    }


    @Override
    public void show() {


    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.BLACK);
        batch.begin();
        player.draw(batch);
        batch.end();
    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();

    }

    @Override
    public void dispose() {
        world.dispose();


    }
}
