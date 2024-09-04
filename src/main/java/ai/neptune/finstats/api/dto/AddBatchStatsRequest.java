package ai.neptune.finstats.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddBatchStatsRequest {

    private String symbol;

    private float[] values;
}
