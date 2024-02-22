package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyEvent;
import InputProcessing.KeyStates;
import InputProcessing.MouseEvent;
import Simulation.EnemyContactListener;
import Simulation.SimulationThread;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
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
import static java.lang.Math.random;

public class GameContext extends Context{
    private SpriteBatch batch;
    private World world;
    private Body player;
    private Array<Body> enemies;
    private BitmapFont font;
    private Texture spriteImage, rectSprite;
    private Rectangle spriteRect, spriteRectEnemy;
    private ShapeRenderer shape;
    private RollingSum UpdateTime, FrameTime, FPS, UPS;
    private long previousFrameStart = System.nanoTime();
    private final Lock renderLock;
    private KeyStates keyStates;
    private final SimulationThread simThread;



    public GameContext(String name, SpriteBatch batch, ContextualInputProcessor iProc) {
        super(name, iProc);

        this.batch = batch;

        setupInputListener();
        setupDebug();

        enemies = new Array<>();

        float enemyScale = 0.3f;
        spriteImage = new Texture(Gdx.files.internal("obligator.png"));

        spriteRect = new Rectangle(1, 1, spriteImage.getWidth() / 2f, spriteImage.getHeight() / 2f);
        spriteRectEnemy = new Rectangle(1, 1, spriteImage.getWidth() / 2f * enemyScale, spriteImage.getHeight() / 2f * enemyScale);

        rectSprite = makeRectangle(spriteImage.getWidth() / 2, spriteImage.getHeight() / 2, 2,2,2,2, Color.GREEN, Color.CLEAR);

        font = new BitmapFont();
        font.setColor(Color.RED);

        this.shape = new ShapeRenderer();



        renderLock = new ReentrantLock(true);

        //create and start simulation
        world = createWorld();
        Set<Body> toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player, toBoKilled);
        world.setContactListener(contactListener);
        simThread = new SimulationThread(renderLock, keyStates, world, toBoKilled, UpdateTime, UPS, player);
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

        this.addAction(Input.Keys.W, KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.A, KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.S, KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.D, KeyEvent.KEYDOWN, keyStates::setInputKey);
        //this.addAction(Input.Keys.ESCAPE, KeyEvent.KEYDOWN, keyStates::setInputKey);
        this.addAction(Input.Keys.ESCAPE, KeyEvent.KEYDOWN, (x) -> {keyStates.setInputKey(Input.Keys.ESCAPE);System.exit(0);});

        this.addAction(Input.Keys.P, KeyEvent.KEYDOWN, (x) -> {
            this.getInputProcessor().setContext("EXAMPLE");
        });

        this.addAction(Input.Keys.W, KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.A, KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.S, KeyEvent.KEYUP, keyStates::unsetInputKey);
        this.addAction(Input.Keys.D, KeyEvent.KEYUP, keyStates::unsetInputKey);

        this.addAction(Input.Buttons.LEFT, MouseEvent.MOUSE_CLICKED, (x, y) -> System.out.println("CLICKED -> " + x + ", " + y));
        this.addAction(Input.Buttons.LEFT, MouseEvent.MOUSE_UNCLICKED, (x, y) -> System.out.println("DROPPED -> " + x + ", " + y));
        this.addAction(0, MouseEvent.MOUSE_DRAGGED, (x, y) -> System.out.println("DRAGGED -> " + x + ", " + y));
        this.addAction(0, MouseEvent.MOUSE_MOVED, (x, y) -> System.out.println("MOVED -> " + x + ", " + y));
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        FPS.add(System.nanoTime() - previousFrameStart);
        previousFrameStart = System.nanoTime();


        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderLock.lock();
        ScreenUtils.clear(Color.WHITE);


        long renderStartTime = System.nanoTime();

        Array<Body> tempE = new Array<>();
        world.getBodies(tempE);
        enemies.clear();
        for (Body b : tempE) {
            if (b == player) continue;
            enemies.add(b);
        }

        float radius = spriteRectEnemy.getWidth() / 2;
        spriteRect.setPosition(player.getPosition());
        Array<Vector2> enemiesPos = new Array<>(enemies.size);
        Array<Vector2> enemiesPosCirlce = new Array<>(enemies.size);
        Array<Vector2> enemiesCenter = new Array<>(enemies.size);
        Vector2 temp;
        for (Body e : enemies) {
            temp = new Vector2();
            temp.add(e.getPosition());
            temp.sub(radius, radius);
            enemiesPos.add(temp);
            enemiesPosCirlce.add(e.getPosition());
            enemiesCenter.add(e.getWorldCenter());
        }
        //spriteRectEnemy.setPosition(enemy.getPosition());
        Vector2 playerCenter = new Vector2().add(player.getWorldCenter());
        //Vector2 enemyCenter = new Vector2().add(enemy.getWorldCenter());

        batch.begin();

        font.draw(batch, "fps: " + (int)(1_000_000_000F/FPS.avg()), 10, 80);
        font.draw(batch, "ups: " + (int)(1_000_000_000F/UPS.avg()), 10, 60);
        font.draw(batch, "us/f: " + (int)(FrameTime.avg()/1_000), 10, 40);
        font.draw(batch, "us/u: " + (int)(UpdateTime.avg()/1_000), 10, 20);




        for (Vector2 v : enemiesPos) {
            batch.draw(spriteImage, v.x, v.y, spriteRectEnemy.width, spriteRectEnemy.height);
            //shape.circle(v.x, v.y, rectSpriteEnemy.getWidth());
            //batch.draw(rectSpriteEnemy, v.x, v.y, rectSpriteEnemy.getWidth(), rectSpriteEnemy.getHeight());
        }
        //draw player sprite
        batch.draw(spriteImage, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);
        //batch.draw(rectSprite, spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);



        batch.end();

        shape.setAutoShapeType(true);
        shape.begin();

        shape.setColor(Color.GREEN);
        shape.rect(spriteRect.x, spriteRect.y, spriteRect.width, spriteRect.height);

        shape.setColor(Color.RED);

        for (Vector2 v : enemiesPosCirlce) {
            shape.circle(v.x, v.y, radius);
        }
        shape.end();

        batch.begin();
        batch.draw(new Texture(2,2, Pixmap.Format.RGB888), playerCenter.x, playerCenter.y);

        for (Vector2 v : enemiesCenter) {
            batch.draw(new Texture(2,2, Pixmap.Format.RGB888), v.x, v.y);
        }

        batch.end();


        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);

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

        BodyDef playerBodyDef = new BodyDef();
        BodyDef enemyBodyDef = new BodyDef();
        //playerBodyDef.type = BodyDef.BodyType.KinematicBody; //we are not able to get the mass center of a Kinematic Body :(
        playerBodyDef.type = BodyDef.BodyType.DynamicBody;
        enemyBodyDef.type = BodyDef.BodyType.DynamicBody;
        playerBodyDef.position.set(20, 20);
        enemyBodyDef.position.set(1000, 1000);
        playerBodyDef.fixedRotation = true;
        enemyBodyDef.fixedRotation = true;

        Body body = world.createBody(playerBodyDef);


        player = body;


        PolygonShape squarePlayer = new PolygonShape();
        float width = spriteRect.getWidth();
        float height = spriteRect.getHeight();
        squarePlayer.set(new float[] {0,0, width,0, width,height, 0,height});



        //square.set(new float[] {0,height, width,height, width,0, 0,0});



        FixtureDef fixtureDefPlayer = new FixtureDef();
        fixtureDefPlayer.shape = squarePlayer;
        fixtureDefPlayer.density = 1f;
        fixtureDefPlayer.friction = 0;
        fixtureDefPlayer.restitution = 0;
        fixtureDefPlayer.isSensor = false;


        Fixture fixturePlayer = player.createFixture(fixtureDefPlayer);
        PolygonShape squareEnemy = new PolygonShape();
        float widthEnemy = spriteRectEnemy.getWidth();
        float heightEnemy = spriteRectEnemy.getHeight();
        squareEnemy.set(new float[] {0,0, widthEnemy,0, widthEnemy,heightEnemy, 0,heightEnemy});

        CircleShape circle = new CircleShape();


        circle.setRadius(widthEnemy / 2);

        FixtureDef fixtureDefEnemy = new FixtureDef();
        fixtureDefEnemy.shape = circle;
        fixtureDefEnemy.density = 1f;
        fixtureDefEnemy.friction = 0;
        fixtureDefEnemy.restitution = 0;
        fixtureDefEnemy.isSensor = false;


        enemies = new Array<>();
        Body enemy;
        for (int i = 0; i < 10; i++) {
            enemy = world.createBody(enemyBodyDef);
            enemy.createFixture(fixtureDefEnemy);
            enemies.add(enemy);
            enemy.setLinearVelocity((float) random(),(float) random()); ;
        }

        circle.dispose();
        squarePlayer.dispose();
        squareEnemy.dispose();

        world.step(1/60f, 10, 10);

        return world;
    }
}
