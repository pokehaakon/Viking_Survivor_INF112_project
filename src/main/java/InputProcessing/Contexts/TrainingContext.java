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

    //    private PlayerExample player;



    private RollingSum UpdateTime, FrameTime, FPS, UPS;
    private long previousFrameStart = System.nanoTime();

    private KeyStates keyStates;

    private  EnemyFactory enemyFactory;
    private float zoomLevel = 1f;
    Player player;
    Player ground;



    private World world;
    private SpriteBatch batch;
    private Camera camera;

    private Box2DDebugRenderer debugRenderer;

    private static final int meterToPixels = 32;
    public TrainingContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.batch = batch;
        this.camera = camera;
        world = new World(new Vector2(0, -20), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(500,500);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(30);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.75f;

        Body playerBody = world.createBody(ballDef);
        Fixture playerFixture = playerBody.createFixture(fixtureDef);
        player = new Player(playerBody,new Texture(Gdx.files.internal("obligator.png")),0.5f);
        ballShape.dispose();

        // ground

        BodyDef groundDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(500,0);

        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[] {new Vector2(-500,200), new Vector2(3000,200)});

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.density = 1f;
        groundFixture.friction = 0.5f;
        groundFixture.restitution = 0;
        groundFixture.shape = groundShape;
        Body groundBody = world.createBody(groundDef);
        Fixture groundFix = groundBody.createFixture(groundFixture);
        ground = new Player(groundBody,new Texture(Gdx.files.internal("obligator.png")),0.5f);
        groundShape.dispose();

        // box

        BodyDef boxDef = new BodyDef();
        groundDef.type = BodyDef.BodyType.StaticBody;
        groundDef.position.set(500,0);

        PolygonShape boxShape = new PolygonShape();
        groundShape.createChain(new Vector2[] {new Vector2(-500,200), new Vector2(3000,200)});

        FixtureDef groundFixture = new FixtureDef();
        groundFixture.density = 1f;
        groundFixture.friction = 0.5f;
        groundFixture.restitution = 0;
        groundFixture.shape = groundShape;
        Body groundBody = world.createBody(groundDef);
        Fixture groundFix = groundBody.createFixture(groundFixture);
        ground = new Player(groundBody,new Texture(Gdx.files.internal("obligator.png")),0.5f);
        groundShape.dispose();




    }


    private void setupInputListener() {
        keyStates = new KeyStates(); //this should load some config!

        this.addAction(Input.Keys.W, ContextualInputProcessor.KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.A, ContextualInputProcessor.KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.S, ContextualInputProcessor.KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.D, ContextualInputProcessor.KeyEvent.KEYDOWN, keyStates::setInputKey);
        //this.addAction(Input.Keys.ESCAPE, KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.ESCAPE, ContextualInputProcessor.KeyEvent.KEYDOWN, (x) -> {keyStates.setInputKey(Input.Keys.ESCAPE);System.exit(0);});

        this.addAction(Input.Keys.P, ContextualInputProcessor.KeyEvent.KEYDOWN, (x) -> this.getInputProcessor().setContext("EXAMPLE"));

        this.addAction(Input.Keys.W, ContextualInputProcessor.KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.A, ContextualInputProcessor.KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.S, ContextualInputProcessor.KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.D, ContextualInputProcessor.KeyEvent.KEYUP, keyStates::unsetInputKey);

        this.addAction(Input.Buttons.LEFT, ContextualInputProcessor.MouseEvent.MOUSE_CLICKED, (x, y) -> System.out.println("CLICKED -> " + x + ", " + y));
        this.addAction(Input.Buttons.LEFT, ContextualInputProcessor.MouseEvent.MOUSE_UNCLICKED, (x, y) -> System.out.println("DROPPED -> " + x + ", " + y));
        this.addAction(0, ContextualInputProcessor.MouseEvent.MOUSE_DRAGGED, (x, y) -> System.out.println("DRAGGED -> " + x + ", " + y));
        this.addAction(0, ContextualInputProcessor.MouseEvent.MOUSE_MOVED, (x, y) -> System.out.println("MOVED -> " + x + ", " + y));

        this.addAction(Input.Buttons.MIDDLE, ContextualInputProcessor.MouseEvent.MOUSE_CLICKED, (x, y) -> {
            zoomLevel = 1f;
            camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
            camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;
        });
        this.addAction(0, ContextualInputProcessor.MouseEvent.MOUSE_SCROLLED, (x, y) -> {
            if (y > 0 && zoomLevel < 2) {
                zoomLevel *= 1.25f;
            }
            if (y < 0 && zoomLevel > 0.01f) {
                zoomLevel /= 1.25f;
            }
            camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
            camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;
        });

    }


    @Override
    public void show() {
        camera = new OrthographicCamera();
        world = new World(new Vector2(0, -20), true);
        debugRenderer = new Box2DDebugRenderer();

        BodyDef ballDef = new BodyDef();
        ballDef.type = BodyDef.BodyType.DynamicBody;
        ballDef.position.set(500,500);

        CircleShape ballShape = new CircleShape();
        ballShape.setRadius(100f);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = ballShape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0;
        fixtureDef.restitution = 0.75f;

        Body playerBody = world.createBody(ballDef);
        Fixture playerFixture = playerBody.createFixture(fixtureDef);
        player = new Player(playerBody,new Texture(Gdx.files.internal("obligator.png")),0.5f);
        ballShape.dispose();



    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(Color.WHITE);
        debugRenderer.render(world, camera.combined);
//        batch.begin();
//        player.draw(batch);
//        ground.draw(batch);
//        batch.end();
        world.step(1/60f, 10, 10);
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
