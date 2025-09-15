package com.crediya.sqs.listener.dto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class GenerateReportEventDtoTest {

    @Test
    void shouldCreateEventDtoWithAllFields() {
        // Given
        String expectedStatus = "APPROVED";
        BigDecimal expectedAmount = new BigDecimal("50000");

        // When
        GenerateReportEventDto eventDto = new GenerateReportEventDto();
        eventDto.setStatus(expectedStatus);
        eventDto.setApprovedAmount(expectedAmount);

        // Then
        assertEquals(expectedStatus, eventDto.getStatus());
        assertEquals(expectedAmount, eventDto.getApprovedAmount());
    }

    @Test
    void shouldHandleNullValues() {
        // When
        GenerateReportEventDto eventDto = new GenerateReportEventDto();
        eventDto.setStatus(null);
        eventDto.setApprovedAmount(null);

        // Then
        assertNull(eventDto.getStatus());
        assertNull(eventDto.getApprovedAmount());
    }
}