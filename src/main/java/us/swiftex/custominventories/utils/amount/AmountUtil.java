package us.swiftex.custominventories.utils.amount;

import us.swiftex.custominventories.utils.CastUtils;
import us.swiftex.custominventories.utils.CastUtils.FormatException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class AmountUtil {

    private AmountUtil() {

    }

    public static String serialize(Amount amount) {
        if(amount instanceof RandomAmount) {
            RandomAmount rangeAmount = (RandomAmount) amount;

            StringBuilder stringBuilder = new StringBuilder();
            Iterator<Integer> iterator = rangeAmount.getValues().iterator();

            while (iterator.hasNext()) {
                stringBuilder.append(iterator.next());
                if(iterator.hasNext()) stringBuilder.append(",");
            }

            return stringBuilder.toString();
        } else if(amount instanceof RangeAmount) {
            RangeAmount rangeAmount = (RangeAmount) amount;
            return rangeAmount.getMin() + "~" + rangeAmount.getMax();
        } else {
            return String.valueOf(amount.getAmount());
        }
    }

    public static Amount deserialize(String serialized) {
        try {
            if(serialized.contains(",")) {
                String[] split = serialized.split(",");

                if(split.length > 1) {
                    List<Integer> values = new ArrayList<>();
                    for (String value : split) values.add(CastUtils.asInt(value));

                    return new RandomAmount(values);
                } else {
                    throw new FormatException("The serialized amount (" + serialized + ") must have more than one value to be RandomAmount");
                }
            } else if(serialized.contains("~")) {
                String[] split = serialized.split("~");

                if(split.length == 2) {
                    return new RangeAmount(CastUtils.asInt(split[0]), CastUtils.asInt(split[1]));
                } else {
                    throw new FormatException("The serialized amount (" + serialized + ") must have two values to be RangeAmount");
                }
            } else {
                return new SimpleAmount(CastUtils.asInt(serialized));
            }
        } catch (FormatException e) {
            throw new RuntimeException("The was an error while deserializing Amount (" + serialized + ")", e);
        }
    }
}
