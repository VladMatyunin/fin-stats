package ai.neptune.finstats.api;

import ai.neptune.finstats.api.dto.AddBatchStatsRequest;
import org.springframework.stereotype.Component;

import static ai.neptune.finstats.api.ApiConstants.*;

@Component
public class ApiValidator {

    public void validateExponentInput(String symbol, int windowSizeExponent) {
        if (windowSizeExponent > K_MAX_EXPONENT || windowSizeExponent < K_MIN_EXPONENT) {
            throw new IllegalArgumentException("The k component must be in range: [" + K_MIN_EXPONENT + "-" + K_MAX_EXPONENT + "]");
        }
        validateSymbol(symbol);
    }

    public void validateBatchRequest(AddBatchStatsRequest request) {
        if (request.getValues() == null) {
            throw new IllegalArgumentException("'values' field is mandatory");
        }
        var numOfItems = request.getValues().length;
        if (numOfItems > MAX_BATCH_ADD_SIZE) {
            throw new IllegalArgumentException("Maximum number of items in batch add must be less than " + MAX_BATCH_ADD_SIZE);
        }
        if (numOfItems == 0) {
            throw new IllegalArgumentException("Empty data in batch add is not allowed");
        }
        validateSymbol(request.getSymbol());
    }

    private void validateSymbol(String symbol) {
        if (symbol == null) {
            throw new IllegalArgumentException("'symbol' field is mandatory");
        }
        if (symbol.isBlank()) {
            throw new IllegalArgumentException("'symbol' field must not be blank");
        }
    }
}
