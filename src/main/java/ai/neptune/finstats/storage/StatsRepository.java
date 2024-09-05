package ai.neptune.finstats.storage;

/**
 * Class for storing and retrieving statistics for various symbols
 * Default implementation uses InMemory storage
 */
public interface StatsRepository {

    /**
     * Batch adding of some float data (from oldest to new) for specific symbol
     * If the symbol exists in repository, the data is appended
     * If not, the record is created for it
     * @param symbol - specified symbol to add data to
     * @param items - batch float items to add
     * @return - true if the symbol existed before, false otherwise
     */
    boolean add(String symbol, float[] items);

    /**
     * Retrieves statistic information for last numOfItems values of the symbol
     * Expected to work faster than O(n) complexity
     * @param symbol - specified symbol to query data for
     * @param numOfItems - the number of data points to iterate. E.g. if numOfItems is 100, then the method
     *                   should find statistics for array [0 - 99] items
     * @return statistical information for last numOfItems (average, last, min, max, variance)
     */
    TotalStats takeLast(String symbol, int numOfItems);
}
