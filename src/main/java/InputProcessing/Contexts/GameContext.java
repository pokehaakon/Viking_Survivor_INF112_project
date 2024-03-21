package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import InputProcessing.KeyStates;
import Simulation.EnemyContactListener;
//import Simulation.SimulationThread;
import Tools.FilterTool;
import Tools.RollingSum;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
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
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static Rendering.Shapes.makeRectangle;
import static Tools.BodyTool.createBodies;
import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.*;
import static java.lang.Math.random;

public class GameContext extends Context {
    private final SpriteBatch batch;
    private final Camera camera;
    private final World world;
    private Body player;
    private Array<Body> enemies;
    private final BitmapFont font;
    final private Texture spriteImage, rectSprite;
    final private Rectangle spriteRect, spriteRectEnemy;
    final private ShapeRenderer shape;
    private RollingSum UpdateTime, FrameTime, FPS, UPS;
    private long previousFrameStart = System.nanoTime();
    private final Lock renderLock;
    private KeyStates keyStates;
    //private final SimulationThread simThread;
    private float zoomLevel = 1f;
    private long frameCount = 0;
    private static boolean SHOW_DEBUG_RENDER_INFO = true;


    public GameContext(String name, SpriteBatch batch, Camera camera, ContextualInputProcessor iProc) {
        super(name, iProc);
        this.setInputProcessor(createInputProcessor());

        this.batch = batch;
        this.camera = camera;

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
        world = createWorld();
        Set<Body> toBoKilled = new HashSet<>();
        ContactListener contactListener = new EnemyContactListener(world, player, toBoKilled);
        world.setContactListener(contactListener);
        //simThread = new SimulationThread(renderLock, keyStates, world, toBoKilled, UpdateTime, UPS, player);
        //simThread.start();

    }
    private void setupDebug() {
        UpdateTime = new RollingSum(60*3);
        FrameTime = new RollingSum(60*3);
        FPS = new RollingSum(60 * 3);
        UPS = new RollingSum(60 * 3);
    }


    private  InputProcessor createInputProcessor() {
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

        Vector2 playerPos = player.getPosition().cpy();


        float radius = spriteRectEnemy.getWidth() / 2;


        Vector2 origin;
        //origin = player.getWorldCenter().cpy();
        origin = playerPos.cpy();
        origin.add(getBottomLeftCorrection(player.getFixtureList().get(0).getShape()));

        //origin = player.getPosition().cpy();
        //center camera at player
        camera.position.x = origin.x;
        camera.position.y = origin.y;
        camera.position.z = 0;
        camera.update(true);





        batch.begin();
        Array<Vector2> enemiesCenter = new Array<>(enemies.size);
        drawEnemies(enemiesCenter);

        //draw player sprite

        Vector2 correctionVector = player.getLinearVelocity().cpy();
        //correctionVector.scl(1f/simThread.SET_UPS);

        //for some reason the sprite batch renders "last" frame...
        batch.draw(
                spriteImage,
                playerPos.x - correctionVector.x,
                playerPos.y - correctionVector.y,
                spriteRect.width,
                spriteRect.height
        );


        batch.end();

        if (SHOW_DEBUG_RENDER_INFO) {
            drawDebug(origin, radius, enemiesCenter);
        }

        renderLock.unlock();
        FrameTime.add(System.nanoTime() - renderStartTime);
        frameCount++;
    }
    private void drawEnemies(Array<Vector2> enemiesCenter) {
        Array<Body> tempE = new Array<>();
        world.getBodies(tempE);
        enemies.clear();
        for (Body b : tempE) {
            if (b == player) continue;
            enemies.add(b);
        }

        float radius = spriteRectEnemy.getWidth() / 2;

        Array<Vector2> enemiesPos = new Array<>(enemies.size);
        Vector2 temp;
        for (Body e : enemies) {
            temp = new Vector2();
            temp.add(e.getPosition());
            temp.sub(radius, radius);
            enemiesPos.add(temp);
            enemiesCenter.add(e.getWorldCenter());
        }

        for (Vector2 v : enemiesPos) {
            batch.draw(spriteImage, v.x, v.y, spriteRectEnemy.width, spriteRectEnemy.height);
            //shape.circle(v.x, v.y, rectSpriteEnemy.getWidth());
            //batch.draw(rectSpriteEnemy, v.x, v.y, rectSpriteEnemy.getWidth(), rectSpriteEnemy.getHeight());
        }
    }

    private void drawDebug(Vector2 origin, float radius, Array<Vector2> enemiesCenter) {
        shape.setAutoShapeType(true);
        shape.begin();


        float xc = camera.viewportWidth / zoomLevel / 2f - origin.x / zoomLevel;
        float yc = camera.viewportHeight / zoomLevel / 2f - origin.y / zoomLevel;

        shape.setColor(Color.GREEN);
        //shape.rect(spriteRect.x / zoomLevel + xc, spriteRect.y / zoomLevel + yc, spriteRect.width / zoomLevel, spriteRect.height / zoomLevel);

        PolygonShape p = (PolygonShape) player.getFixtureList().get(0).getShape();
        float [] points = new float[2*p.getVertexCount()];
        Vector2 temp2 = new Vector2();
        Vector2 playerPositionCorrection = getBottomLeftCorrection(p);
        for(int i = 0; i < points.length; i += 2) {
            p.getVertex(i/2, temp2);
//            points[i] = temp2.x / zoomLevel + player.getPosition().x / zoomLevel + xc;
//            points[i+1] = temp2.y / zoomLevel + player.getPosition().y / zoomLevel + yc;

            temp2.sub(playerPositionCorrection).add(origin).scl(1/zoomLevel);
            points[i] = temp2.x + xc;
            points[i+1] = temp2.y + yc;
        }

        shape.polygon(points);

        shape.setColor(Color.RED);

        for (Vector2 v : enemiesCenter) {
            shape.circle(v.x / zoomLevel + xc, v.y / zoomLevel + yc, radius / zoomLevel);
        }
        shape.end();

        batch.begin();
        batch.draw(new Texture(5,5, Pixmap.Format.RGB888), 0f, 0f);
        batch.draw(new Texture(1,1, Pixmap.Format.RGB888), origin.x, origin.y);
//        batch.draw(
//                new Texture(2,2, Pixmap.Format.RGB888),
//                player.getPosition().x + playerPositionCorrection.x,
//                player.getPosition().y + playerPositionCorrection.y
//        );

        for (Vector2 v : enemiesCenter) {
            batch.draw(new Texture(1,1, Pixmap.Format.RGB888), v.x, v.y);
        }

        font.draw(batch, "fps: " + String.format("%.1f", 1_000_000_000F/FPS.avg()), 10, 80);
        font.draw(batch, "ups: " + String.format("%.1f",1_000_000_000F/UPS.avg()), 10, 60);
        font.draw(batch, "us/f: " + String.format("%.0f",FrameTime.avg()/1_000), 10, 40);
        font.draw(batch, "us/u: " + String.format("%.0f",UpdateTime.avg()/1_000), 10, 20);

        batch.end();

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        renderLock.lock();
        //simThread.pause();
        renderLock.unlock();
    }

    @Override
    public void resume() {
        //simThread.unpause();
        //synchronized (simThread) {
        //    simThread.notify();
        //}
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

        PolygonShape squarePlayer = createSquareShape(
                spriteRect.getWidth(),
                spriteRect.getHeight()
        );
        CircleShape circle = createCircleShape(spriteRectEnemy.getWidth() / 2);



        player = createBody(
                world,
                new Vector2(0,0),
                squarePlayer,
                createFilter(
                        FilterTool.Category.PLAYER,
                        new FilterTool.Category[]{
                                FilterTool.Category.ENEMY,
                                FilterTool.Category.WALL
                        }
                ),
                1,
                0,
                0
        );

        Filter enemyFilter = createFilter(
                FilterTool.Category.ENEMY,
                new FilterTool.Category[]{
                        FilterTool.Category.WALL,
                        FilterTool.Category.ENEMY,
                        FilterTool.Category.PLAYER
                }
        );
        int enemiesToSpawn = 10;
        enemies = createBodies(enemiesToSpawn, world, new Iterable<Vector2>() {
            @Override
            public Iterator<Vector2> iterator() {
                return new Iterator<Vector2>() {
                    @Override
                    public boolean hasNext() {return true;}

                    @Override
                    public Vector2 next() {return new Vector2((float) (1000f + random() - 0.5f), (float) (1000f + random() - 0.5f));}
                };
            }
        }, circle, enemyFilter, 1, 0, 0, false);

        circle.dispose();
        squarePlayer.dispose();


        return world;
    }
}
