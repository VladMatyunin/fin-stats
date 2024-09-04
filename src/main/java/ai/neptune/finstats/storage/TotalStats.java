package ai.neptune.finstats.storage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TotalStats {

    private float min;

    private float max;

    private float avg;

    private float variance;

    private float last;
}
