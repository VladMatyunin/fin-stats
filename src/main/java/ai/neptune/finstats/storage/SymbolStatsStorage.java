package ai.neptune.finstats.storage;

public interface SymbolStatsStorage {

    void pushBatch(float[] data);

    TotalStats takeStats(int windowSizeExponent);
}
