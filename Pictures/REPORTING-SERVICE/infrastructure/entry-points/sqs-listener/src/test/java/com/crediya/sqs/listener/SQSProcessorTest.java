package com.crediya.sqs.listener;

import com.crediya.sqs.listener.dto.GenerateReportEventDto;
import com.crediya.usecase.generatereport.GenerateReportUseCase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import software.amazon.awssdk.services.sqs.model.Message;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SQSProcessorTest {

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private GenerateReportUseCase generateReportUseCase;

    private SQSProcessor sqsProcessor;

    @BeforeEach
    void setUp() {
        sqsProcessor = new SQSProcessor(objectMapper, generateReportUseCase);
    }

    @Test
    void apply_shouldProcessMessageSuccessfully() throws JsonProcessingException {
        // Given
        String messageId = "test-message-id";
        String messageBody = "{\"status\":\"APPROVED\",\"approvedAmount\":50000}";
        BigDecimal approvedAmount = new BigDecimal("50000");

        Message message = Message.builder()
                .messageId(messageId)
                .body(messageBody)
                .build();

        GenerateReportEventDto eventDto = new GenerateReportEventDto();
        eventDto.setStatus("APPROVED");
        eventDto.setApprovedAmount(approvedAmount);

        when(objectMapper.readValue(messageBody, GenerateReportEventDto.class))
                .thenReturn(eventDto);
        when(generateReportUseCase.execute(approvedAmount))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(sqsProcessor.apply(message))
                .verifyComplete();

        verify(objectMapper).readValue(messageBody, GenerateReportEventDto.class);
        verify(generateReportUseCase).execute(approvedAmount);
    }

//    @Test
//    void apply_shouldHandleJsonParsingError() throws JsonProcessingException {
//        // Given
//        String messageId = "test-message-id";
//        String messageBody = "invalid-json";
//
//        Message message = Message.builder()
//                .messageId(messageId)
//                .body(messageBody)
//                .build();
//
//        when(objectMapper.readValue(messageBody, GenerateReportEventDto.class))
//                .thenThrow(new RuntimeException("JSON parsing error"));
//
//        // When & Then
//        StepVerifier.create(sqsProcessor.apply(message))
//                .verifyComplete(); // With onErrorResume, errors are handled gracefully
//
//        verify(objectMapper).readValue(messageBody, GenerateReportEventDto.class);
//        verifyNoInteractions(generateReportUseCase);
//    }
//
//    @Test
//    void apply_shouldHandleUseCaseExecutionError() throws JsonProcessingException {
//        // Given
//        String messageId = "test-message-id";
//        String messageBody = "{\"status\":\"APPROVED\",\"approvedAmount\":50000}";
//        BigDecimal approvedAmount = new BigDecimal("50000");
//
//        Message message = Message.builder()
//                .messageId(messageId)
//                .body(messageBody)
//                .build();
//
//        GenerateReportEventDto eventDto = new GenerateReportEventDto();
//        eventDto.setStatus("APPROVED");
//        eventDto.setApprovedAmount(approvedAmount);
//
//        when(objectMapper.readValue(messageBody, GenerateReportEventDto.class))
//                .thenReturn(eventDto);
//        when(generateReportUseCase.execute(approvedAmount))
//                .thenReturn(Mono.error(new RuntimeException("Database error")));
//
//        // When & Then
//        StepVerifier.create(sqsProcessor.apply(message))
//                .verifyComplete(); // With onErrorResume, errors are handled gracefully
//
//        verify(objectMapper).readValue(messageBody, GenerateReportEventDto.class);
//        verify(generateReportUseCase).execute(approvedAmount);
//    }

    @Test
    void apply_shouldHandleNullApprovedAmount() throws JsonProcessingException {
        // Given
        String messageId = "test-message-id";
        String messageBody = "{\"status\":\"REJECTED\",\"approvedAmount\":null}";

        Message message = Message.builder()
                .messageId(messageId)
                .body(messageBody)
                .build();

        GenerateReportEventDto eventDto = new GenerateReportEventDto();
        eventDto.setStatus("REJECTED");
        eventDto.setApprovedAmount(null);

        when(objectMapper.readValue(messageBody, GenerateReportEventDto.class))
                .thenReturn(eventDto);
        when(generateReportUseCase.execute(null))
                .thenReturn(Mono.empty());

        // When & Then
        StepVerifier.create(sqsProcessor.apply(message))
                .verifyComplete();

        verify(objectMapper).readValue(messageBody, GenerateReportEventDto.class);
        verify(generateReportUseCase).execute(null);
    }
}