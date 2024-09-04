package ai.neptune.finstats.storage;

import ai.neptune.finstats.storage.segment.SegmentTreeStatsStorage;
import org.springframework.stereotype.Component;

@Component
public class DefaultStorageProvider implements StorageProvider {

    @Override
    public SymbolStatsStorage generate() {
        return new SegmentTreeStatsStorage();
    }
}
