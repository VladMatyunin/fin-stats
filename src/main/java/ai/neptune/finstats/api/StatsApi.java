package ai.neptune.finstats.api;

import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import ai.neptune.finstats.api.dto.AddBatchStatsResponse;
import ai.neptune.finstats.api.dto.StatsResponse;
import org.springframework.http.ResponseEntity;

public interface StatsApi {

    ResponseEntity<AddBatchStatsResponse> addStats(AddBatchStatsRequest request);

    ResponseEntity<StatsResponse> getStats(String symbol, int k);
}
