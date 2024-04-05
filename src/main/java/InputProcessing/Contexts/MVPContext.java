package InputProcessing.Contexts;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.Enemy.SwarmType;
import GameObjects.Actors.ActorAction.PlayerActions;
import GameObjects.Factories.EnemyFactory;
import GameObjects.Actors.Player.Player;
import GameObjects.Factories.PlayerFactory;
import GameObjects.Factories.TerrainFactory;
import GameObjects.ObjectPool;
import GameObjects.Terrain.Terrain;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.Coordinates.SpawnCoordinates;
import InputProcessing.Coordinates.SwarmCoordinates;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
import Simulation.SimulationThread;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static GameObjects.Actors.ActorAction.EnemyActions.*;
import static GameObjects.Actors.Stats.Stats.SWARM_SPEED_MULTIPLIER;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.getBottomLeftCorrection;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public class MVPContext extends Context {


    private final SpriteBatch batch;
    private final Camera camera;

    public static final double SPAWN_RADIUS = (double)0.7*SCREEN_WIDTH;

    private World world;

    private Player player;

    private ArrayList<Enemy> spawnedEnemies;
    private final BitmapFont font;

    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;

    private ObjectPool<Enemy> enemyPool;

    private RollingSum UPS;
    private long previousFrameStart = System.nanoTime();

    private final Lock renderLock;


    private KeyStates keyStates;

    private final SimulationThread simThread;
    private  EnemyFactory enemyFactory;

    private TerrainFactory terrainFactory;

    private List<Terrain> spawnedTerrain;

    private List<Terrain> drawableTerrain;
    private List<Enemy> drawableEnemies;

    private PlayerFactory playerFactory;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!

    private Box2DDebugRenderer debugRenderer;

    private ObjectPool<Terrain> terrainPool;




    float elapsedTime;
    private Set<Body> toBoKilled;

    private long lastSpawnTime;

    private long lastSwarmSpawnTime;






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

        for (Enemy enemy : drawableEnemies) {
            enemy.doAnimation();
            enemy.draw(batch, elapsedTime);
            enemy.doAction();
        }
        for(Terrain terrain : drawableTerrain) {
            terrain.draw(batch);
        }

        player.doAnimation();
        player.draw(batch,elapsedTime);
        player.doAction();



        batch.end();


        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);
        frameCount++;

        if(TimeUtils.millis() - lastSpawnTime > 5000) {
            spawnSwarm("ENEMY1",SwarmType.LINE,10,100, SWARM_SPEED_MULTIPLIER);
            spawnTerrain("TREE");
        }



        removeDestroyedEnemies();

        world.step(1/(float) 60, 10, 10);

    }

    /**
     * Despawns destroyed enemies by returning them to enemy pool and removing them from the spawned enemy list
     */
    public void removeDestroyedEnemies() {
        for(Iterator<Enemy> iter = drawableEnemies.iterator(); iter.hasNext();) {
            Enemy enemy = iter.next();
            if(enemy.isDestroyed()) {
                enemy.resetActions();
                enemyPool.returnToPool(enemy);
                iter.remove();
                // removes from list of active enemies
            }

                //System.out.println("destroy!");
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

        enemyFactory = new EnemyFactory();
        drawableEnemies = new ArrayList<>();

        terrainFactory = new TerrainFactory();
        drawableTerrain = new ArrayList<>();


        playerFactory = new PlayerFactory();
        player = playerFactory.create("PLAYER1");
        player.addToWorld(world);
        player.setAction(PlayerActions.moveToInput(keyStates));


        enemyPool = new ObjectPool<>(world, enemyFactory,Arrays.asList("ENEMY1", "ENEMY2"),200);
        terrainPool = new ObjectPool<>(world, terrainFactory, List.of("TREE"), 50);

        toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        world.setContactListener(contactListener);

        world.step(1/60f, 10, 10);
    }

    private void spawnEnemies(String enemyType, int num, List<ActorAction> actions) {

        for(Enemy enemy: enemyPool.get(enemyType,num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), SPAWN_RADIUS));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            drawableEnemies.add(enemy);
        }
    }

    private void spawnRandomEnemies(int num, List<ActorAction> actions) {

        for(Enemy enemy : enemyPool.getRandom(num)) {
            enemy.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), SPAWN_RADIUS));
            for(ActorAction action : actions) {
                enemy.setAction(action);
            }
            drawableEnemies.add(enemy);
        }
        lastSpawnTime = TimeUtils.millis();
    }

    private void spawnTerrain(String type) {
        Terrain terrain = terrainPool.get(type);
        terrain.setPosition(SpawnCoordinates.randomSpawnPoint(player.getBody().getPosition(), SPAWN_RADIUS));
        drawableTerrain.add(terrain);
        lastSwarmSpawnTime = TimeUtils.millis();
    }


    private void spawnSwarm(String enemyType, SwarmType swarmType, int size, int spacing, int speedMultiplier) {
        List<Enemy> swarmMembers = enemyPool.get(enemyType, size);
        LinkedList<Enemy> swarm = SwarmCoordinates.createSwarm(swarmType,swarmMembers,player.getBody().getPosition(),SPAWN_RADIUS,size, spacing,speedMultiplier);
        for(Enemy enemy : swarm) {
            enemy.setAction(moveInStraightLine());
            enemy.setAction(destroyIfDefeated(player));
            drawableEnemies.add(enemy);
        }

        lastSpawnTime = TimeUtils.millis();

    }





    public void updateActorActions() {
        player.doAction();
        for(Enemy enemy: drawableEnemies) {
            enemy.doAction();
        }

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
