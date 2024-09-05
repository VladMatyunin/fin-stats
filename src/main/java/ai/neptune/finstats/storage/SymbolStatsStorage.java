package ai.neptune.finstats.storage;

/**
 * An abstract storage to store items and retrieve stats for them for specific symbol
 * @see ai.neptune.finstats.storage.segment.SegmentTreeStatsStorage
 */
public interface SymbolStatsStorage {

    void pushBatch(float[] data);

    TotalStats takeStats(int windowSizeExponent);
}
