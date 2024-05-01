package Simulation;

import GameObjects.Actor;
import GameObjects.GameObject;
import GameObjects.ObjectFactory;
import Parsing.MapParser;
import Parsing.ObjectDefineParser.Defines.ActorDefinition;
import Parsing.ObjectDefineParser.Defines.ObjectDefinition;
import Parsing.ObjectDefineParser.ObjectDefineParser;
import Parsing.SpawnFrame;
import Simulation.SpawnHandler.SpawnHandlerFactory;
import Tools.Pool.ObjectPool;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Disposable;
import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameWorld implements Disposable {
    private Map<String, String> defines;
    private List<Pair<Long, List<SpawnFrame>>> timeFrames;
    private TiledMapRenderer mapRenderer;
    private final TiledMap map;
    private final float mapScale = 4f;
    private long nextFrame;
    private int frameIndex = 0;
    private final List<ISpawnHandler> spawnHandlers = new ArrayList<>();

    private final SpawnHandlerFactory handlerFactory;

//    private final ObjectPool<Actor> actorPool;
//    private final ObjectPool<GameObject> objectPool;
//
//    private final List<Actor> activeActors;
//    private final List<GameObject> activeObjects;
    public final Actor player;

    public GameWorld(
            String worldDef,
            ObjectPool<Actor> actorPool,
            ObjectPool<GameObject> objectPool,
            List<Actor> activeActors,
            List<GameObject> activeObjects
    ) {
        if (!worldDef.endsWith(".wdef")) {
            throw new RuntimeException("world definition file needs ending '.wdef', got : " + worldDef);
        }

//        this.actorPool = actorPool;
//        this.objectPool = objectPool;
//        this.activeActors = activeActors;
//        this.activeObjects = activeObjects;

        MapParser mapParser = new MapParser(worldDef);

        var includes = mapParser.doParseIncludes();
        for (String include : includes) {
            var objectDefineParser = new ObjectDefineParser(include);
            objectDefineParser.parseDocument();

            for (var entry : objectDefineParser.variables.entrySet()) {
                if (entry.getValue().get() instanceof ActorDefinition actorDefinition) {
                    ObjectFactory.register(entry.getKey().substring(1), actorDefinition);
                }
                if (entry.getValue().get() instanceof ObjectDefinition objectDefinition) {
                    ObjectFactory.register(entry.getKey().substring(1), objectDefinition);
                }
            }
        }

        defines = mapParser.doParseDefines();
        timeFrames = mapParser.doParseTimeFrames();

        map = new TmxMapLoader().load(defines.get("MAP_PATH"));
        mapRenderer = new OrthogonalTiledMapRenderer(map, mapScale);

        if (timeFrames != null) {
            nextFrame = timeFrames.get(0).getValue0();
        } else {
            nextFrame = Long.MAX_VALUE;
        }

        this.player = ObjectFactory.createActor(defines.get("PLAYER_NAME"));

        handlerFactory = new SpawnHandlerFactory(player, actorPool, objectPool, activeActors, activeObjects);
    }
    public void act(Long frame) {
        if (frame == nextFrame) {
            setNextFrame(frame);
        }
        actThisFrame(frame);
    }

    private void actThisFrame(long frame) {
        for (ISpawnHandler handler : spawnHandlers) {
            handler.act(frame);
        }
    }

    private void setNextFrame(long frame) {
        spawnHandlers.clear();

        //System.out.println("Frame: " + frameIndex);
        for (var f : timeFrames.get(frameIndex++).getValue1()) {
            spawnHandlers.add(handlerFactory.create(f.spawnable(), f.spawnType(), f.args()));
            //System.out.println("\t" + f);
        }

        nextFrame = timeFrames.size() == frameIndex ? Long.MAX_VALUE : timeFrames.get(frameIndex).getValue0();
    }

    public void render(OrthographicCamera camera, float delta) {
        mapRenderer.setView(camera);
        mapRenderer.render();
    }

    @Override
    public void dispose() {
        map.dispose();
    }
}
