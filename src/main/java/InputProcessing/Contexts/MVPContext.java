package InputProcessing.Contexts;

import GameObjects.Actors.ActorAction.ActorAction;
import GameObjects.Actors.Enemy.Enemy;
import GameObjects.Actors.ObjectTypes.*;
import GameObjects.Actors.ActorAction.PlayerActions;
import GameObjects.Factories.EnemyFactory;
import GameObjects.Actors.Player.Player;
import GameObjects.Factories.PlayerFactory;
import GameObjects.Factories.TerrainFactory;
import GameObjects.Factories.WeaponItemFactory;
import GameObjects.ObjectPool;
import GameObjects.Terrain.Terrain;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.Coordinates.SpawnCoordinates;
import InputProcessing.Coordinates.SwarmCoordinates;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
import Simulation.Simulation;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static GameObjects.Actors.ActorAction.EnemyActions.*;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.getBottomLeftCorrection;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;
import static InputProcessing.Contexts.PixelsPerMeter.PPM;

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

    private ObjectPool<Enemy, EnemyType> enemyPool;

    private RollingSum UPS;
    private long previousFrameStart = System.nanoTime();

    private final Lock renderLock;


    private KeyStates keyStates;

    private final Simulation sim;
    private final Thread simThread;
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

    private ObjectPool<Terrain, TerrainType> terrainPool;

    float elapsedTime;
    private Set<Body> toBoKilled;



    private AtomicLong synchronizer;

    private TiledMap map;
    private OrthogonalTiledMapRenderer tiledMapRenderer;
    private float tileMapScale = 4f;



    public MVPContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;

        this.setInputProcessor(createInputProcessor());
        setupDebug();
        font = new BitmapFont();
        font.setColor(Color.RED);
        renderLock = new ReentrantLock(true);
        synchronizer = new AtomicLong();

        //create and start simulation
        createWorld();
        sim = new Simulation(this);
        simThread = new Thread(sim);
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

    private void cameraUpdate(Vector2 target) {
        Vector3 position = camera.position;
        position.x = target.x * PPM;
        position.y = target.y * PPM;
        camera.position.set(position);
        camera.update();
    }


    @Override
    public void render(float delta) {


        FPS.add(System.nanoTime() - previousFrameStart);

        previousFrameStart = System.nanoTime();

        renderLock.lock();
        long renderStartTime = System.nanoTime();
        ScreenUtils.clear(Color.GREEN);

        tiledMapRenderer.setView((OrthographicCamera) camera);
        tiledMapRenderer.render();

        //debugRenderer.render(world, camera.combined);

        cameraUpdate(player.getBody().getPosition());
//        Vector2 origin;
//        origin = player.getBody().getPosition().cpy();
//        origin.add(getBottomLeftCorrection(player.getBody().getFixtureList().get(0).getShape()));
//
//        //center camera at player
//        camera.position.x = origin.x;
//        camera.position.y = origin.y;
//        camera.position.z = 0;
//        camera.update(true);

        batch.begin();

        // draw enemies

        elapsedTime += Gdx.graphics.getDeltaTime();

        font.draw(batch, "fps: " + String.format("%.1f", 1_000_000_000F/FPS.avg()), 10, 80);
        font.draw(batch, "ups: " + String.format("%.1f",1_000_000_000F/UPS.avg()), 10, 60);
        font.draw(batch, "us/f: " + String.format("%.0f",FrameTime.avg()/1_000), 10, 40);
        font.draw(batch, "us/u: " + String.format("%.0f",UpdateTime.avg()/1_000), 10, 20);

        int i = 0;

        for (Enemy enemy : drawableEnemies) {
            if(i > 100) batch.flush();
            enemy.doAnimation();
            enemy.draw(batch, elapsedTime);
            i++;
        }
        for(Terrain terrain : drawableTerrain) {
            if(i > 100) batch.flush();
            terrain.draw(batch);
            i++;
        }

        player.doAnimation();
        player.draw(batch, elapsedTime);
        batch.end();

        frameCount++;
        synchronizer.incrementAndGet();
        renderLock.unlock();

        FrameTime.add(System.nanoTime() - renderStartTime);



        //temp physics

//
//        for (Enemy enemy : drawableEnemies) {
//            enemy.doAction();
//        }
//
//        player.doAction();
//
//        if (TimeUtils.millis() - lastSpawnTime > 5000) {
//            spawnSwarm(EnemyType.ENEMY1,SwarmType.LINE,10,100, SWARM_SPEED_MULTIPLIER);
//            spawnTerrain(TerrainType.TREE);
//        }
//
//
//
//        removeDestroyedEnemies();
//
//        world.step(1/(float) 60, 10, 10);

    }




    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        renderLock.lock();
        sim.pause();
        renderLock.unlock();
    }

    @Override
    public void resume() {
        sim.unpause();
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

    private Vector2 getMiddleOfMapPosition(TiledMap map, float scale) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int tileWidth = layer.getTileWidth(); // width of a tile in pixels
        int tileHeight = layer.getTileHeight(); // height of a tile in pixels
        int mapWidth = layer.getWidth(); // width of the tilemap in tiles
        int mapHeight = layer.getHeight(); // height of the tilemap in tiles

        return new Vector2(mapWidth * tileWidth * scale / 2f, mapHeight * tileHeight * scale / 2f);
    }


    private void createWorld() {
        // sets up world

        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);

        this.map = new TmxMapLoader().load("assets/damaged_roads_map.tmx");
        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(map, tileMapScale);

        enemyFactory = new EnemyFactory();
        drawableEnemies = new ArrayList<>();

        terrainFactory = new TerrainFactory();
        drawableTerrain = new ArrayList<>();


        playerFactory = new PlayerFactory();
        player = playerFactory.create(PlayerType.PLAYER1);
        player.addToWorld(world);
        player.setPosition(getMiddleOfMapPosition(map, tileMapScale));
        player.setAction(PlayerActions.moveToInput(keyStates));

        player.addWeapon(WeaponType.KNIFE);

        enemyPool = new ObjectPool<>(world, enemyFactory, List.of(EnemyType.values()),200);
        terrainPool = new ObjectPool<>(world, terrainFactory, List.of(TerrainType.TREE), 50);

        toBoKilled = new HashSet<>();
        //ContactListener contactListener = new EnemyContactListener(world, player.getBody(), toBoKilled);
        //world.setContactListener(contactListener);

        //world.step(1/60f, 10, 10);
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

    public AtomicLong getSynchronizer() {
        return synchronizer;
    }


    public List<Enemy> getDrawableEnemies() {
        return drawableEnemies;
    }

    public ObjectPool<Enemy, EnemyType> getEnemyPool() {
        return enemyPool;
    }

    public List<Terrain> getDrawableTerrain() {
        return drawableTerrain;
    }

    public ObjectPool<Terrain, TerrainType> getTerrainPool() {
        return terrainPool;
    }


}
