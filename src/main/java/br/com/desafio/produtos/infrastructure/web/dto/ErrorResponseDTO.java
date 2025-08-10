package br.com.desafio.produtos.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
public class ErrorResponseDTO {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final String path;
    private final int status;
    private final String error;
    private final String message;
    private final Map<String, String> details;

    public ErrorResponseDTO(String path,  int status, String error, String message, Map<String, String> details) {
        this.path = path;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

}
