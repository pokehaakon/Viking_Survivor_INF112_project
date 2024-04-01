package Actors.Enemy;

import Actors.Actor;
import Actors.Stats.EnemyStats;
import Tools.FilterTool;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

import static Tools.BodyTool.createBody;
import static Tools.FilterTool.createFilter;
import static Tools.ShapeTools.createSquareShape;

public class Enemy extends Actor{

    public float knockBackResistance;

    private EnemyStats stats;


    public Enemy(Body body, String spawnGIF, float scale, EnemyStats stats) {
        super(body, spawnGIF, scale);
        this.stats = stats;

        // stats
        HP = stats.HP();
        speed = stats.speed();
        damage = stats.damage();
        armour = stats.armour();
        knockBackResistance = stats.knockBackResistance();

        velocityVector = new Vector2();

    }



    public void setPosition(Vector2 pos) {
        body.setTransform(pos, body.getAngle());
    }














}
