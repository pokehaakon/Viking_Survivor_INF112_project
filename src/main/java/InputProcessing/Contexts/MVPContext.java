package InputProcessing.Contexts;

import Actors.Actor;
import Actors.ActorAction;
import Actors.Enemy.EnemyFactory;
import Actors.Enemy.Enemy;
import Actors.Player.Player;

import Actors.Stats.Stats;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
import Simulation.SimulationThread;
import Tools.FilterTool;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
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

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Rendering.Shapes.makeRectangle;
import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.*;

public class MVPContext extends Context {
    private static final float PLAYER_SCALE = 0.5f;
    private final SpriteBatch batch;
    private final Camera camera;
    private World world;
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

    private ActorAction playerAction;
    private ActorAction enemyAction;



    public MVPContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;

        this.setInputProcessor(createInputProcessor());
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
        createWorld();
        Set<Body> toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        world.setContactListener(contactListener);
        simThread = new SimulationThread(renderLock, keyStates, world, toBoKilled, UpdateTime, UPS, player, enemies);
        simThread.start();

    }
    private void setupDebug() {
        UpdateTime = new RollingSum(60*3);
        FrameTime = new RollingSum(60*3);
        FPS = new RollingSum(60 * 3);
        UPS = new RollingSum(60 * 3);
    }


    private InputProcessor createInputProcessor() {
        Context me = this;
        keyStates = new KeyStates();
        return new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                return switch (keycode) {
                    case Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D -> keyStates.setInputKey(keycode);
                    case Input.Keys.ESCAPE -> {keyStates.setInputKey(keycode); System.exit(0); yield true;}
                    case Input.Keys.P -> {me.getContextualInputProcessor().setContext("EXAMPLE"); yield true;}
                    default -> false;
                };
            }

            @Override
            public boolean keyUp(int keycode) {
                return switch (keycode) {
                    case Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D -> keyStates.unsetInputKey(keycode);
                    default -> false;
                };
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                //ignore pointer for now...
                return switch (button) {
                    case Input.Buttons.MIDDLE -> {
                        zoomLevel = 1f;
                        camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
                        camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;
                        yield true;
                    }
                    default -> false;
                };

            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(float amountX, float amountY) {
                if (amountY > 0 && zoomLevel < 2) {
                    zoomLevel *= 1.25f;
                }
                if (amountY < 0 && zoomLevel > 0.01f) {
                    zoomLevel /= 1.25f;
                }
                camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
                camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;

                return true;
            }
        };
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
        enemies.clear();
        enemies.addAll(tempEnemies);
        //enemies = tempEnemies;
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

    private void createWorld() {
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        enemyFactory = new EnemyFactory(world);
        setPlayerAction();
        setEnemyAction();
        initializePlayer();

        enemies = new Array<>();

        for(Enemy e : enemyFactory.createRandomEnemies(10)) {
            enemies.add(e);
            e.setAction(enemyAction);
        }


        world.step(1/60f, 10, 10);

        //return world;
    }

    private void initializePlayer() {
        PolygonShape squarePlayer = squarePlayer = createSquareShape(
                spriteRect.getWidth(),
                spriteRect.getHeight()
        );

        Body playerBody = createBody(
                world,
                new Vector2(),
                squarePlayer,
                createFilter(
                        FilterTool.Category.PLAYER,
                        new FilterTool.Category[]{FilterTool.Category.ENEMY, FilterTool.Category.WALL}
                ),
                1f,
                0,
                0
        );
        player = new Player(playerBody, spriteImage, PLAYER_SCALE, Stats.player());
        player.setAction(playerAction);


        squarePlayer.dispose();
    }

    private void setEnemyAction() {

        enemyAction = (e) -> {
            //se.resetVelocity();
            e.chase(player);
        };
    }

    private void setPlayerAction() {
        playerAction = (p) -> {
            player.resetVelocity();
            if (keyStates.getState(KeyStates.GameKey.UP)) {
                p.setVelocityVector(0,1);
            }
            if (keyStates.getState(KeyStates.GameKey.DOWN)) {
                p.setVelocityVector(0,-1);
            }
            if (keyStates.getState(KeyStates.GameKey.LEFT)) {
                p.setVelocityVector(-1,0);
            }
            if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
                p.setVelocityVector(1,0);
            }
            p.move();
        };
    }
}
