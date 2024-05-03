package Tools;

public class RollingSum {
    private long[] elements;
    private int head;
    private int n;
    private int length;

    private long sum;

    /**
     * Creates a rolling sum containing the sum of the last 'length' elements added to it.
     * @param length the number of elements to keep a sum of
     */
    public RollingSum(int length) {
        this.length = length;
        this.elements = new long[length];
        n = 0;
        head = 0;
    }

    /**
     * Add a number to the sum
     * @param num the number to add
     * @return the sum after the addition of 'num'
     */
    public long add(long num) {
        sum += num;
        if (n == length) {
            sum -= elements[head];
        } else {
            n++;
        }
        elements[head] = num;
        head = (head + 1) % length;
        return sum;
    }

    /**
     * Add a number and return the average after the number is added
     * @param num the number to add
     * @return average of the added numbers
     */
    public float avg(long num) {
        add(num);
        return avg();
    }

    /**
     * Get the average of the numbers added
     * @return the average of the added numbers
     */
    public float avg() {
        return sum / (float)n;
    }

    /**
     * @return the sum
     */
    public long getSum() {
        return sum;
    }

    public static int millisToFrames(float millis, float FPS) {
        return (int)(millis*FPS /1000);
    }

}
