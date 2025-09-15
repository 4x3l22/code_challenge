package com.crediya.config;


import com.crediya.model.report.gateways.ReportGateway;
import com.crediya.usecase.generatereport.GenerateReportUseCase;
import com.crediya.usecase.showreports.ShowReportsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@ComponentScan(basePackages = "com.crediya.usecase",
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "^.+UseCase$")
        },
        useDefaultFilters = false)
public class UseCasesConfig {
    @Bean
    public ShowReportsUseCase showReportsUseCase(ReportGateway gateway) {
        return new ShowReportsUseCase(gateway);
    }

    @Bean
    public GenerateReportUseCase generateReportUseCase(ReportGateway gateway) {
        return new GenerateReportUseCase(gateway);
    }


}
