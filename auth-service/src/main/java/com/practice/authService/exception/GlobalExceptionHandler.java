package com.practice.authService.exception;

import com.practice.authService.dto.ErrorResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException ex){
        log.info("Wrong input validation",ex);
         Map<String,String> map = ex. getBindingResult()
                 .getFieldErrors()
                 .stream()
                 .collect(Collectors.toMap(
                         FieldError::getField,
                         FieldError::getDefaultMessage,
                         (msg1, msg2) -> msg1 + ", " + msg2
                 ));

         return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(false,"Validations failed",map,LocalDateTime.now()));
    }

    @ExceptionHandler(AlreadyPresentException.class)
    public ResponseEntity<ErrorResponseDto> handleAlreadyPresentException(AlreadyPresentException ex){
        log.info("Already present exception",ex);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponseDto(false,ex.getMessage(),null, LocalDateTime.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException ex){
        log.info("Not found exception",ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND
                )
                .body(new ErrorResponseDto(false,ex.getMessage(),null,LocalDateTime.now()));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleInvalidCredentialsException(InvalidCredentialsException ex){
        log.info("Invalid credentials exception",ex);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponseDto(false,ex.getMessage(),null,LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDto> handleBadCredentialsException(BadCredentialsException ex){
        log.info("Bad credentials exception",ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDto(false,ex.getMessage(),null,LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex){
        log.info("Unknown exception",ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDto(false,"Something went wrong",null,LocalDateTime.now()));
    }
}
