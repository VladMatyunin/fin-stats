package ai.neptune.finstats.storage.segment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class SegmentTreeTest {
    private SegmentTree segmentTree;
    private float[] largeRandomArray;
    private static final int LARGE_ARRAY_SIZE = 1000;  // Adjust the size as needed for testing

    @BeforeEach
    public void setUp() {
        // Initialize with a large random array
        largeRandomArray = generateRandomArray(LARGE_ARRAY_SIZE);
        segmentTree = new SegmentTree(largeRandomArray);
    }

    @Test
    public void testTotalStatsWithLargeArray() {
        // Calculating expected total stats for the entire random array
        Stats expectedStats = calculateExpectedStats(largeRandomArray, 0, LARGE_ARRAY_SIZE - 1);

        Stats totalStats = segmentTree.getTotal();
        assertEquals(expectedStats.getMin(), totalStats.getMin(), 0.001, "Total min value mismatch");
        assertEquals(expectedStats.getMax(), totalStats.getMax(), 0.001, "Total max value mismatch");
        assertEquals(expectedStats.getAvg(), totalStats.getAvg(), 0.001, "Total average value mismatch");
        assertEquals(expectedStats.getVariance(), totalStats.getVariance(), 0.01, "Total variance mismatch");
    }

    @Test
    public void testRandomRangeStatsInLargeArray() {
        // Testing random range queries within the large array
        Random random = new Random();
        int start = random.nextInt(LARGE_ARRAY_SIZE / 2);
        int end = start + random.nextInt(LARGE_ARRAY_SIZE - start);

        Stats expectedStats = calculateExpectedStats(largeRandomArray, start, end);
        Stats rangeStats = segmentTree.rangeStats(start, end);

        assertEquals(expectedStats.getMin(), rangeStats.getMin(), 0.001, "Range min value mismatch");
        assertEquals(expectedStats.getMax(), rangeStats.getMax(), 0.001, "Range max value mismatch");
        assertEquals(expectedStats.getAvg(), rangeStats.getAvg(), 0.001, "Range average value mismatch");
        assertEquals(expectedStats.getVariance(), rangeStats.getVariance(), 0.01, "Range variance mismatch");
    }

    @Test
    public void testEdgeCasesWithLargeArray() {
        // Edge case: First and last elements
        Stats firstElementStats = segmentTree.rangeStats(0, 0);
        Stats lastElementStats = segmentTree.rangeStats(LARGE_ARRAY_SIZE - 1, LARGE_ARRAY_SIZE - 1);

        assertNotNull(firstElementStats, "First element stats should not be null");
        assertNotNull(lastElementStats, "Last element stats should not be null");

        assertEquals(firstElementStats.getMin(), firstElementStats.getMax(), "First element min/max mismatch");
        assertEquals(lastElementStats.getMin(), lastElementStats.getMax(), "Last element min/max mismatch");
        assertEquals(0.0f, firstElementStats.getVariance(), 0.001, "First element variance mismatch");
        assertEquals(0.0f, lastElementStats.getVariance(), 0.001, "Last element variance mismatch");
    }

    // Utility method to generate a random array of floats
    private float[] generateRandomArray(int size) {
        float[] array = new float[size];
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            array[i] = random.nextFloat() * 100; // Random float between 0 and 100
        }
        return array;
    }

    // Utility method to calculate expected stats for verification
    private Stats calculateExpectedStats(float[] array, int start, int end) {
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        float sum = 0.0f;
        for (int i = start; i <= end; i++) {
            min = Math.min(min, array[i]);
            max = Math.max(max, array[i]);
            sum += array[i];
        }
        float avg = sum / (end - start + 1);

        float varianceSum = 0.0f;
        for (int i = start; i <= end; i++) {
            varianceSum += Math.pow(array[i] - avg, 2);
        }
        float variance = varianceSum / (end - start + 1);

        return Stats.builder()
                .min(min)
                .max(max)
                .avg(avg)
                .sum(sum)
                .variance(variance)
                .numOfItems(end - start + 1)
                .build();
    }
}