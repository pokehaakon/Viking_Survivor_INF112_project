package GameMap;

import Tools.BodyTool;
import Tools.ExcludeFromGeneratedCoverage;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.World;

import static Tools.FilterTool.createFilter;

public class GameMap {

    private TiledMap map;
    private float scale;

    public GameMap(String filePath, float mapScale) {
        this.map = new TmxMapLoader().load(filePath);
        this.scale = mapScale;
    }

    /**
     * method for rendering the TiledMap
     * @param cam OrthographicCamera object
     */
    @ExcludeFromGeneratedCoverage
    public void renderTiledMap(OrthographicCamera cam) {
        TiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(map, scale);
        mapRenderer.setView(cam);
        mapRenderer.render();
    }

    /**
     * method for getting hold of the TiledMap
     * @return gamemap
     */
    public TiledMap getTiledMap() {
        return map;
    }

    /**
     * gets the Vector2 position of the middle of the map
     * @return Vector2 location representing the middle of the gamemap
     */
    public Vector2 getMiddleOfMapPosition() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        int tileWidth = layer.getTileWidth(); // width of a tile in pixels
        int tileHeight = layer.getTileHeight(); // height of a tile in pixels
        int mapWidth = layer.getWidth(); // width of the tilemap in tiles
        int mapHeight = layer.getHeight(); // height of the tilemap in tiles

        return new Vector2(mapWidth * tileWidth * scale / 2f, mapHeight * tileHeight * scale / 2f);
    }

    private float[] createPolyLine(PolygonMapObject polygon) {
        //for(float juice : polygon.getPolygon().getTransformedVertices()) System.out.println(juice);
        float[] points = polygon.getPolygon().getTransformedVertices();
        float[] newPoints = new float[points.length + 2];
        newPoints[0] = 0.0f;
        newPoints[1] = 0.0f;

        for (int i = 0; i < points.length; i++) {
            newPoints[i + 2] = points[i] * scale;
        }
        return newPoints;
    }

    /**
     * creates a line around the map, preventing the player from moving out of bounds
     * @param world 
     */
    public void createMapBorder(World world) {
        MapLayer layer = map.getLayers().get("area");

        for(MapObject object : layer.getObjects()) {

            // create the shape
            ChainShape shape = new ChainShape();
            shape.createChain(createPolyLine((PolygonMapObject) object));

            BodyTool.createBody(
                    world,
                    new Vector2(),
                    shape,
                    createFilter(
                            FilterTool.Category.WALL,
                            new FilterTool.Category[] {
                                    FilterTool.Category.PLAYER
                            }
                    ),
                    1f,
                    0f,
                    0f,
                    false,
                    BodyDef.BodyType.StaticBody
            );
        }
    }

    /**
     * gets the scale of the map
     * @return mapscale
     */
    public float getScale() {
        return scale;
    }

    /**
     * disposes the tiledMap
     */
    public void dispose() {
        getTiledMap().dispose();
    }
}
