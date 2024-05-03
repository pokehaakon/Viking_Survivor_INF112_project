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
import Tools.ExcludeFromGeneratedCoverage;
import Tools.Pool.ObjectPool;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2D;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Stream;

import static VikingSurvivor.app.HelloWorld.SET_FPS;
import static VikingSurvivor.app.Main.SCREEN_HEIGHT;
import static VikingSurvivor.app.Main.SCREEN_WIDTH;

public class GameContext extends Context {
    static public float zoomLevel = 0.07f;
    static public final double SPAWN_RADIUS = 0.7*SCREEN_WIDTH * zoomLevel;
    static public final Vector2 SPAWN_RECT = (new Vector2(SCREEN_WIDTH, SCREEN_HEIGHT)).scl(zoomLevel * 1.05f);
    static public final Vector2 DE_SPAWN_RECT = SPAWN_RECT.cpy().scl(1.3f);

    public static boolean SHOW_DEBUG_RENDER_INFO = false;

    public final SpriteBatch batch;
    public final Lock renderLock;
    public final OrthographicCamera camera;
    public final OrthographicCamera HUDcamera;
    public final BitmapFont font;
    public final Simulation sim;
    public final Thread simThread;
    public float level;
    public final KeyStates keyStates;
    public final Box2DDebugRenderer debugRenderer;
    public final AtomicLong synchronizer;

    private long frameCount = 0;
    private long previousFrameStart = System.nanoTime();
    private boolean gameOver = false;
    private Vector2 previousFramePlayerSpeed = Vector2.Zero;

    public final RollingSum UpdateTime;
    public final RollingSum FrameTime;
    public final RollingSum FPS;
    public final RollingSum UPS;

    public final World world;
    public final Actor player;
    private final GameMap gMap;
    public final GameWorld gameWorld;

    public final List<GameObject> objects;
    public final List<Actor> actors;
    public final ObjectPool<Actor> actorPool;
    public final ObjectPool<GameObject> objectPool;

    private final ProgressBar xpBar;
    private final ProgressBar hpBar;
    private final Label levelLabel;
    private final Label timerLabel;


    @ExcludeFromGeneratedCoverage(reason = "functionality covered elsewhere")
    public GameContext(SpriteBatch batch, OrthographicCamera camera, ContextualInputProcessor iProc) {
        super(iProc);

        ObjectFactory.empty();

        this.batch = batch;
        this.camera = camera;
        camera.viewportHeight = Gdx.graphics.getHeight() * zoomLevel;
        camera.viewportWidth = Gdx.graphics.getWidth() * zoomLevel;

        HUDcamera = new OrthographicCamera();
        HUDcamera.viewportHeight = camera.viewportHeight;
        HUDcamera.viewportWidth = camera.viewportWidth;
        HUDcamera.update();
//        HUDcamera.viewportHeight = Gdx.graphics.getHeight();
//        HUDcamera.viewportWidth = Gdx.graphics.getWidth();

        level = 1;

        this.keyStates = new KeyStates();
        this.setInputProcessor(
                new GameInputProcessor(
                        this,
                        camera,
                        keyStates,
                        flt -> {
                            float temp = zoomLevel;
                            zoomLevel = flt;
                            return temp;
                        }
                ));

        //setupDebug
        {
            if (SHOW_DEBUG_RENDER_INFO)
                debugRenderer = new Box2DDebugRenderer();
            else
                debugRenderer = null;

            UpdateTime = new RollingSum(60*3);
            FrameTime = new RollingSum(60*3);
            FPS = new RollingSum(60 * 3);
            UPS = new RollingSum(60 * 3);
        }

        font = new BitmapFont();
        font.setColor(Color.RED);
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.getData().setScale(0.1f);

        renderLock = new ReentrantLock(true);
        synchronizer = new AtomicLong();

                //create and start simulation
        {
            // sets up world

            Box2D.init();
            world = new World(new Vector2(0, 0), true);

            actors = new ArrayList<>();
            objects = new ArrayList<>();
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


            gameWorld = new GameWorld("mapdefines/demo.wdef", actorPool, actors);
            gMap = gameWorld.getGameMap();
            gMap.createMapBorder(world);

            player = gameWorld.player;
            player.addToWorld(world);
            player.setPosition(gMap.getMiddleOfMapPosition());

            player.addAction(PlayerActions.moveToInput(keyStates));
            world.setContactListener(new ObjectContactListener());
        }

        // Setup HUD
        {
            //      Create top XP bar:
            // XP bar style
            ProgressBar.ProgressBarStyle xpBarStyle = new ProgressBar.ProgressBarStyle();

            // Set XP bar background
            Pixmap xbBarPixMap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            xbBarPixMap.setColor(Color.BLACK);
            xbBarPixMap.fill();
            xpBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(xbBarPixMap)));
            xpBarStyle.background.setMinHeight(2);

            // Set XP bar "knob" (XP amount visualiser)
            xbBarPixMap.setColor(Color.CYAN);
            xbBarPixMap.fill();
            TextureRegionDrawable xpBarKnobDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(xbBarPixMap)));
            xbBarPixMap.dispose();
            xpBarStyle.knob = xpBarKnobDrawable;
            xpBarStyle.knobBefore = xpBarKnobDrawable;
            xpBarStyle.knob.setMinHeight(2);

            xpBar = new ProgressBar(0, 100 * level, 1, false, xpBarStyle);
            xpBar.setValue(0);
            xpBar.setPosition(HUDcamera.position.x - HUDcamera.viewportWidth / 2, HUDcamera.position.y + HUDcamera.viewportHeight / 2 - 2);
            xpBar.setWidth(HUDcamera.viewportWidth);

            //      Create HP bar:
            // HP bar style
            ProgressBar.ProgressBarStyle hpBarStyle = new ProgressBar.ProgressBarStyle();

            // Set HP bar background
            Pixmap hpBarPixMap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
            hpBarPixMap.setColor(Color.BLACK);
            hpBarPixMap.fill();
            hpBarStyle.background = new TextureRegionDrawable(new TextureRegion(new Texture(hpBarPixMap)));
            hpBarStyle.background.setMinHeight(1);

            // Set HP bar "knob" (HP amount visualiser)
            hpBarPixMap.setColor(Color.RED);
            hpBarPixMap.fill();
            TextureRegionDrawable hpBarKnobDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(hpBarPixMap)));
            hpBarPixMap.dispose();
            hpBarStyle.knob = hpBarKnobDrawable;
            hpBarStyle.knobBefore = hpBarKnobDrawable;
            hpBarStyle.knob.setMinHeight(1);

            hpBar = new ProgressBar(0, player.getHP(), 1, false, hpBarStyle);
            hpBar.setValue(player.getHP());
            hpBar.setPosition(HUDcamera.position.x, HUDcamera.position.y);
            hpBar.setWidth(10);

            //      Create level counter
            Skin lvlSkin = new Skin();
            BitmapFont lvlFont = new BitmapFont();
            lvlFont.setColor(Color.WHITE);
            lvlFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            lvlFont.getData().setScale(1/8f);

            lvlSkin.add("default", lvlFont, BitmapFont.class);
            Label.LabelStyle lvlLabelStyle = new Label.LabelStyle();
            lvlLabelStyle.font = lvlFont;
            lvlSkin.add("default", lvlLabelStyle, Label.LabelStyle.class);

            levelLabel = new Label("Level: "+ (int) level, lvlSkin);

            // Clock
            Skin tmSkin = new Skin();
            BitmapFont tmFont = new BitmapFont();
            tmFont.setColor(Color.WHITE);
            tmFont.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            tmFont.getData().setScale(0.33f);
            tmSkin.add("default", tmFont, BitmapFont.class);

            Label.LabelStyle tmLabelStyle = new Label.LabelStyle();
            tmLabelStyle.font = tmSkin.getFont("default");
            tmSkin.add("default", tmLabelStyle, Label.LabelStyle.class);

            timerLabel = new Label("00 : 00", tmSkin);
        }

        sim = new Simulation(this);
        simThread = new Thread(sim);
        simThread.start();

    }

    @ExcludeFromGeneratedCoverage
    @Override
    public void render(float delta) {

        FPS.add(System.nanoTime() - previousFrameStart);

        Sprites.loadWaiting();
        GIFS.loadWaiting();

        previousFrameStart = System.nanoTime();


        //lock ensures that the simulation does not step!
        renderLock.lock();

        long renderStartTime = System.nanoTime();
        ScreenUtils.clear(Color.GREEN);

        gameWorld.render(camera);

        Vector2 origin = player.getBody().getPosition().cpy().add(previousFramePlayerSpeed);

        TargetCamera.updateCamera(origin, camera, gMap);
        previousFramePlayerSpeed = player.getBody().getLinearVelocity().cpy().scl(1f / Simulation.SET_UPS);
        //batch.setProjectionMatrix(camera.combined);

        // Save player position for further use

        batch.begin();

        AtomicInteger i = new AtomicInteger(1);

        Stream.concat(objects.stream(), actors.stream()).forEach(obj -> {
            if(i.getAndIncrement() % 100 == 0) batch.flush();
            obj.draw(batch, frameCount);
        });

        //HUDcamera.position.set(camera.position);
        //HUDcamera.update();
        if(!gameOver) {
            batch.setProjectionMatrix(HUDcamera.combined);
            //      XP bar and level
            long xpAmount = Simulation.EXP.get();

            // Level up
            if (xpAmount >= level * 100) {
                xpAmount -= (long) (level * 100);
                Simulation.EXP.set(xpAmount);
                level += 1;
                xpBar.setRange(0, level * 100);
                levelLabel.setText("Level: " + (int) level);
                player.setDamage(player.getDamage() + 2);
            }
            xpBar.setValue(xpAmount);
            xpBar.setPosition(HUDcamera.position.x - HUDcamera.viewportWidth / 2, HUDcamera.position.y + HUDcamera.viewportHeight / 2 - 2);
            levelLabel.setPosition(HUDcamera.position.x - HUDcamera.viewportWidth / 2, HUDcamera.position.y + HUDcamera.viewportHeight / 2 - 2);
            xpBar.draw(batch, 1);
            levelLabel.draw(batch, 1);

            // HP bar
            hpBar.setValue(player.getHP());
            hpBar.setPosition(HUDcamera.position.x - 4, HUDcamera.position.y - 8);
            hpBar.draw(batch, 1);

            // Clock
            int elapsedSeconds = (int) (frameCount / SET_FPS);

            int minutes = elapsedSeconds / 60;
            int seconds = elapsedSeconds % 60;
            timerLabel.setText(String.format("%02d:%02d", minutes, seconds));
            timerLabel.setPosition(HUDcamera.position.x - 5, HUDcamera.position.y + 27);
            timerLabel.draw(batch, 1);
        }

        batch.setProjectionMatrix(camera.combined);

        batch.setColor(Color.WHITE);

        if (SHOW_DEBUG_RENDER_INFO) {
            debugRenderer.render(world, camera.combined);

            // Performance statistics
            font.draw(batch, "FPS: " + String.format("%.1f", 1_000_000_000F/FPS.avg()), origin.x -500, origin.y -420);
            font.draw(batch, "UPS: " + String.format("%.1f",1_000_000_000F/UPS.avg()), origin.x -500, origin.y -440);
            font.draw(batch, "US/F: " + String.format("%.0f",FrameTime.avg()/1_000), origin.x -500, origin.y -460);
            font.draw(batch, "US/U: " + String.format("%.0f",UpdateTime.avg()/1_000), origin.x -500, origin.y -480);
        }

        if(gameOver) {
            font.getData().setScale(2);
            font.draw(batch, "GAME OVER",  camera.position.x, camera.position.y);
        }
        batch.end();

        frameCount++;
        synchronizer.incrementAndGet();
        renderLock.unlock();

        FrameTime.add(System.nanoTime() - renderStartTime);


        if(gameOver) {
            sim.stopSim();
            try {Thread.sleep(100 * 15);} catch (InterruptedException ignored) {}
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
    public void hide() {}

    @Override
    public void dispose() {}

    @Override
    public void show() {}

    public void gameOver() {
        gameOver = true;
    }

}
