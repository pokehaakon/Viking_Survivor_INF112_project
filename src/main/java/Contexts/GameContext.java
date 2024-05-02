package Contexts;

import Camera.TargetCamera;
import GameMap.GameMap;
import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectActions.PlayerActions;
import GameObjects.ObjectFactory;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Rendering.Animations.AnimationRendering.GIFS;
import Rendering.Animations.AnimationRendering.Sprites;
import Simulation.GameWorld;
import Simulation.ObjectContactListener;
import Simulation.Simulation;
import Tools.Pool.ObjectPool;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public class GameContext extends Context {
    static public float zoomLevel = 0.07f;
    public static final double SPAWN_RADIUS = 0.7*SCREEN_WIDTH * zoomLevel;
    public static final Vector2 SPAWN_RECT = (new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT)).scl(zoomLevel * 1.05f);
    public static final Vector2 DE_SPAWN_RECT = SPAWN_RECT.cpy().scl(1.3f);

    private List<Actor> pickup;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private World world;
    private Actor player;
    private final BitmapFont font;
    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;
    private ObjectPool<Actor> actorPool;
    private RollingSum UPS;
    private long previousFrameStart = System.nanoTime();
    private final Lock renderLock;
    private ProgressBar xpBar;
    private int level;
    private Label levelLabel;
    private Label timerLabel;
    private long startTime;
    private Table weaponTable;
    private KeyStates keyStates;

    private List<Actor> drawableEnemies;
    private final Simulation sim;
    private final Thread simThread;
    private List<GameObject> drawableObjects;
    private List<Actor> drawableActors;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!
    private Box2DDebugRenderer debugRenderer;
    private ObjectPool<GameObject> objectPool;
    private List<Body> toBoKilled;
    private AtomicLong synchronizer;
    boolean gameOver = false;

    //private float tiledMapScale = 1f / 16;
    private GameMap gMap;

    private Vector2 previousFramePlayerSpeed = Vector2.Zero;
    private GameWorld gameWorld;


    public GameContext(String name, SpriteBatch batch, OrthographicCamera camera, ContextualInputProcessor iProc) {
        super(name, iProc);
        pickup = new ArrayList<>();

        ObjectFactory.empty();

        this.batch = batch;
        this.camera = camera;
        camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
        camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;

        level = 0;



        this.keyStates = new KeyStates();
        this.setInputProcessor(
                new GameInputProcessor(
                        this,
                        camera,
                        keyStates,
                        flt -> {
                            float temp = this.zoomLevel;
                            zoomLevel = flt;
                            return temp;
                        }
                ));
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




    @Override
    public void show() {

    }

//    private void updateCamera(Vector2 player, int viewportWidth, int viewportHeight, TiledMap map, float tiledMapScale) {
//        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
//        float mapHeight = layer.getHeight() * layer.getTileHeight() * tiledMapScale;
//        float mapWidth = layer.getWidth() * layer.getTileWidth() * tiledMapScale;
//
//        if(player.x < viewportWidth / 2f) camera.position.x = viewportWidth / 2f;
//        else if(player.x > mapWidth - viewportWidth / 2f) camera.position.x = mapWidth - viewportWidth / 2f;
//        else camera.position.x = player.x;
//
//        if(player.y < viewportHeight / 2f) camera.position.y = viewportHeight / 2f;
//        else if(player.y > mapHeight - viewportHeight / 2f) camera.position.y = mapHeight - viewportHeight / 2f;
//        else camera.position.y = player.y;
//
//        camera.update();
//    }

    @Override
    public void render(float delta) {

        FPS.add(System.nanoTime() - previousFrameStart);

        Sprites.loadWaiting();
        GIFS.loadWaiting();

        previousFrameStart = System.nanoTime();

        renderLock.lock();
        long renderStartTime = System.nanoTime();
        ScreenUtils.clear(Color.GREEN);

        //gMap.renderTiledMap((OrthographicCamera) camera);

        gameWorld.render(camera, delta);
        debugRenderer.render(world, camera.combined);

        Vector2 origin;
        origin = player.getBody().getPosition().cpy();
        previousFramePlayerSpeed.scl(1f/Simulation.SET_UPS);
        Vector2 correctedOrigin = origin.cpy().add(previousFramePlayerSpeed);

        TargetCamera.updateCamera(correctedOrigin, camera, gMap);
        previousFramePlayerSpeed = player.getBody().getLinearVelocity();

        // Save player position for further use
        float playerPosX = origin.x;
        float playerPosY = origin.y;

        batch.begin();


        // draw enemies

        int i = 0;
//        for(Actor p : pickup) {
//            p.draw(batch,frameCount);
//        }
        for (Actor actor : drawableActors) {
            if(i > 100) batch.flush();
            if(actor.isInCoolDown()) {
                batch.setColor(Color.RED);

            }
            actor.draw(batch, frameCount);
            batch.setColor(Color.WHITE);

            i++;
        }



        for(GameObject object : drawableObjects) {
            if(i > 100) batch.flush();
            object.draw(batch, frameCount);
            i++;
        }




        if(!gameOver) {
            // Performance statistics
            font.draw(batch, "FPS: " + String.format("%.1f", 1_000_000_000F/FPS.avg()), playerPosX -500, playerPosY -420);
            font.draw(batch, "UPS: " + String.format("%.1f",1_000_000_000F/UPS.avg()), playerPosX -500, playerPosY -440);
            font.draw(batch, "US/F: " + String.format("%.0f",FrameTime.avg()/1_000), playerPosX -500, playerPosY -460);
            font.draw(batch, "US/U: " + String.format("%.0f",UpdateTime.avg()/1_000), playerPosX -500, playerPosY -480);

            // XP bar and level
            xpBar.draw(batch, 1);
            levelLabel.draw(batch, 1);
            xpBar.setPosition(playerPosX -512, playerPosY +495);
            levelLabel.setPosition(playerPosX -512, playerPosY +495);

            // Clock
            long elapsedMillis = TimeUtils.timeSinceMillis(startTime);
            int elapsedSeconds = (int)(elapsedMillis / 1000);

            int minutes = elapsedSeconds / 60;
            int seconds = elapsedSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));

            timerLabel.draw(batch, 1);
            timerLabel.setPosition(playerPosX -55, playerPosY +430);

            // Weapon table
            weaponTable.setPosition(playerPosX -295,playerPosY +455);
            weaponTable.draw(batch, 1);
        }
        //batch.setColor(Color.WHITE);


//        if(!gameOver) {
//            player.draw(batch, frameCount);
//        }

        batch.setColor(Color.WHITE);
        if(!gameOver) {
            font.draw(batch, "Player HP: " + player.getHP(), playerPosX +400,playerPosY +470);
        }


        if(gameOver) {
            font.getData().setScale(5,5);
            font.draw(batch, "GAME OVER", playerPosX -200, playerPosY);
        }
        batch.end();

        frameCount++;
        synchronizer.incrementAndGet();
        renderLock.unlock();

        FrameTime.add(System.nanoTime() - renderStartTime);


        if(gameOver) {
            try {
                Thread.sleep(1000 * 3);
            } catch (InterruptedException e) {
                //throw new RuntimeException(e);
            }
            sim.stopSim();
            getContextualInputProcessor().setContext("MAINMENU");
        }
    }


    @Override
    public void resize(int width, int height) {
        System.out.println(width + ", " + height);
        camera.viewportHeight = height * zoomLevel;
        camera.viewportWidth = width * zoomLevel;
        camera.update();
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

    private void createWorld() {
        // sets up world
        //animationLibrary = new AnimationLibrary();
        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);



        //enemyFactory = new EnemyFactory();
        drawableEnemies = new ArrayList<>();
        drawableActors = new ArrayList<>();
        drawableObjects = new ArrayList<>();
        Function<String, GameObject> objectFactory = s -> {
            var obj = ObjectFactory.create(s);
            obj.addToWorld(world);
            return obj;
        };

        Function<String, Actor> actorFactory = s -> {
            var obj = ObjectFactory.createActor(s);
            obj.addToWorld(world);
            return obj;
        };
        objectPool = new ObjectPool<>(objectFactory);
        actorPool = objectPool.createSubPool(actorFactory);


//        player = ExperimentalFactory.createActor("PlayerType:PLAYER1");
        gameWorld = new GameWorld("mapdefines/demo.wdef", actorPool, objectPool, drawableActors, drawableObjects);
        gMap = gameWorld.getGameMap();
        gMap.createMapBorder(world);

        player = gameWorld.player;
        player.addToWorld(world);
//        player.setPosition(getMiddleOfMapPosition(map, tiledMapScale));
        player.setPosition(gMap.getMiddleOfMapPosition()); //TODO fix this!
//        player.setAction(PlayerActions.moveToInput(keyStates));
//        player.setAction(PlayerActions.coolDown(500));
        //player.setPosition(getMiddleOfMapPosition(map, tiledMapScale));
//        player.setPosition(new Vector2());
        player.addAction(PlayerActions.moveToInput(keyStates));


        setupHUD();






        toBoKilled = new ArrayList<>();

        world.setContactListener(new ObjectContactListener());

    }

    private void setupHUD() {

        //      Create top XP bar:
        // XP bar style
        ProgressBar.ProgressBarStyle xpBarStyle = new ProgressBar.ProgressBarStyle();

        // Set XP bar background
        Pixmap xbBarPixMap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        xbBarPixMap.setColor(Color.BLACK);
        xbBarPixMap.fill();
        TextureRegionDrawable barBackgroundDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(xbBarPixMap)));
        xpBarStyle.background = barBackgroundDrawable;
        xpBarStyle.background.setMinHeight(20);

        // Set XP bar "knob" (XP amount visualiser)
        xbBarPixMap.setColor(Color.CYAN);
        xbBarPixMap.fill();
        TextureRegionDrawable barKnobDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(xbBarPixMap)));
        xbBarPixMap.dispose();
        xpBarStyle.knob = barKnobDrawable;
        xpBarStyle.knob.setMinHeight(20);

        xpBar = new ProgressBar(0, 100, 1, false, xpBarStyle);
        xpBar.setValue(0);
        xpBar.setPosition(0, Gdx.graphics.getHeight() - xpBar.getHeight());
        xpBar.setWidth(Gdx.graphics.getWidth());

        //      Create level counter
        Skin lvlSkin = new Skin();
        BitmapFont lvlFont = new BitmapFont();
        font.setColor(Color.WHITE);
        lvlSkin.add("default", font, BitmapFont.class);

        Label.LabelStyle lvlLabelStyle = new Label.LabelStyle();
        lvlLabelStyle.font = lvlSkin.getFont("default");
        lvlSkin.add("default", lvlLabelStyle, Label.LabelStyle.class);

        levelLabel = new Label("Level: "+ level, lvlSkin);

        // Clock
        Skin tmSkin = new Skin();
        BitmapFont tmFont = new BitmapFont();
        tmFont.setColor(Color.WHITE);
        tmFont.getData().setScale(3);
        tmSkin.add("default", tmFont, BitmapFont.class);

        Label.LabelStyle tmLabelStyle = new Label.LabelStyle();
        tmLabelStyle.font = tmSkin.getFont("default");
        tmSkin.add("default", tmLabelStyle, Label.LabelStyle.class);

        timerLabel = new Label("00:00", tmSkin);

        startTime = TimeUtils.millis();

        // Weapon table
        weaponTable = new Table();
        weaponTable.setFillParent(true);

        Pixmap wptPixmap = new Pixmap(64, 64, Pixmap.Format.RGBA8888);
        wptPixmap.setColor(Color.GRAY);
        wptPixmap.fill();
        Drawable emptySlot = new TextureRegionDrawable(new TextureRegion(new Texture(wptPixmap)));
        wptPixmap.dispose();

        for (int i= 0; i < 7; i++) {
            Image weaponSlot = new Image(emptySlot);
            weaponTable.add(weaponSlot).size(40).pad(10);
        }
    }




    public Lock getRenderLock() {
        return renderLock;
    }

    public Actor getPlayer() {
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

    public List<Body> getToBoKilled() {
        return toBoKilled;
    }

    public AtomicLong getSynchronizer() {
        return synchronizer;
    }

    public List<Actor> getDrawableActors() {
        return drawableActors;
    }

    public ObjectPool<Actor> getActorPool() {
        return actorPool;
    }

    public ObjectPool<GameObject> getObjectPool() {
        return objectPool;
    }

    public List<GameObject> getDrawableObjects() {
        return drawableObjects;
    }

    public void gameOver() {
        gameOver = true;
    }

    public GameWorld getGameWorld() {return gameWorld;}


    public List<Actor> getDrawableEnemies() {
        return drawableEnemies;
    }

}
