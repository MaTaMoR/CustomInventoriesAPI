package me.matamor.custominventories.utils.amount;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class RandomAmount implements Amount {

    private final List<Integer> values;

    public RandomAmount(Collection<Integer> values) {
        this.values = new ArrayList<>(values);
    }

    public Collection<Integer> getValues() {
        return Collections.unmodifiableCollection(values);
    }

    @Override
    public int getAmount() {
        if(values.isEmpty()) return 0;
        return values.get(ThreadLocalRandom.current().nextInt(values.size()));
    }

}
