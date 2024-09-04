package ai.neptune.finstats.service;

import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import ai.neptune.finstats.api.dto.AddBatchStatsResponse;
import ai.neptune.finstats.api.dto.StatsResponse;
import ai.neptune.finstats.storage.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultStatsService implements StatsService {

    private final StatsRepository statsRepository;

    @Override
    public AddBatchStatsResponse add(AddBatchStatsRequest request) {
        var exists = statsRepository.add(request.getSymbol(), request.getValues());
        return new AddBatchStatsResponse(exists);
    }

    @Override
    public StatsResponse eval(String symbol, int k) {
        if (k < 0) {
            throw new IllegalArgumentException("k must be > 1");
        }
        var numOfItems = (int) Math.pow(10, k);
        var items = statsRepository.takeLast(symbol, numOfItems);
        return StatsResponse.builder()
                .min(items.getMin())
                .max(items.getMax())
                .last(items.getLast())
                .var(items.getVariance())
                .avg(items.getAvg())
                .build();
    }

}
