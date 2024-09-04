package ai.neptune.finstats.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class InMemoryStatsRepository implements StatsRepository {

    private final StorageProvider storageProvider;

    private final ConcurrentHashMap<String, SymbolStatsStorage> symbolStorages = new ConcurrentHashMap<>();

    public boolean add(String symbol, float[] prices) {
        var exists = symbolStorages.containsKey(symbol);
        symbolStorages.computeIfAbsent(symbol, s -> storageProvider.generate());
        symbolStorages.get(symbol).pushBatch(prices);
        return exists;
    }

    public TotalStats takeLast(String symbol, int numOfItems) {
        var storage = symbolStorages.get(symbol);
        if (storage == null) {
            throw new NoSuchElementException("Symbol not found: " + symbol);
        }
        return storage.takeStats(numOfItems);
    }
}
