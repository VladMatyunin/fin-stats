package ai.neptune.finstats.storage.segment;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Stats {

    float sum;

    float max;

    float min;

    float avg;

    float variance;

    int numOfItems;

    public Stats merge(Stats another) {
        return builder()
                .max(Math.max(max, another.max))
                .min(Math.min(min, another.min))
                .sum(sum + another.sum)
                .avg((sum + another.sum) / (numOfItems + another.numOfItems))
                .variance(combineVariance(another))
                .numOfItems(numOfItems + another.numOfItems)
                .build();
    }

    public static Stats empty() {
        return builder()
                .max(Float.MIN_VALUE)
                .min(Float.MAX_VALUE)
                .sum(0)
                .avg(0)
                .variance(0)
                .numOfItems(0)
                .build();
    }

    public static Stats fromValue(float val) {
        return builder()
                .max(val)
                .min(val)
                .sum(val)
                .avg(val)
                .variance(0)
                .numOfItems(1)
                .build();
    }

    private float combineVariance(Stats another) {
        var totalNum = numOfItems + another.numOfItems;
        var weightVar = (variance * numOfItems + another.variance * another.numOfItems) / totalNum;
        var meanDiff = Math.pow(avg - another.avg, 2);
        var meanDiffComponent = (numOfItems * another.numOfItems * meanDiff) / Math.pow(totalNum , 2);
        return (float) (weightVar + meanDiffComponent);
    }
}
