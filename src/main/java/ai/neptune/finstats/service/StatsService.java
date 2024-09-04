package ai.neptune.finstats.service;

import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import ai.neptune.finstats.api.dto.AddBatchStatsResponse;
import ai.neptune.finstats.api.dto.StatsResponse;

public interface StatsService {

    AddBatchStatsResponse add(AddBatchStatsRequest request);

    StatsResponse eval(String symbol, int k);
}
