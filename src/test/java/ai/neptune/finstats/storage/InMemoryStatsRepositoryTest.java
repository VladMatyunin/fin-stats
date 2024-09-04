package ai.neptune.finstats.storage;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryStatsRepositoryTest {

    private InMemoryStatsRepository storage;

    @BeforeEach
    public void setUp() {
        storage = new InMemoryStatsRepository(new DefaultStorageProvider());
    }

    @Test
    public void testAddTradingData() {
        String symbol = "AAPL";
        var prices = new float[]{1.2f, 3.4f, 5.6f, 7.8f, 9.0f};

        storage.add(symbol, prices);

        // Check if data was added for different window sizes
        assertNotNull(storage.takeLast(symbol, 10)); // 1e1 -> 10
        assertNotNull(storage.takeLast(symbol, 100)); // 1e2 -> 100
        assertNotNull(storage.takeLast(symbol, 1000));    // 1e3 -> 1000 not added
    }

    @Test
    public void testAddNonPowerOfTen() {
        String symbol = "MSFT";
        var prices = new float[]{2.5f, 4.5f, 6.5f, 8.5f};

        storage.add(symbol, prices);

        assertNotNull(storage.takeLast(symbol, 1)); // 1e1 -> 10
        assertNotNull(storage.takeLast(symbol, 2));    // 1e2 -> 100 not added
    }

    @Test
    public void testTakeLastValidSymbol() {
        String symbol = "GOOG";
        var prices = new float[] {10.0f, 20.0f, 30.0f, 40.0f, 50.0f};

        storage.add(symbol, prices);

        TotalStats stats = storage.takeLast(symbol, 10); // Request for 1e1 -> 10 points

        assertNotNull(stats);
        assertEquals(10.0f, stats.getMin());
        assertEquals(50.0f, stats.getMax());
        assertEquals(30.0f, stats.getAvg(), 0.001);
        assertEquals(200.0f, stats.getVariance(), 0.001); // Validate variance
    }

    @Test
    public void testTakeLastNonExistingSymbol() {
        String symbol = "TSLA";

        assertThrows(NoSuchElementException.class, () -> storage.takeLast(symbol, 1));
    }

    @Test
    public void testTakeLastMoreThanAvailable() {
        String symbol = "AMZN";
        var prices =  new float[] {100.0f, 200.0f};

        storage.add(symbol, prices);

        // Request more items than available (i.e., 1e3 -> 1000 points)
        var stats = storage.takeLast(symbol, 1000);
        verifyStats("AMZN", 2, 100, 200, 150, 2500);
    }


    @Test
    public void testMultipleDataAdditions() {
        String symbol = "AAPL";

        // Add first batch of data
        var batch1 = new float[]{1.0f, 2.0f, 3.0f};
        storage.add(symbol, batch1);

        // Verify stats after first batch
        verifyStats(symbol, 10, 1.0f, 3.0f, 2.0f, 0.6667f);

        // Add second batch of data
        var batch2 = new float[] {4.0f, 5.0f, 6.0f};
        storage.add(symbol, batch2);

        // Verify stats after second batch
        verifyStats(symbol, 10, 1.0f, 6.0f, 3.5f, 2.9167f);

        // Add third batch of data
        var batch3 = new float[] {7.0f, 8.0f};
        storage.add(symbol, batch3);

        // Verify stats after third batch
        verifyStats(symbol, 10, 1.0f, 8.0f, 4.5f, 5.25f);

        // Add fourth batch of data
        var batch4 = new float[] {10.0f, 20.0f, 30.0f, 40.0f};
        storage.add(symbol, batch4);

        // Verify stats after fourth batch
        verifyStats(symbol, 10, 3.0f, 40.0f, 13.3f, 143.01f);
    }

    private void verifyStats(String symbol, int numOfItems, float expectedMin, float expectedMax, float expectedAvg, float expectedVariance) {
        TotalStats stats = storage.takeLast(symbol, numOfItems);
        assertNotNull(stats, "TotalStats should not be null");

        // Verify min, max, average, and variance statistics
        assertEquals(expectedMin, stats.getMin(), 0.001, "Min value mismatch");
        assertEquals(expectedMax, stats.getMax(), 0.001, "Max value mismatch");
        assertEquals(expectedAvg, stats.getAvg(), 0.001, "Average value mismatch");
        assertEquals(expectedVariance, stats.getVariance(), 0.001, "Variance value mismatch");
    }
}