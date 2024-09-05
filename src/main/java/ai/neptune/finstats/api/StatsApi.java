package ai.neptune.finstats.api;

import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import ai.neptune.finstats.api.dto.AddBatchStatsResponse;
import ai.neptune.finstats.api.dto.StatsResponse;
import org.springframework.http.ResponseEntity;


/**
 * Interface for API that receives a request for stats and returns responses
 * Must be implemented by RestController
 */
public interface StatsApi {

    /**
     *
     * @param request - request for adding batch information about specific symbol
     * @return - an entity containing boolean flag, whether the symbol is new or not
     */
    ResponseEntity<AddBatchStatsResponse> addStats(AddBatchStatsRequest request);

    /**
     * Returns statistic for 10^k last items for specified symbol
     * @param symbol - the symbol to query statistics for
     * @param k - the power of ten for items
     * @return - entity containing min, max, average, variance and last item for the most recent 10^k items
     */
    ResponseEntity<StatsResponse> getStats(String symbol, int k);
}
