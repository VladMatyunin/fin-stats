package ai.neptune.finstats.storage.segment;

import ai.neptune.finstats.storage.TotalStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SegmentTreeStatsStorageTest {

    private SegmentTreeStatsStorage statsStorage;

    @BeforeEach
    public void setUp() {
        statsStorage = new SegmentTreeStatsStorage(1000);
    }

    @Test
    public void testPushBatchAndTakeStats() {
        // Push a batch of data and check the stats
        float[] data = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
        statsStorage.pushBatch(data);

        TotalStats stats = statsStorage.takeStats(data.length);
        assertNotNull(stats, "TotalStats should not be null");
        assertEquals(5.0f, stats.getMax(), 0.001, "Max value mismatch");
        assertEquals(1.0f, stats.getMin(), 0.001, "Min value mismatch");
        assertEquals(3.0f, stats.getAvg(), 0.001, "Average value mismatch");
        assertEquals(2.0f, stats.getVariance(), 0.01, "Variance mismatch");
        assertEquals(5.0f, stats.getLast(), 0.001, "Last value mismatch");
    }

    @Test
    public void testPushBatchWithMoreThanMaxSize() {
        // Push more data than the segment tree max size and check the stats
        float[] data = new float[1000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }
        statsStorage.pushBatch(data);

        // The segment tree should only have the last `segmentTreeMaxSize` elements
        TotalStats stats = statsStorage.takeStats(statsStorage.segmentTreeMaxSize);
        assertNotNull(stats, "TotalStats should not be null");
        assertEquals(1000.0f, stats.getMax(), 0.001, "Max value mismatch");
        assertEquals(901.0f, stats.getMin(), 0.001, "Min value mismatch");
        assertEquals(950.5f, stats.getAvg(), 0.001, "Average value mismatch");
        assertEquals(83333.333f, stats.getVariance(), 0.01, "Variance mismatch");
        assertEquals(1000.0f, stats.getLast(), 0.001, "Last value mismatch");
    }

    @Test
    public void testCleanUpAfterExceedingMaxSize() {
        // Push data and check if the cleanup occurs
        float[] data = new float[2000];
        for (int i = 0; i < data.length; i++) {
            data[i] = i + 1;
        }
        statsStorage.pushBatch(data);

        // The segment tree should only have the last `segmentTreeMaxSize` elements
        TotalStats stats = statsStorage.takeStats(statsStorage.segmentTreeMaxSize);
        assertNotNull(stats, "TotalStats should not be null");
        assertEquals(2000.0f, stats.getMax(), 0.001, "Max value mismatch");
        assertEquals(1001.0f, stats.getMin(), 0.001, "Min value mismatch");
        assertEquals(1500.5f, stats.getAvg(), 0.001, "Average value mismatch");
        assertEquals(83333.25f, stats.getVariance(), 0.01, "Variance mismatch");
        assertEquals(2000.0f, stats.getLast(), 0.001, "Last value mismatch");
    }

    @Test
    public void testPushBatchAndTakeStatsForSubset() {
        // Push a batch of data and check stats for a subset
        float[] data = {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
        statsStorage.pushBatch(data);

        // Check stats for the first 5 elements
        TotalStats stats = statsStorage.takeStats(5);
        assertNotNull(stats, "TotalStats should not be null");
        assertEquals(7.0f, stats.getMax(), 0.001, "Max value mismatch");
        assertEquals(3.0f, stats.getMin(), 0.001, "Min value mismatch");
        assertEquals(5.0f, stats.getAvg(), 0.001, "Average value mismatch");
        assertEquals(2.0f, stats.getVariance(), 0.01, "Variance mismatch");
        assertEquals(7.0f, stats.getLast(), 0.001, "Last value mismatch");
    }
}