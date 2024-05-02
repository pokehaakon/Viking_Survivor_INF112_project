package Camera;

import GameMap.GameMap;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.Camera;

import static Contexts.GameContext.zoomLevel;

public class TargetCamera {

    /**
     * sets camera to follow target. Camera stops when edge of the map is hit
     * @param target target which camera follows
     * @param cam OrthographicCamera
     * @param map gamemap
     */
    public static void updateCamera(Vector2 target, Camera cam, GameMap map) {
        float mapScale = map.getScale();
        TiledMapTileLayer layer = (TiledMapTileLayer) map
                .getTiledMap()
                .getLayers()
                .get(0);

        float mapHeight = layer.getHeight() * layer.getTileHeight() * mapScale;
        float mapWidth = layer.getWidth() * layer.getTileWidth() * mapScale;

        if(target.x < cam.viewportWidth / 2) cam.position.x = cam.viewportWidth / 2;
        else cam.position.x = Math.min(target.x, mapWidth - cam.viewportWidth / 2);

        //cam.position.x *= zoomLevel;

        if(target.y < cam.viewportHeight / 2) cam.position.y = cam.viewportHeight / 2;
        else cam.position.y = Math.min(target.y, mapHeight - cam.viewportHeight / 2);

        //cam.position.y *= zoomLevel;

        cam.update();
    }
}
