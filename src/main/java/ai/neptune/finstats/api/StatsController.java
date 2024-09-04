package ai.neptune.finstats.api;

import ai.neptune.finstats.service.StatsService;
import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import ai.neptune.finstats.api.dto.AddBatchStatsResponse;
import ai.neptune.finstats.api.dto.StatsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ai.neptune.finstats.api.ApiConstants.*;

@RestController
@RequestMapping(API_BASE_URL)
@RequiredArgsConstructor
public class StatsController implements StatsApi {

    private final StatsService statsService;

    private final ApiValidator validator;

    @PostMapping(BATCH_ADD_URL)
    public ResponseEntity<AddBatchStatsResponse> addStats(@RequestBody AddBatchStatsRequest request) {
        validator.validateBatchRequest(request);
        return ResponseEntity.ok(statsService.add(request));
    }

    @GetMapping(GET_STATS_URL)
    public ResponseEntity<StatsResponse> getStats(@RequestParam String symbol, @RequestParam int k) {
        validator.validateExponentInput(symbol, k);
        return ResponseEntity.ok(statsService.eval(symbol, k));
    }
}
