package Tools;

import org.junit.jupiter.api.Test;
import org.javatuples.*;


import java.util.List;

import static Tools.Tuple.zip;
import static org.junit.jupiter.api.Assertions.*;

class TupleTest {
    @Test
    void testTupleTypes() {
        var t1 = Tuple.of(1);
        var t2 = Tuple.of(1, 2);
        var t3 = Tuple.of(1, 2, 3);
        var t4 = Tuple.of(1, 2, 3, 4);
        var t5 = Tuple.of(1, 2, 3, 4, 5);
        var t6 = Tuple.of(1, 2, 3, 4, 5, 6);
        var t7 = Tuple.of(1, 2, 3, 4, 5, 6, 7);
        var t8 = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8);
        var t9 = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9);
        var t0 = Tuple.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        assertInstanceOf(Unit.class, t1);
        assertInstanceOf(Pair.class, t2);
        assertInstanceOf(Triplet.class, t3);
        assertInstanceOf(Quartet.class, t4);
        assertInstanceOf(Quintet.class, t5);
        assertInstanceOf(Sextet.class, t6);
        assertInstanceOf(Septet.class, t7);
        assertInstanceOf(Octet.class, t8);
        assertInstanceOf(Ennead.class, t9);
        assertInstanceOf(Decade.class, t0);

    }

    @Test
    void testZippedIterator() {
        var a = List.of(1,2,3);
        var b = List.of('x', 'y');
        int i = 0;

        for (var unit : zip(a)) {
            assertEquals(a.get(i), unit.getValue0());
            i++;
        }
        assertEquals(3, i);

        i = 0;
        for (var pair : zip(a, b)) {
            assertEquals(a.get(i), pair.getValue0());
            assertEquals(b.get(i), pair.getValue1());

            i++;
        }

        assertEquals(2, i);


    }
}