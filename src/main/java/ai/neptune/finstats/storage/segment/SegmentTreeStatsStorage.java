package ai.neptune.finstats.storage.segment;

import ai.neptune.finstats.storage.SymbolStatsStorage;
import ai.neptune.finstats.storage.TotalStats;

import java.util.ArrayDeque;
import java.util.Deque;

public class SegmentTreeStatsStorage implements SymbolStatsStorage {

    Deque<SegmentTreeHolder> segmentTrees = new ArrayDeque<>();

    Float lastElement;

    public void pushBatch(float[] data) {
        lastElement = data[data.length - 1];
        segmentTrees.addFirst(new SegmentTreeHolder(data));
    }

    public TotalStats takeStats(int to) {
        var iterator = segmentTrees.iterator();
        var isReachedBound = false;
        var currentPoint = 0;
        Stats totalStats = Stats.empty();
        while (iterator.hasNext() && !isReachedBound) {
            var nextTree = iterator.next();
            if (currentPoint + nextTree.numOfItems < to) {
                totalStats = totalStats.merge(nextTree.tree.getTotal());
                currentPoint += nextTree.numOfItems;
            } else {
                var rangedStat = nextTree.tree.rangeStats(currentPoint + nextTree.numOfItems - to, nextTree.numOfItems);
                totalStats = totalStats.merge(rangedStat);
                isReachedBound = true;
            }
        }
        return TotalStats.builder()
                .max(totalStats.getMax())
                .min(totalStats.getMin())
                .avg(totalStats.getAvg())
                .last(lastElement)
                .variance(totalStats.variance)
                .build();
    }
}
