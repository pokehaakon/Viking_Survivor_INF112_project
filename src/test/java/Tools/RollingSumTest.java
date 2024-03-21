package Tools;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class RollingSumTest {

    @Test
    void add() {
        RollingSum r = new RollingSum(10);
        for (int i = 0; i < 9; i ++) {
            r.add(i);
        }
        assertEquals(36, r.getSum());
        assertEquals(36/9f, r.avg());
        assertEquals(45/10f, r.avg(9));
        assertEquals(45, r.getSum());
        assertEquals(55/10f, r.avg(10));

    }

    @Test
    void avg() {
    }

    @Test
    void testAvg() {
    }
}