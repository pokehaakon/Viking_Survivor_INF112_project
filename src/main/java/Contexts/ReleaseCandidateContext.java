package Contexts;

import GameObjects.Actors.ActorAction.WeaponActions;
import GameObjects.Actors.Enemy;
import GameObjects.Actors.Pickups;
import GameObjects.Factories.*;
import GameObjects.ObjectTypes.*;
import GameObjects.Actors.ActorAction.PlayerActions;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.ObjectTypes.WeaponType;
//import GameObjects.Factories.EnemyFactory;
import GameObjects.Actors.Player;
import GameObjects.Factories.PlayerFactory;
import GameObjects.Factories.TerrainFactory;
import GameObjects.Factories.WeaponFactory;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import GameObjects.Actors.Weapon;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Rendering.Animations.AnimationRendering.GIFS;
import Rendering.Animations.AnimationRendering.Sprites;
import Simulation.Simulation;
import Simulation.GameWorld;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import Simulation.ObjectContactListener;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.getBottomLeftCorrection;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;

public class ReleaseCandidateContext extends Context {
    public static final double SPAWN_RADIUS = (double)0.7*SCREEN_WIDTH;
    public static final Vector2 SPAWN_RECT = new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT);
    public static final Vector2 DE_SPAWN_RECT = SPAWN_RECT.cpy().scl(1.3f);


    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private World world;
    private Player player;
    private final BitmapFont font;
    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;
    private ObjectPool<Enemy> enemyPool;
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
    private final Simulation sim;
    private final Thread simThread;
    //private EnemyFactory enemyFactory;
    private PickupsFactory pickupsFactory;
    private TerrainFactory terrainFactory;
    private List<Weapon> drawableWeapons;
    private WeaponFactory weaponFactory;
    private List<Terrain> drawableTerrain;
    private List<Pickups> drawablePickups;
    private List<Enemy> drawableEnemies;
    private PlayerFactory playerFactory;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!
    private Box2DDebugRenderer debugRenderer;
    private ObjectPool<Weapon> weaponPool;
    private ObjectPool<Pickups> pickupsPool;
    private ObjectPool<Terrain> terrainPool;
    private List<Body> toBoKilled;
    private AtomicLong synchronizer;
    boolean gameOver = false;

//    private TiledMap map;
//    private OrthogonalTiledMapRenderer tiledMapRenderer;
//    private float tiledMapScale = 4f;


    private GameWorld gameWorld;


    public ReleaseCandidateContext(String name, SpriteBatch batch, OrthographicCamera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;

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

    private void updateCamera(Vector2 player, int viewportWidth, int viewportHeight, TiledMap map, float tiledMapScale) {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        float mapHeight = layer.getHeight() * layer.getTileHeight() * tiledMapScale;
        float mapWidth = layer.getWidth() * layer.getTileWidth() * tiledMapScale;

        if(player.x < viewportWidth / 2f) camera.position.x = viewportWidth / 2f;
        else if(player.x > mapWidth - viewportWidth / 2f) camera.position.x = mapWidth - viewportWidth / 2f;
        else camera.position.x = player.x;

        if(player.y < viewportHeight / 2f) camera.position.y = viewportHeight / 2f;
        else if(player.y > mapHeight - viewportHeight / 2f) camera.position.y = mapHeight - viewportHeight / 2f;
        else camera.position.y = player.y;

        camera.update();
    }

    @Override
    public void render(float delta) {

        FPS.add(System.nanoTime() - previousFrameStart);

        Sprites.loadWaiting();
        GIFS.loadWaiting();

        previousFrameStart = System.nanoTime();

        renderLock.lock();
        long renderStartTime = System.nanoTime();
        ScreenUtils.clear(Color.GREEN);

//        this.tiledMapRenderer = new OrthogonalTiledMapRenderer(map, tiledMapScale);
//        tiledMapRenderer.setView((OrthographicCamera) camera);
//        tiledMapRenderer.render();

        gameWorld.render(camera, delta);
        debugRenderer.render(world, camera.combined);

        //updateCamera(player.getBody().getPosition(), Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), map, tiledMapScale);


        Vector2 origin;
        origin = player.getBody().getPosition().cpy();
        origin.sub(getBottomLeftCorrection(player.getBody().getFixtureList().get(0).getShape()));
        camera.position.x = origin.x;
        camera.position.y = origin.y;

        // Save player position for further use
        float playerPosX = origin.x;
        float playerPosY = origin.y;

//        //center camera at player
//        camera.position.x = playerPosX;
//        camera.position.y = playerPosY;
//        camera.position.z = 0;
//        camera.update(true);

        batch.begin();


        // draw enemies

        int i = 0;

        for (Enemy enemy : drawableEnemies) {
            if(i > 100) batch.flush();
            if(enemy.isUnderAttack()) {
                batch.setColor(Color.RED);
            }
            enemy.draw(batch, frameCount);
            batch.setColor(Color.WHITE);
            i++;
        }

        for(Weapon weapon : drawableWeapons) {
            if(weapon.getBody().isActive()) {
                weapon.draw(batch, frameCount);
            }

        }

        for(Terrain terrain : drawableTerrain) {
            if(i > 100) batch.flush();
            terrain.draw(batch, frameCount);
            i++;
        }

        // Draw pickups
        for (Pickups pickup : drawablePickups) {
            if(i > 100) batch.flush();
            pickup.draw(batch, frameCount);
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



        if(player.isUnderAttack()) {
            batch.setColor(Color.RED);
        }

        if(!gameOver) {
            player.draw(batch, frameCount);
        }

        batch.setColor(Color.WHITE);
        if(!gameOver) {
            font.draw(batch, "Player HP: " + String.valueOf(player.HP), playerPosX +400,playerPosY +470);
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

    private float[] createPolyLine(PolygonMapObject polygon) {
        for(float juice : polygon.getPolygon().getTransformedVertices()) System.out.println(juice);
        float[] points = polygon.getPolygon().getTransformedVertices();
        float[] newPoints = new float[points.length + 2];
        newPoints[0] = 0.0f;
        newPoints[1] = 0.0f;

        for (int i = 0; i < points.length; i++) {
            newPoints[i + 2] = points[i];
        }
        return newPoints;
    }

//    private void createMapObjects(String objectLayerName, World world) {
//        MapLayer layer = map.getLayers().get(objectLayerName);
//
//        for(MapObject object : layer.getObjects()) {
//
//            // create the shape
//            ChainShape shape = new ChainShape();
//            shape.createChain(createPolyLine((PolygonMapObject) object));
//
//            BodyTool.createBody(
//                    world,
//                    new Vector2(),
//                    shape,
//                    createFilter(
//                            FilterTool.Category.WALL,
//                            new FilterTool.Category[] {
//                                    FilterTool.Category.PLAYER
//                            }
//                    ),
//                    1f,
//                    0f,
//                    0f,
//                    false,
//                    BodyDef.BodyType.StaticBody
//            );
//        }
//    }

    private void createWorld() {
        // sets up world
        //animationLibrary = new AnimationLibrary();
        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);

        //map = new TmxMapLoader().load("assets/damaged_roads_map.tmx");

        //enemyFactory = new EnemyFactory();
        pickupsFactory = new PickupsFactory();
        terrainFactory = new TerrainFactory();
        weaponFactory = new WeaponFactory();
        playerFactory = new PlayerFactory();


        drawableEnemies = new ArrayList<>();
        drawableTerrain = new ArrayList<>();
        drawablePickups = new ArrayList<>();

        drawableWeapons =  new ArrayList<>();

        player = playerFactory.create("PlayerType:PLAYER1");
        player.addToWorld(world);
        //player.setPosition(getMiddleOfMapPosition(map, tiledMapScale));
        player.setPosition(new Vector2());
        player.setAction(PlayerActions.moveToInput(keyStates));
        player.setAction(PlayerActions.coolDown(500));

        //player.renderAnimations(animationLibrary);




        //pickup = pickupsFactory.create("PickupType:PICKUPORB");
        //pickup.renderAnimations(animationLibrary);


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


//        enemyPool = new ObjectPool<>(world, enemyFactory, List.of(EnemyType.values()),200);
        enemyPool = new ObjectPool<>(
                world,
                ExperimentalEnemyFactory::create,
                200
        );
        terrainPool = new ObjectPool<>(
                world,
                terrainFactory,
                200
        );
        weaponPool = new ObjectPool<>(
                world,
                weaponFactory,
                20
        );
        pickupsPool = new ObjectPool<>(
                world,
                pickupsFactory,
                200
        );

        spawnOrbitingWeapons(player,4, "WeaponType:KNIFE",150,0.1f,0);

        gameWorld = new GameWorld("mapdefines/test.wdef", player, enemyPool, terrainPool, drawableEnemies, drawableTerrain);

        toBoKilled = new ArrayList<>();

        world.setContactListener(new ObjectContactListener());

        //world.step(1/60f, 10, 10);
    }



    /**
     * Orbits desired amount of weapons around player
     * @param player player to orbit
     * @param numWeapons number of weapons to orbit
     * @param weaponName type of weapon to orbit
     * @param orbitRadius radius of weapon to player
     * @param orbitSpeed speed of weapons
     * @param orbitInterval time between each orbit loop
     */
    private void spawnOrbitingWeapons(Player player, int numWeapons, String weaponName, float orbitRadius, float orbitSpeed, long orbitInterval){
        float angle=0;
        for(Weapon weapon : weaponPool.get(weaponName, numWeapons)){
            weapon.setAction(WeaponActions.orbitPlayer(orbitRadius,orbitSpeed,player,orbitInterval));
            weapon.setOwner(player);
            weapon.setAngleToPlayer(angle);
            //weapon.renderAnimations(animationLibrary);
            drawableWeapons.add(weapon);
            angle+=(float)((float)2*Math.PI/numWeapons);
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

    public List<Body> getToBoKilled() {
        return toBoKilled;
    }

    public AtomicLong getSynchronizer() {
        return synchronizer;
    }


    public List<Enemy> getDrawableEnemies() {
        return drawableEnemies;
    }

    public ObjectPool<Enemy> getEnemyPool() {
        return enemyPool;
    }

    public ObjectPool<Pickups> getPickupsPool() {
        return pickupsPool;
    }

    public List<Terrain> getDrawableTerrain() {
        return drawableTerrain;
    }

    public List<Pickups> getDrawablePickups() {
        return drawablePickups;
    }
    public ObjectPool<Terrain> getTerrainPool() {
        return terrainPool;
    }

//    public Weapon getOrbitWeapon() {
//        return orbitWeapon;
//    }

    public void gameOver() {
        gameOver = true;
    }

    public GameWorld getGameWorld() {return gameWorld;}

    public List<Weapon> getDrawableWeapons() {
        return drawableWeapons;
    }
}
