package com.crediya.usecase.showreports;

import com.crediya.model.report.Report;
import com.crediya.model.report.gateways.ReportGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class ShowReportsUseCase {
    private final ReportGateway gateway;
    public Mono<Report> execute() { return gateway.get(); }
}
