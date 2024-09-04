package ai.neptune.finstats.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class StatsResponse {

    private float min;

    private float max;

    private float last;

    private float avg;

    private float var;
}
