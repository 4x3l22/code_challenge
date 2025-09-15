// com/crediya/api/Handler.java
package com.crediya.api;

import com.crediya.api.dto.ApiResponse;
import com.crediya.usecase.shared.Messages;
import com.crediya.usecase.showreports.ShowReportsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final ShowReportsUseCase showReportsUseCase;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest req) {
        return showReportsUseCase.execute()
                .map(report -> ApiResponse.ok(report, Messages.REPORTS_SUCCESSFUL, req.path()))
                .flatMap(body -> ServerResponse.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(body))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.fail(Messages.NOT_FOUND, null, req.path())))
                .onErrorResume(e -> ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.fail(Messages.ERROR, e.getMessage(), req.path())));
    }
}
