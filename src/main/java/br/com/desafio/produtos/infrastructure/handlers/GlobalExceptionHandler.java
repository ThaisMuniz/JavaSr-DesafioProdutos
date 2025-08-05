package br.com.desafio.produtos.infrastructure.handlers;

import br.com.desafio.produtos.domain.exception.RegistroDuplicadoException;
import br.com.desafio.produtos.infrastructure.web.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Captura exceções de validação do Bean Validation (@Valid).
     * Retorna um status 400 (Bad Request) com um mapa detalhando cada campo que falhou.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();

        // Itera sobre todos os erros de campo encontrados na exceção
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Erro de validação na requisição",
                fieldErrors
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistroDuplicadoException.class)
    public ResponseEntity<ErrorResponseDTO> handleBaralhoClientException(RegistroDuplicadoException ex, WebRequest request) {
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                request.getDescription(false).replace("uri=", ""),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                ex.getMessage(),
                null
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
