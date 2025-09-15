package com.crediya.usecase.generatereport;


import com.crediya.model.report.gateways.ReportGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GenerateReportUseCaseTest {

    @Mock
    private ReportGateway reportGateway;

    private GenerateReportUseCase generateReportUseCase;

    @BeforeEach
    void setUp() {
        generateReportUseCase = new GenerateReportUseCase(reportGateway);
    }

    @Test
    void execute_shouldCallGatewayWithApprovedAmount() {
        // Given
        BigDecimal approvedAmount = new BigDecimal("75000");
        when(reportGateway.updateReport(approvedAmount))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(generateReportUseCase.execute(approvedAmount))
                .verifyComplete();

        verify(reportGateway).updateReport(approvedAmount);
    }

    @Test
    void execute_shouldHandleZeroAmount() {
        // Given
        BigDecimal zeroAmount = BigDecimal.ZERO;
        when(reportGateway.updateReport(zeroAmount))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(generateReportUseCase.execute(zeroAmount))
                .verifyComplete();

        verify(reportGateway).updateReport(zeroAmount);
    }

    @Test
    void execute_shouldHandleNullAmount() {
        // Given
        when(reportGateway.updateReport(null))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(generateReportUseCase.execute(null))
                .verifyComplete();

        verify(reportGateway).updateReport(null);
    }

    @Test
    void execute_shouldPropagateGatewayError() {
        // Given
        BigDecimal approvedAmount = new BigDecimal("50000");
        RuntimeException gatewayError = new RuntimeException("Database connection error");

        when(reportGateway.updateReport(approvedAmount))
                .thenReturn(Mono.error(gatewayError));

        // When & Then
        StepVerifier.create(generateReportUseCase.execute(approvedAmount))
                .expectError(RuntimeException.class)
                .verify();

        verify(reportGateway).updateReport(approvedAmount);
    }

    @Test
    void execute_shouldHandleLargeAmounts() {
        // Given
        BigDecimal largeAmount = new BigDecimal("999999999999.99");
        when(reportGateway.updateReport(largeAmount))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(generateReportUseCase.execute(largeAmount))
                .verifyComplete();

        verify(reportGateway).updateReport(largeAmount);
    }
}