package InputProcessing.Contexts;

import Actors.Enemy.EnemyFactory;
import Actors.Enemy.EnemyTypes.Enemy;
import Actors.Player.Player;
import Actors.Stats;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
import Simulation.SimulationThread;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Rendering.Shapes.makeRectangle;
import static Tools.ShapeTools.*;
import static java.lang.Math.random;

public class MVPContext extends Context {
    private final SpriteBatch batch;
    private final Camera camera;
    private final World world;
//    private PlayerExample player;
    private Player player;
    //private Array<Enemy> enemies;
    private Array<Enemy> enemies;
    private final BitmapFont font;
    final private Texture spriteImage, rectSprite;
    final private Rectangle spriteRect, spriteRectEnemy;
    final private ShapeRenderer shape;
    private RollingSum UpdateTime, FrameTime, FPS, UPS;
    private long previousFrameStart = System.nanoTime();
    private final Lock renderLock;
    private KeyStates keyStates;
    private final SimulationThread simThread;
    private  EnemyFactory enemyFactory;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!


    public MVPContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;

        setupInputListener();
        setupDebug();

        enemies = new Array<>();

        float enemyScale = 0.3f;
        spriteImage = new Texture(Gdx.files.internal("obligator.png"));



        spriteRect = new Rectangle(0f, 0f, spriteImage.getWidth(), spriteImage.getHeight());
        spriteRectEnemy = new Rectangle(1, 1, spriteImage.getWidth() / 2f * enemyScale, spriteImage.getHeight() / 2f * enemyScale);

        rectSprite = makeRectangle(spriteImage.getWidth() / 2, spriteImage.getHeight() / 2, 2,2,2,2, Color.GREEN, Color.CLEAR);

        font = new BitmapFont();
        font.setColor(Color.RED);

        this.shape = new ShapeRenderer();



        renderLock = new ReentrantLock(true);

        //create and start simulation
        world = createWorld();
        Set<Body> toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        world.setContactListener(contactListener);
        simThread = new SimulationThread(renderLock, keyStates, world, toBoKilled, UpdateTime, UPS, player.getBody());
        simThread.start();

    }
    private void setupDebug() {
        UpdateTime = new RollingSum(60*3);
        FrameTime = new RollingSum(60*3);
        FPS = new RollingSum(60 * 3);
        UPS = new RollingSum(60 * 3);
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

    }

    @Override
    public void render(float delta) {
        FPS.add(System.nanoTime() - previousFrameStart);
        previousFrameStart = System.nanoTime();

        renderLock.lock();
        long renderStartTime = System.nanoTime();
        ScreenUtils.clear(Color.WHITE);

        float radius = spriteRectEnemy.getWidth() / 2;


        Vector2 origin;
        //origin = player.getWorldCenter().cpy();
        origin = player.getBody().getPosition().cpy();
        origin.add(getBottomLeftCorrection(player.getBody().getFixtureList().get(0).getShape()));

        //origin = player.getPosition().cpy();
        //center camera at player
        camera.position.x = origin.x;
        camera.position.y = origin.y;
        camera.position.z = 0;
        camera.update(true);



        batch.begin();
        Array<Enemy> tempEnemies = new Array<>();
        for (Enemy e : enemies) {
            if (!e.getBody().isActive()) {continue;}
            tempEnemies.add(e);
            e.draw(batch);
        }
        enemies = tempEnemies;
        Array<Vector2> enemiesCenter = new Array<>(enemies.size);
        //drawEnemies(enemiesCenter);

        //draw player sprite
        player.draw(batch);


        //for some reason the sprite batch renders "last" frame...



        batch.end();


        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);
        frameCount++;
    }
    private void drawEnemies(Array<Vector2> enemiesCenter) {
        for (Enemy e : enemies) {
            if (e.isDestroyed()) {continue;}
            e.draw(batch);
        }

    }



    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        renderLock.lock();
        simThread.pause();
        renderLock.unlock();
    }

    @Override
    public void resume() {
        simThread.unpause();
        synchronized (simThread) {
            simThread.notify();
        }
    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        spriteImage.dispose();
    }

    private World createWorld() {
        Box2D.init();
        World world = new World(new Vector2(0, 0), true);
        enemyFactory = new EnemyFactory(world);


        BodyDef playerBodyDef = new BodyDef();
        //playerBodyDef.type = BodyDef.BodyType.KinematicBody; //we are not able to get the mass center of a Kinematic Body :(
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(20, 20);
        playerBodyDef.fixedRotation = true;

        Body playerBody = world.createBody(playerBodyDef);

        PolygonShape squarePlayer = squarePlayer = createSquareShape(
                spriteRect.getWidth(),
                spriteRect.getHeight()
        );

        FixtureDef fixtureDefPlayer = new FixtureDef();
        fixtureDefPlayer.shape = squarePlayer;
        fixtureDefPlayer.density = 1f;
        fixtureDefPlayer.friction = 0;
        fixtureDefPlayer.restitution = 0;
        fixtureDefPlayer.isSensor = false;

        Fixture fixturePlayer = playerBody.createFixture(fixtureDefPlayer);
        player = new Player(playerBody, spriteImage, 1);




        enemies = new Array<>();
        //enemies.add(enemyFactory.createEnemyType("ENEMY1", 50,  50, 1));
        for(Enemy e : enemyFactory.createRandomEnemies(10))
            enemies.add(e);

        squarePlayer.dispose();

        world.step(1/60f, 10, 10);

        return world;
    }
}
