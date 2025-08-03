package br.com.desafio.produtos.infrastructure.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class GlobalExceptionHandler {

//    @ExceptionHandler(ParametrosDeJogoInvalidosException.class)
//    public ResponseEntity<ErrorResponseDto> handleParametrosDeJogoInvalidosException(ParametrosDeJogoInvalidosException ex, WebRequest request) {
//        ErrorResponseDto errorResponse = new ErrorResponseDto(
//                HttpStatus.BAD_REQUEST.value(),
//                HttpStatus.BAD_REQUEST.getReasonPhrase(),
//                ex.getMessage(),
//                request.getDescription(false).replace("uri=", ""),
//                ex.getErrors()
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
//    }
//
//    @ExceptionHandler(BaralhoClientException.class)
//    public ResponseEntity<ErrorResponseDto> handleBaralhoClientException(BaralhoClientException ex, WebRequest request) {
//        ErrorResponseDto errorResponse = new ErrorResponseDto(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
//                ex.getMessage(),
//                request.getDescription(false).replace("uri=", "")
//        );
//        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

}
