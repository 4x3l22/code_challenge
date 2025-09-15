package com.crediya.usecase.generatereport;

import com.crediya.model.report.Report;
import com.crediya.model.report.gateways.ReportGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@RequiredArgsConstructor
public class GenerateReportUseCase {
    private final ReportGateway gateway;
    public Mono<Void> execute(BigDecimal approvedAmountCents) {
        return gateway.updateReport( approvedAmountCents); }
}
