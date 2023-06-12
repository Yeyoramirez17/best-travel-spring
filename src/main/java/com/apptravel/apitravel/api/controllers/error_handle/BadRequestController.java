package com.apptravel.apitravel.api.controllers.error_handle;

import com.apptravel.apitravel.api.models.response.BaseErrorResponse;
import com.apptravel.apitravel.api.models.response.ErrorResponse;
import com.apptravel.apitravel.api.models.response.ErrorsResponse;
import com.apptravel.apitravel.util.exceptions.IdNotFoundException;
import com.apptravel.apitravel.util.exceptions.UsernameNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestController {

    @ExceptionHandler({ IdNotFoundException.class , UsernameNotFoundException.class})
    public BaseErrorResponse handleBadRequest(RuntimeException exception) {
        return ErrorResponse
                .builder()
                .error(exception.getMessage())
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseErrorResponse handleBadRequest(MethodArgumentNotValidException exception) {
        var errors = new ArrayList<String>();
        exception.getAllErrors()
                .forEach(error -> errors.add(error.getDefaultMessage()));

        return ErrorsResponse
                .builder()
                .errors(errors)
                .status(HttpStatus.BAD_REQUEST.name())
                .code(HttpStatus.BAD_REQUEST.value())
                .build();
    }
}
