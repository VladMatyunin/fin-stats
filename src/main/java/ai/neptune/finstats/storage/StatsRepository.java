package ai.neptune.finstats.storage;

public interface StatsRepository {

    boolean add(String symbol, float[] items);

    TotalStats takeLast(String symbol, int numOfItems);
}
