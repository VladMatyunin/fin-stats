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

    public static TotalStats empty() {
        return TotalStats.builder()
                .min(0)
                .max(0)
                .avg(0)
                .variance(0)
                .last(0)
                .build();
    }
}
