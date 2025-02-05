package ai.neptune.finstats.storage.segment;

import ai.neptune.finstats.api.ApiConstants;
import ai.neptune.finstats.storage.SymbolStatsStorage;
import ai.neptune.finstats.storage.TotalStats;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * A storage based on Segment Tree data structure
 * On each batch add it recreates the Segment Tree containing the statistics
 * When the statistics is queried, the class simply queries the predefined segment tree
 * Expected complexity for statistics querying is O(log(n))
 * <p>
 * Further optimisations may include not rebuilding tree on each batch insert, but accumulate the stats
 * to a temporary storage and on predefined threshold rebuild the tree
 */
public class SegmentTreeStatsStorage implements SymbolStatsStorage {

    final int segmentTreeMaxSize;

    public SegmentTreeStatsStorage(int maxSize) {
        this.segmentTreeMaxSize = maxSize;
    }

    public SegmentTreeStatsStorage() {
        this.segmentTreeMaxSize = (int) Math.pow(10, ApiConstants.K_MAX_EXPONENT);
    }

    private SegmentTree segmentTree;
    private Deque<Float> dataPoints = new ArrayDeque<>();

    public void pushBatch(float[] data) {
        for (float item : data) {
            dataPoints.addFirst(item);
        }
        updateSegmentTree();
    }

    private void updateSegmentTree() {
        cleanUp();
        var segmentArray = new float[Math.min(segmentTreeMaxSize, dataPoints.size())];
        int i = 0;
        for (float point : dataPoints) {
            segmentArray[i++] = point;
            if (i >= segmentTreeMaxSize) break;
        }
        segmentTree = new SegmentTree(segmentArray);
    }

    public TotalStats takeStats(int to) {
        if (segmentTree == null || dataPoints.isEmpty() || to <= 0) {
            return TotalStats.empty();
        }
        Stats segmentStats = segmentTree.rangeStats(0, Math.min(to - 1, dataPoints.size() - 1));
        return TotalStats.builder()
                .max(segmentStats.getMax())
                .min(segmentStats.getMin())
                .avg(segmentStats.getAvg())
                .last(dataPoints.peekFirst())
                .variance(segmentStats.getVariance())
                .build();
    }

    private void cleanUp() {
        while (dataPoints.size() > segmentTreeMaxSize) {
            dataPoints.pollLast();
        }
    }
}
