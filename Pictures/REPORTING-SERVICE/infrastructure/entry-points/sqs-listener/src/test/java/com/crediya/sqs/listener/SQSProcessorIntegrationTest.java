package com.crediya.sqs.listener;


import com.crediya.model.report.gateways.ReportGateway;
import com.crediya.sqs.listener.dto.GenerateReportEventDto;
import com.crediya.usecase.generatereport.GenerateReportUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.model.Message;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {SQSProcessor.class, GenerateReportUseCase.class, ObjectMapper.class})
class SQSProcessorIntegrationTest {

    @Autowired
    private SQSProcessor sqsProcessor;

    @MockitoBean
    private ReportGateway reportGateway;

    @Test
    void shouldProcessCompleteMessageFlow() {
        // Given
        String messageBody = "{\"status\":\"APPROVED\",\"approvedAmount\":100000}";
        Message message = Message.builder()
                .messageId("integration-test-id")
                .body(messageBody)
                .build();

        when(reportGateway.updateReport(any(BigDecimal.class)))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(sqsProcessor.apply(message))
                .verifyComplete();

        verify(reportGateway).updateReport(new BigDecimal("100000"));
    }
}