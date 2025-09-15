package com.crediya.model.report;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Report {
    private String metricId;
    private Long count;
    private String updatedAt;
    private BigDecimal totalAmountCents;

}
