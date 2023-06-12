package com.apptravel.apitravel.api.controllers.error_handle;

import com.apptravel.apitravel.api.models.response.BaseErrorResponse;
import com.apptravel.apitravel.api.models.response.ErrorResponse;
import com.apptravel.apitravel.util.exceptions.ForbiddenCustomerException;
import com.apptravel.apitravel.util.exceptions.IdNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenCustomerHandler {

    @ExceptionHandler(ForbiddenCustomerException.class)
    public BaseErrorResponse handleIdNotFound(ForbiddenCustomerException exception) {
        return ErrorResponse
                .builder()
                .error(exception.getMessage())
                .status(HttpStatus.FORBIDDEN.name())
                .code(HttpStatus.FORBIDDEN.value())
                .build();
    }
}
