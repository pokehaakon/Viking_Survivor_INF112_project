package InputProcessing.Contexts;

import Actors.GifDecoder;
import InputProcessing.Coordinates.Coordinates;
import Actors.Enemy.*;
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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.*;

public class MVPContext extends Context {


    private final SpriteBatch batch;
    private final Camera camera;

    private World world;

    private Player player;

    private ArrayList<Enemy> enemies;
    private final BitmapFont font;

    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;



    private RollingSum UPS;
    private long previousFrameStart = System.nanoTime();

    private final Lock renderLock;


    private KeyStates keyStates;

    private final SimulationThread simThread;
    private  EnemyFactory enemyFactory;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!

    private Box2DDebugRenderer debugRenderer;


    private Set<Body> toBoKilled;


    public MVPContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;

        this.setInputProcessor(createInputProcessor());
        setupDebug();
        font = new BitmapFont();
        font.setColor(Color.RED);
        renderLock = new ReentrantLock(true);

        //create and start simulation
        createWorld();

        // spawns start enemies
        spawnRandomEnemies(10);
        spawnSwarm("Enemy1", SwarmType.LINE, 10, 60);
        spawnSwarm("Enemy2", SwarmType.SQUARE, 10,60);

        toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        world.setContactListener(contactListener);
        simThread = new SimulationThread(this);

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

        debugRenderer.render(world, camera.combined);

        Vector2 origin;
        origin = player.getBody().getPosition().cpy();
        origin.add(getBottomLeftCorrection(player.getBody().getFixtureList().get(0).getShape()));

        //center camera at player
        camera.position.x = origin.x;
        camera.position.y = origin.y;
        camera.position.z = 0;
        camera.update(true);


        batch.begin();

        // draw enemies
        for (Enemy e : enemies) {
            e.draw(batch, Gdx.graphics.getDeltaTime());
        }

        //draw player
        player.draw(batch, Gdx.graphics.getDeltaTime());

        batch.end();


        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);
        frameCount++;
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

    }

    private void createWorld() {
        // sets up world

        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        enemyFactory = new EnemyFactory(world);
        initializePlayer();
        enemies = new ArrayList<>();

        world.step(1/60f, 10, 10);
    }

    private void spawnEnemies(String enemyType, int num) {
        // random start coordinates
        List<Vector2> startPoints = Coordinates.randomPoints(num, player.getBody().getPosition());

        enemies.addAll(enemyFactory.createEnemies(num, enemyType, startPoints));
    }

    private void spawnRandomEnemies(int num) {
        // random start coordinates
        List<Vector2> startPoints = Coordinates.randomPoints(num, player.getBody().getPosition());

        enemies.addAll(enemyFactory.createRandomEnemies(num, startPoints));
    }

    private void spawnSwarm(String enemyType, SwarmType swarmType, int size, int spacing) {
        List<Vector2> startPoints;
        Vector2 randomCenter = Coordinates.randomPoint(player.getBody().getPosition());

        // swarm velocity vector is the player's initial position
        Vector2 target = player.getBody().getPosition();

        if(swarmType == SwarmType.LINE) {
            startPoints = Coordinates.lineSwarm(size, spacing,randomCenter,target);
        }
        else {
            startPoints = Coordinates.squareSwarm(size, randomCenter,spacing);
        }

        enemies.addAll(enemyFactory.createSwarm(size, enemyType, swarmType, startPoints));

    }

    private void initializePlayer() {
        // player sprite
        Animation<TextureRegion> playerSprite = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(Sprites.PLAYER_PNG).read());

        // player hitbox
        PolygonShape squarePlayer = createSquareShape(
                playerSprite.getKeyFrame(0).getRegionWidth()*Sprites.PLAYER_SCALE,
                playerSprite.getKeyFrame(0).getRegionHeight()*Sprites.PLAYER_SCALE
        );

        // player body
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

        player = new Player(playerBody, playerSprite, Sprites.PLAYER_SCALE, Stats.player());


        squarePlayer.dispose();
    }


    private void movePlayer() {
        // moves player according to input
        player.resetVelocity();
        if (keyStates.getState(KeyStates.GameKey.UP)) {
            player.setVelocityVector(0,1);
        }
        if (keyStates.getState(KeyStates.GameKey.DOWN)) {
            player.setVelocityVector(0,-1);
        }
        if (keyStates.getState(KeyStates.GameKey.LEFT)) {
            player.setVelocityVector(-1,0);
        }
        if (keyStates.getState(KeyStates.GameKey.RIGHT)) {
            player.setVelocityVector(1,0);
        }
        player.move();
    }
    private void updatePlayer() {
        movePlayer();
    }

    private void updateEnemies() {
        for(Enemy enemy:enemies) {
            if(enemy.getEnemyState() ==  EnemyState.SOLO) {
                enemy.chase(player);
            }
            // if swarm
            else {
                enemy.swarmStrike(player);
            }

        }

    }

    /**
     * updates the in-game actors. this could be attack, movement etc
     */
    public void updateActors() {
        updatePlayer();
        updateEnemies();

    }


    public Lock getRenderLock() {
        return renderLock;
    }
    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }
    public RollingSum getUPS() {
        return UPS;
    }
    public KeyStates getKeyStates() {
        return keyStates;
    }

    public World getWorld() {
        return world;
    }

    public RollingSum getUpdateTime() {
        return UpdateTime;
    }

    public Set<Body> getToBoKilled() {
        return toBoKilled;
    }



}
