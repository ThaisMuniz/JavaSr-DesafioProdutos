package br.com.desafio.produtos.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ErrorResponseDTO {

    private LocalDateTime timestamp = LocalDateTime.now();
    private String path;
    private int status;
    private String error;
    private String message;
    private Map<String, String> details;

    public ErrorResponseDTO(String path,  int status, String error, String message, Map<String, String> details) {
        this.path = path;
        this.status = status;
        this.error = error;
        this.message = message;
        this.details = details;
    }

}
