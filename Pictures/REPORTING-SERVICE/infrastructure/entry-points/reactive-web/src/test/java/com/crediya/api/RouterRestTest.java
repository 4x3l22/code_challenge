package com.crediya.api;

import com.crediya.model.report.Report;
import com.crediya.usecase.showreports.ShowReportsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ContextConfiguration(classes = {RouterRest.class, Handler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ShowReportsUseCase showReportsUseCase;

    @Test
    @WithMockUser // This bypasses Spring Security for this test
    void getReport_shouldReturnOkWithApiResponse() {
        // Arrange
        Report report = Report.builder()
                .metricId("M1")
                .count(5L)
                .updatedAt("2025-09-15T12:00:00Z")
                .totalAmountCents(new BigDecimal("12345"))
                .build();

        when(showReportsUseCase.execute()).thenReturn(Mono.just(report));

        // Act + Assert
        webTestClient.get()
                .uri("/api/v1/report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(true)
                .jsonPath("$.data.metricId").isEqualTo("M1")
                .jsonPath("$.data.count").isEqualTo(5)
                .jsonPath("$.data.totalAmountCents").isEqualTo(12345)
                .jsonPath("$.path").isEqualTo("/api/v1/report");

        verify(showReportsUseCase, times(1)).execute();
    }

    @Test
    @WithMockUser
    void getReport_shouldReturn404_whenUseCaseReturnsEmpty() {
        // Arrange
        when(showReportsUseCase.execute()).thenReturn(Mono.empty());

        // Act + Assert
        webTestClient.get()
                .uri("/api/v1/report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.path").isEqualTo("/api/v1/report");

        verify(showReportsUseCase, times(1)).execute();
    }

    @Test
    @WithMockUser
    void getReport_shouldReturn500_whenUseCaseThrowsError() {
        // Arrange
        when(showReportsUseCase.execute()).thenReturn(Mono.error(new RuntimeException("Database error")));

        // Act + Assert
        webTestClient.get()
                .uri("/api/v1/report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is5xxServerError()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.success").isEqualTo(false)
                .jsonPath("$.path").isEqualTo("/api/v1/report");

        verify(showReportsUseCase, times(1)).execute();
    }

    @Test
    @WithMockUser
    void getReport_shouldReturnCorrectContentType() {
        // Arrange
        Report report = Report.builder()
                .metricId("TEST")
                .count(1L)
                .updatedAt("2025-09-15T10:00:00Z")
                .totalAmountCents(new BigDecimal("100.00"))
                .build();

        when(showReportsUseCase.execute()).thenReturn(Mono.just(report));

        // Act + Assert
        webTestClient.get()
                .uri("/api/v1/report")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    // Test authentication is required
    @Test
    void getReport_shouldReturn401_whenNotAuthenticated() {
        // Don't use @WithMockUser here - test actual security behavior
        webTestClient.get()
                .uri("/api/v1/report")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isUnauthorized();
    }

    // Test unknown routes with authentication
    @Test
    @WithMockUser
    void unknownGet_shouldReturn404_whenAuthenticated() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .exchange()
                .expectStatus().isNotFound();
    }

    // Test unauthorized access to unknown routes
    @Test
    void unknownGet_shouldReturn401_whenNotAuthenticated() {
        webTestClient.get()
                .uri("/api/usecase/path")
                .exchange()
                .expectStatus().isUnauthorized();
    }



    // Test CSRF protection on POST requests when not authenticated
    @Test
    void wrongHttpMethod_shouldReturn403_whenNotAuthenticated() {
        // Without authentication, POST requests get 403 Forbidden due to CSRF protection
        webTestClient.post()
                .uri("/api/v1/report")
                .bodyValue("")
                .exchange()
                .expectStatus().isForbidden();
    }
}