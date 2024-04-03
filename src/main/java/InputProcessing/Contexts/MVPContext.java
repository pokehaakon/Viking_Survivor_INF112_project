package InputProcessing.Contexts;

import Actors.ActorAction.ActorAction;
import Actors.Enemy.Enemy;
import Actors.Enemy.EnemyPool;
import Actors.Enemy.SwarmType;
import Animations.ActorAnimations;
import Actors.ActorAction.PlayerActions;
import Actors.Enemy.EnemyFactory;
import Animations.AnimationConstants;
import Actors.Player.Player;
import Actors.Stats.Stats;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.Coordinates.RandomCoordinates;
import InputProcessing.Coordinates.SwarmCoordinates;
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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;
import static Tools.ShapeTools.getBottomLeftCorrection;
import static Actors.ActorAction.EnemyActions.*;

public class MVPContext extends Context {


    private final SpriteBatch batch;
    private final Camera camera;

    private World world;

    private Player player;

    private ArrayList<Enemy> spawnedEnemies;
    private final BitmapFont font;

    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;

    private EnemyPool enemyPool;

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


    float elapsedTime;
    private Set<Body> toBoKilled;

    private long lastSpawnTime;






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
        simThread = new SimulationThread(this);
        simThread.start();

        //spawnRandomEnemies(1, EnemyActions.chasePlayer(player));
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
        ScreenUtils.clear(Color.GREEN);

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

        elapsedTime += Gdx.graphics.getDeltaTime();
        for (Enemy enemy : spawnedEnemies) {
            enemy.doAnimation();
            enemy.draw(batch, elapsedTime);
        }
        //draw player
        player.doAnimation();
        player.draw(batch,elapsedTime);



        batch.end();


        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);
        frameCount++;

        if(TimeUtils.millis() - lastSpawnTime > 5000) {
            //spawnRandomEnemies(5, Arrays.asList(chasePlayer(player), destroyIfDefeated(player)));
            spawnSwarm("ENEMY1",SwarmType.SQUARE, 12,60);
        }

    }

    /**
     * Despawns destroyed enemies by returning them to enemy pool and removing them from the spawned enemy list
     */
    public void removeDestroyedEnemies() {
        for(Iterator<Enemy> iter = spawnedEnemies.iterator(); iter.hasNext();) {
            Enemy enemy = iter.next();
            if(enemy.isDestroyed()) {
                // returns enemy to enemy pool
                enemyPool.returnEnemy(enemy);
                // removes from list of active enemies
                iter.remove();
                //System.out.println("destroy!");
            }
        }
    }

    private void updateActorAnimations() {
        player.doAnimation();
        for(Enemy enemy: spawnedEnemies) {
            enemy.doAnimation();
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

    }

    private void createWorld() {
        // sets up world

        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);
        initializePlayer();
        spawnedEnemies = new ArrayList<>();
        enemyPool = new EnemyPool(world, 100);


        toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        world.setContactListener(contactListener);

        world.step(1/60f, 10, 10);
    }

    private void spawnEnemies(String enemyType, int num, List<ActorAction> actions) {

        for(Enemy enemy: enemyPool.getEnemies(enemyType,num)) {
            enemy.setPosition(RandomCoordinates.randomPoint(player.getBody().getPosition()));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            spawnedEnemies.add(enemy);
        }
    }

    private void spawnRandomEnemies(int num, List<ActorAction> actions) {

        for(Enemy enemy : enemyPool.getRandomEnemies(num)) {
            enemy.setPosition(RandomCoordinates.randomPoint(player.getBody().getPosition()));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            spawnedEnemies.add(enemy);
        }
        lastSpawnTime = TimeUtils.millis();
    }


    private void spawnSwarm(String enemyType, SwarmType swarmType, int size, int spacing) {
        Vector2 target = player.getBody().getPosition();
        List<Vector2> swarmCoordinates = SwarmCoordinates.getSwarmCoordinates(swarmType,size,spacing,target);
        Vector2 swarmDirection = SwarmCoordinates.swarmDirection(target, swarmType,swarmCoordinates);
        List<Enemy> swarmMembers = enemyPool.getEnemies(enemyType,size);

        for(int i = 0; i < size; i++) {
            Enemy enemy = swarmMembers.get(i);
            enemy.setPosition(swarmCoordinates.get(i));
            enemy.setSpeed(Stats.SWARM_SPEED_MULTIPLIER);
            enemy.setAction(swarmStrike(swarmDirection));
            enemy.setAction(destroyIfDefeated(player));
            spawnedEnemies.add(enemy);
        }

        lastSpawnTime = TimeUtils.millis();

    }

    private void initializePlayer() {
        // player sprite
        Texture playerSprite = new Texture(Gdx.files.internal(AnimationConstants.PLAYER_RIGHT));

        // player hitbox
        PolygonShape squarePlayer = createSquareShape(
                (playerSprite.getWidth())* AnimationConstants.PLAYER_SCALE ,
                (playerSprite.getHeight())* AnimationConstants.PLAYER_SCALE
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

        player = new Player(playerBody, AnimationConstants.PLAYER_IDLE_RIGHT, AnimationConstants.PLAYER_SCALE, Stats.player());
        player.setAction(PlayerActions.moveToInput(keyStates));
        player.setAnimation(ActorAnimations.playerMoveAnimation());


        squarePlayer.dispose();
    }


    private void updatePlayerAction() {
        player.doAction();

    }

    private void updateEnemyAction() {
        for(Enemy enemy: spawnedEnemies) {
            enemy.doAction();
        }


    }

    public void updateActorActions() {
        updatePlayerAction();
        updateEnemyAction();

    }

    public Lock getRenderLock() {
        return renderLock;
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
