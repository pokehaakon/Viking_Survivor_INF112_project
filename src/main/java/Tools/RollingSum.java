package Tools;

public class RollingSum {
    private long[] elements;
    private int head;
    private int n;
    private int length;

    private long sum;

    public RollingSum(int length) {
        this.length = length;
        this.elements = new long[length];
        n = 0;
        head = 0;
    }

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

    public float avg(long num) {
        add(num);
        return avg();
    }

    public float avg() {
        return sum / (float)n;
    }


}
