package com.simplyminds.product.ExceptionHandler;

import com.simplyminds.product.Model.ResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {


    //404 Not found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDTO> ResourceNotFoundException(ResourceNotFoundException ex) {
        ResponseDTO responseDTO = new ResponseDTO( false,null,  HttpStatus.NOT_FOUND.value(), ex.getMessage() );

        return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
    }

    //500 Internal server error
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> internalServerError(Exception ex, WebRequest request){
        ResponseDTO responseDTO = new ResponseDTO<>(false,null,500, ex.getMessage());
        return new ResponseEntity<>(responseDTO,HttpStatus.INTERNAL_SERVER_ERROR);
    }
    //400 Bad request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleBadRequestException(IllegalArgumentException ex){
        ResponseDTO responseDTO = new ResponseDTO(false,null,400, ex.getMessage());
        return new ResponseEntity<>(responseDTO,HttpStatus.BAD_REQUEST);
    }


}
