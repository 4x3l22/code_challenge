package com.crediya.config;

import com.crediya.model.report.gateways.ReportGateway;
import com.crediya.usecase.generatereport.GenerateReportUseCase;
import com.crediya.usecase.showreports.ShowReportsUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class UseCasesConfigTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(UseCasesConfig.class, TestGatewayConfig.class);

    @Test
    void shouldCreateShowReportsUseCaseBean() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(ShowReportsUseCase.class);
            assertThat(context.getBean(ShowReportsUseCase.class)).isNotNull();
        });
    }

    @Test
    void shouldCreateGenerateReportUseCaseBean() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(GenerateReportUseCase.class);
            assertThat(context.getBean(GenerateReportUseCase.class)).isNotNull();
        });
    }

    @Test
    void shouldCreateAllExpectedUseCaseBeans() {
        contextRunner.run(context -> {
            // Verify both use case beans are created
            assertThat(context).hasSingleBean(ShowReportsUseCase.class);
            assertThat(context).hasSingleBean(GenerateReportUseCase.class);

            // Verify they are properly configured with dependencies
            ShowReportsUseCase showReportsUseCase = context.getBean(ShowReportsUseCase.class);
            GenerateReportUseCase generateReportUseCase = context.getBean(GenerateReportUseCase.class);

            assertThat(showReportsUseCase).isNotNull();
            assertThat(generateReportUseCase).isNotNull();
        });
    }

    @Test
    void shouldInjectReportGatewayDependency() {
        contextRunner.run(context -> {
            // Verify that ReportGateway is available and injected
            assertThat(context).hasSingleBean(ReportGateway.class);

            ShowReportsUseCase showReportsUseCase = context.getBean(ShowReportsUseCase.class);
            GenerateReportUseCase generateReportUseCase = context.getBean(GenerateReportUseCase.class);

            // Both should be created successfully (which means gateway injection worked)
            assertThat(showReportsUseCase).isNotNull();
            assertThat(generateReportUseCase).isNotNull();
        });
    }

    @Test
    void shouldFailWhenReportGatewayIsMissing() {
        // Test without the gateway configuration
        ApplicationContextRunner failingRunner = new ApplicationContextRunner()
                .withUserConfiguration(UseCasesConfig.class);

        failingRunner.run(context -> {
            // Context should fail to start due to missing ReportGateway dependency
            assertThat(context).hasFailed();
        });
    }

    @Test
    void shouldScanForUseCaseComponents() {
        contextRunner.run(context -> {
            String[] beanNames = context.getBeanDefinitionNames();

            // Count beans that end with "UseCase"
            long useCaseBeanCount = java.util.Arrays.stream(beanNames)
                    .filter(name -> name.toLowerCase().contains("usecase"))
                    .count();

            // Should have at least the 2 explicitly defined use cases
            assertThat(useCaseBeanCount).isGreaterThanOrEqualTo(2);
        });
    }

    @Test
    void shouldHaveCorrectBeanNames() {
        contextRunner.run(context -> {
            // Verify specific bean names are present
            assertThat(context.containsBean("showReportsUseCase")).isTrue();
            assertThat(context.containsBean("generateReportUseCase")).isTrue();
        });
    }

    /**
     * Test configuration that provides a mock ReportGateway
     * This is needed because UseCasesConfig depends on ReportGateway
     */
    @Configuration
    static class TestGatewayConfig {
        @Bean
        public ReportGateway reportGateway() {
            return mock(ReportGateway.class);
        }
    }
}