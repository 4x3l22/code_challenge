// com/crediya/api/RouterRest.java
package com.crediya.api;

import com.crediya.api.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/report",
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "listenGETUseCase",
                    operation = @Operation(
                            operationId = "listenGETUseCase",
                            summary = "Obtener el reporte único",
                            responses = {
                                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                                            responseCode = "200",
                                            description = "OK",
                                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        // Alinea la ruta real con la documentada
        return route(GET("/api/v1/report"), handler::listenGETUseCase);
    }
}
