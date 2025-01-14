package com.simplyminds.product.exception.handler;

import com.simplyminds.product.dto.ResponseDto;
import com.simplyminds.product.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation exceptions caused by invalid input.
     *
     * @param ex the MethodArgumentNotValidException containing validation errors
     * @return a response with error details
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseDto<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ResponseDto<String> response = new ResponseDto<>(false, null, 400, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle bad request exceptions caused by invalid business logic.
     *
     * @param ex the BadRequestException
     * @return a response with error details
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResponseDto<String>> handleBadRequestException(BadRequestException ex) {
        ResponseDto<String> response = new ResponseDto<>(false, null, 400, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle generic exceptions that are not explicitly handled elsewhere.
     *
     * @param ex the Exception
     * @return a response with a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<String>> handleGenericException(Exception ex) {
        ResponseDto<String> response = new ResponseDto<String>(false, null, 500, "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
