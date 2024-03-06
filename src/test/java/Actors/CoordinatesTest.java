package Actors;

import com.badlogic.gdx.math.Vector2;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CoordinatesTest {




    @Test
    void random() {
    }


    @Test
    void squareSwarmSize() {
        for(int i = 0; i < 10;i++) {
            int randomNumMembers = new Random(123).nextInt(30);
            int sideLength = (int) (Math.ceil(Math.sqrt(randomNumMembers)));
            assertEquals(Math.pow(sideLength, 2), Coordinates.squareSwarm(randomNumMembers, new Vector2(0,0),1).size());
        }
    }

    @ParameterizedTest
    @MethodSource("squareSwarmData")
    void squareSwarm(int size, List<Vector2> expected) {
        //when the root of squareMembers is rational
        List<Vector2> swarm = Coordinates.squareSwarm(size, new Vector2(0, 0),1);

        for (int i = 0; i < swarm.size(); i++) {
            assertEquals(expected.get(i), swarm.get(i));
        }
    }
    @ParameterizedTest
    @MethodSource("notSquareSwarmData")
    void notSquareSwarm(int size, List<Vector2> expected, int spacing) {
        //when the root of swarm members is irrational
        List<Vector2> swarm = Coordinates.squareSwarm(size, new Vector2(0, 0),1);

        for (int i = 0; i < swarm.size(); i++) {
            assertEquals(expected.get(i), swarm.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("differSpacing")
    void differSpacing(int size, List<Vector2> expected, int spacing) {
        List<Vector2> swarm = Coordinates.squareSwarm(size, new Vector2(0, 0),spacing);
        for (int i = 0; i < swarm.size(); i++) {
            assertEquals(expected.get(i), swarm.get(i));
        }
    }

    @ParameterizedTest
    @MethodSource("lineSwarm")
    void lineSwarm() {

    }


    private static List<Object[]> squareSwarmData() {
        return Arrays.asList(
                new Object[]{4, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(1, 0),
                        new Vector2(0, 1),
                        new Vector2(1, 1)
                )},
                new Object[]{9, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(1, 0),
                        new Vector2(2, 0),
                        new Vector2(0, 1),
                        new Vector2(1, 1),
                        new Vector2(2, 1),
                        new Vector2(0, 2),
                        new Vector2(1, 2),
                        new Vector2(2, 2)
                )}
        );
    }

    private static List<Object[]> notSquareSwarmData() {
        return Arrays.asList(
                new Object[]{5, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(1, 0),
                        new Vector2(2, 0),
                        new Vector2(0, 1),
                        new Vector2(1,1),
                        new Vector2(2,1),
                        new Vector2(0,2),
                        new Vector2(1,2),
                        new Vector2(2,2)


                ),1},
                new Object[]{7, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(1, 0),
                        new Vector2(2, 0),
                        new Vector2(0, 1),
                        new Vector2(1, 1),
                        new Vector2(2, 1),
                        new Vector2(0, 2),
                        new Vector2(1, 2),
                        new Vector2(2, 2)
                ),1}
        );
    }

    private static List<Object[]> differSpacing() {
        return Arrays.asList(
                //spacing 2
                new Object[]{4, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(2, 0),
                        new Vector2(0, 2),
                        new Vector2(2, 2)
                ), 2},
                //spacing 3
                new Object[]{4, Arrays.asList(
                        new Vector2(0, 0),
                        new Vector2(3, 0),
                        new Vector2(0, 3),
                        new Vector2(3, 3)

                ), 3},
                //spacing 5
            new Object[]{3, Arrays.asList(
                    new Vector2(0, 0),
                    new Vector2(5, 0),
                    new Vector2(0, 5),
                    new Vector2(5, 5)

            ), 5}
        );
    }



}