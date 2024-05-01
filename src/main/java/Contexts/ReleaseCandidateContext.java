package Contexts;

import GameMap.GameMap;
import GameObjects.Actors.ActorAction.WeaponActions;
import GameObjects.Actors.Enemy;
import GameObjects.Actors.Pickups;
import GameObjects.Animations.AnimationRendering.AnimationLibrary;
import GameObjects.Factories.*;
import GameObjects.ObjectTypes.*;
import GameObjects.Actors.ActorAction.PlayerActions;
import GameObjects.ObjectTypes.TerrainType;
import GameObjects.ObjectTypes.WeaponType;
import GameObjects.Factories.EnemyFactory;
import GameObjects.Actors.Player;
import GameObjects.Factories.PlayerFactory;
import GameObjects.Factories.TerrainFactory;
import GameObjects.Factories.WeaponFactory;
import GameObjects.Pool.ObjectPool;
import GameObjects.StaticObjects.Terrain;
import GameObjects.Actors.Weapon;
import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Simulation.Simulation;
import Tools.BodyTool;
import Tools.FilterTool;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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

import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.getBottomLeftCorrection;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

import Camera.TargetCamera;

public class ReleaseCandidateContext extends Context {


    private final SpriteBatch batch;
    private final Camera camera;

    public static final double SPAWN_RADIUS = (double)0.7*SCREEN_WIDTH;

    private World world;

    private Player player;
    private Pickups pickup;
    private Weapon orbitWeapon;
    private ArrayList<Enemy> spawnedEnemies;
    private final BitmapFont font;

    private RollingSum UpdateTime;
    private RollingSum FrameTime;
    private RollingSum FPS;

    private ObjectPool<Enemy, EnemyType> enemyPool;

    private RollingSum UPS;
    private long previousFrameStart = System.nanoTime();

    private final Lock renderLock;

    private ProgressBar xpBar;
    private float xpAmount;
    private int level;
    private Label levelLabel;
    private Label timerLabel;
    private long startTime;
    private Table weaponTable;

    private KeyStates keyStates;

    private final Simulation sim;
    private final Thread simThread;
    private  EnemyFactory enemyFactory;
    private PickupsFactory pickupsFactory;

    private TerrainFactory terrainFactory;

    private List<Terrain> spawnedTerrain;

    private List<Weapon> drawableWeapons;

    private WeaponFactory weaponFactory;

    private Sprite grass;
    private List<Terrain> drawableTerrain;
    private List<Pickups> drawablePickups;
    private List<Enemy> drawableEnemies;

    private AnimationLibrary animationLibrary;

    private PlayerFactory playerFactory;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = false; //not working!!!

    private Box2DDebugRenderer debugRenderer;

    private ObjectPool<Weapon,WeaponType> weaponPool;
    private ObjectPool<Pickups, PickupType> pickupsPool;

    private ObjectPool<Terrain, TerrainType> terrainPool;

    float elapsedTime;
    private Set<Body> toBoKilled;



    private AtomicLong synchronizer;

    float angle = 0;

    float totalRotation = 0;

    long lastOrbit;

    boolean gameOver = false;

    private float tiledMapScale = 1f;
    private GameMap gMap;



    public ReleaseCandidateContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;
        this.camera = camera;


        level = 0;
        xpAmount = 0;

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
                    case Input.Keys.P -> {me.getContextualInputProcessor().setContext("PAUSEMENU"); yield true;}
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

        gMap.renderTiledMap((OrthographicCamera) camera);

        debugRenderer.render(world, camera.combined);

        TargetCamera.updateCamera(player.getBody().getPosition(), camera, gMap.getTiledMap(), tiledMapScale);

        Vector2 origin;
        origin = player.getBody().getPosition().cpy();
        origin.sub(getBottomLeftCorrection(player.getBody().getFixtureList().get(0).getShape()));

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

        elapsedTime += Gdx.graphics.getDeltaTime();

        int i = 0;

        for (Enemy enemy : drawableEnemies) {
            if(i > 100) batch.flush();
            if(enemy.isUnderAttack()) {
                batch.setColor(Color.RED);
            }
            enemy.draw(batch, elapsedTime);
            batch.setColor(Color.WHITE);
            i++;
        }

        for(Weapon weapon : drawableWeapons) {
            if(weapon.getBody().isActive()) {
                weapon.draw(batch,elapsedTime);
            }

        }

        for(Terrain terrain : drawableTerrain) {
            if(i > 100) batch.flush();
            terrain.draw(batch, elapsedTime);
            i++;
        }

        // Draw pickups
        for (Pickups pickup : drawablePickups) {
            if(i > 100) batch.flush();
            pickup.draw(batch, elapsedTime);
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
            player.draw(batch, elapsedTime);
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

    private void createWorld() {
        // sets up world
        animationLibrary = new AnimationLibrary();
        debugRenderer = new Box2DDebugRenderer();
        Box2D.init();
        world = new World(new Vector2(0, 0), true);

        this.gMap = new GameMap("assets/damaged_roads_map.tmx", tiledMapScale);
        gMap.createMapBorder(world);

        enemyFactory = new EnemyFactory();
        drawableEnemies = new ArrayList<>();

        pickupsFactory = new PickupsFactory();

        terrainFactory = new TerrainFactory();
        drawableTerrain = new ArrayList<>();
        drawablePickups = new ArrayList<>();

        weaponFactory = new WeaponFactory();
        playerFactory = new PlayerFactory();
        drawableWeapons =  new ArrayList<>();

        player = playerFactory.create(PlayerType.PLAYER1);
        player.addToWorld(world);
//        player.setPosition(getMiddleOfMapPosition(map, tiledMapScale));
        player.setPosition(gMap.getMiddleOfMapPosition());
        player.setAction(PlayerActions.moveToInput(keyStates));
        player.setAction(PlayerActions.coolDown(500));

        player.renderAnimations(animationLibrary);




        pickup = pickupsFactory.create(PickupType.PICKUPORB);
        pickup.renderAnimations(animationLibrary);


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


        enemyPool = new ObjectPool<>(world, enemyFactory, List.of(EnemyType.values()),200);
        terrainPool = new ObjectPool<>(world, terrainFactory, List.of(TerrainType.values()), 200);
        weaponPool = new ObjectPool<>(world,weaponFactory, List.of(WeaponType.values()), 20);
        pickupsPool = new ObjectPool<>(world, pickupsFactory, List.of(PickupType.values()), 200);

        spawnOrbitingWeapons(player,4,WeaponType.KNIFE,150,0.1f,0);
        toBoKilled = new HashSet<>();

        world.setContactListener(new ObjectContactListener());

        //world.step(1/60f, 10, 10);
    }

    /**
     * Orbits desired amount of weapons around player
     * @param player player to orbit
     * @param numWeapons number of weapons to orbit
     * @param weaponType type of weapon to orbit
     * @param orbitRadius radius of weapon to player
     * @param orbitSpeed speed of weapons
     * @param orbitInterval time between each orbit loop
     */
    private void spawnOrbitingWeapons(Player player,int numWeapons,WeaponType weaponType,float orbitRadius,float orbitSpeed,long orbitInterval){
        float angle=0;
        for(Weapon weapon:weaponPool.get(weaponType,numWeapons)){
            weapon.setAction(WeaponActions.orbitPlayer(orbitRadius,orbitSpeed,player,orbitInterval));
            weapon.setOwner(player);
            weapon.setAngleToPlayer(angle);
            weapon.renderAnimations(animationLibrary);
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

    public ObjectPool<Pickups, PickupType> getPickupsPool() {
        return pickupsPool;
    }

    public List<Terrain> getDrawableTerrain() {
        return drawableTerrain;
    }

    public List<Pickups> getDrawablePickups() {
        return drawablePickups;
    }
    public ObjectPool<Terrain, TerrainType> getTerrainPool() {
        return terrainPool;
    }

    public Weapon getOrbitWeapon() {
        return orbitWeapon;
    }

    public void gameOver() {
        gameOver = true;
    }

    public AnimationLibrary getAnimationLibrary() {
        return animationLibrary;
    }

    public List<Weapon> getDrawableWeapons() {
        return drawableWeapons;
    }
}
