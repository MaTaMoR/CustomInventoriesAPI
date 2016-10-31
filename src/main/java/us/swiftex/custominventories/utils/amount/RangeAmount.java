package us.swiftex.custominventories.utils.amount;

import java.util.concurrent.ThreadLocalRandom;

public class RangeAmount implements Amount {

    private final int min;
    private final int max;

    public RangeAmount(int a, int b) {
        if(a == b) {
            min = a;
            max = b;
        } else if(a < b) {
            min = a;
            max = b;
        } else {
            min = b;
            max = a;
        }
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public int getAmount() {
        return ThreadLocalRandom.current().nextInt(min, max);
    }
}
