package ai.neptune.finstats.storage.segment;

import ai.neptune.finstats.api.ApiConstants;
import ai.neptune.finstats.storage.SymbolStatsStorage;
import ai.neptune.finstats.storage.TotalStats;

import java.util.*;
import java.util.stream.IntStream;

public class SegmentTreeStatsStorage implements SymbolStatsStorage {

    private final int segmentTreeMaxSize = (int) Math.pow(10, ApiConstants.K_MAX_EXPONENT);

    private SegmentTree segmentTree;

    Deque<Float> dataPoints = new ArrayDeque<>();

    public void pushBatch(float[] data) {
        for (var item: data) {
            dataPoints.addFirst(item);
        }
        var pointsIterator = dataPoints.iterator();
        var currentPoint = 0;
        var segmentTreeSize = Math.min(segmentTreeMaxSize, dataPoints.size());
        var segmentArray = new float[segmentTreeSize];
        while (pointsIterator.hasNext() && currentPoint < segmentTreeMaxSize) {
            segmentArray[currentPoint] = pointsIterator.next();
            currentPoint++;
        }
        segmentTree = new SegmentTree(segmentArray);
    }

    public TotalStats takeStats(int to) {
        if (segmentTree == null || dataPoints.isEmpty()) {
            return TotalStats.empty();
        }
        var segmentStats = segmentTree.rangeStats(0, to - 1);
        return TotalStats.builder()
                .max(segmentStats.getMax())
                .min(segmentStats.getMin())
                .avg(segmentStats.getAvg())
                .last(dataPoints.peekFirst())
                .variance(segmentStats.variance)
                .build();
    }

    private void cleanUp() {
        if (dataPoints.size() > segmentTreeMaxSize) {
            var itemsToRemove = segmentTreeMaxSize - dataPoints.size();
            IntStream.range(0, itemsToRemove).forEach(i -> dataPoints.pollLast());
        }
    }
}
