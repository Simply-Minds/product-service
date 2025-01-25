package com.simplyminds.exception.handler;

import com.simplyminds.product.dto.ResponseDto;
import com.simplyminds.product.enums.ErrorCode;
import com.simplyminds.product.exception.BadRequestException;
import com.simplyminds.product.exception.NotFoundException;
import com.simplyminds.product.exception.ResourceAlreadyExistException;
import com.simplyminds.product.exception.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void testHandleValidationExceptions() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("object", "field", "error message");
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(java.util.Collections.singletonList(fieldError));

        ResponseEntity<ResponseDto<String>> response = globalExceptionHandler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().getSuccess());
        assertTrue(response.getBody().getErrorCode().startsWith(ErrorCode.BAD0001.getCode()));
        assertEquals("field: error message", response.getBody().getErrorMessage());
    }

    @Test
    void testHandleBadRequestException() {
        String errorCode = "BAD001";
        String errorMessage = "Bad Request error";
        BadRequestException ex = new BadRequestException(errorCode, errorMessage);

        ResponseEntity<ResponseDto<String>> response = globalExceptionHandler.handleBadRequestException(ex);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(false, response.getBody().getSuccess());
        assertEquals(errorCode, response.getBody().getErrorCode());
        assertEquals(errorMessage, response.getBody().getErrorMessage());
    }

    @Test
    void testHandleResourceAlreadyExistException() {
        String errorCode = "RES001";
        String errorMessage = "Resource already exists";
        ResourceAlreadyExistException ex = new ResourceAlreadyExistException(errorCode, errorMessage);

        ResponseEntity<ResponseDto<String>> response = globalExceptionHandler.handleResourceAlreadyExistException(ex);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(false, response.getBody().getSuccess());
        assertEquals(errorCode, response.getBody().getErrorCode());
        assertEquals(errorMessage, response.getBody().getErrorMessage());
    }
    @Test
    void testHandleNotFoundException() {
        String errorCode = "ERR404";
        String errorMessage = "Not found error";
        NotFoundException ex = new NotFoundException(errorCode, errorMessage);

        ResponseEntity<ResponseDto<String>> response = globalExceptionHandler.handleNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());// what this line mean ? Ans:-
        assertEquals(false, response.getBody().getSuccess());
        assertEquals(errorCode, response.getBody().getErrorCode());
        assertEquals(errorMessage, response.getBody().getErrorMessage());
    }

    @Test
    void testHandleGenericException() {
        Exception ex = new Exception("Generic error");

        ResponseEntity<ResponseDto<String>> response = globalExceptionHandler.handleGenericException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(false, response.getBody().getSuccess());
        assertEquals(ErrorCode.GEN0001.getCode(), response.getBody().getErrorCode());
        assertEquals(ErrorCode.GEN0001.getMessage(), response.getBody().getErrorMessage());
    }
}