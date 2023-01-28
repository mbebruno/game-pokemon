package com.game.exception;

import com.game.model.dto.APIResponse;
import com.game.model.dto.ErrorDTO;
import com.game.util.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class HandlerException {

    //Gestion de la validité des arguments
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<?> handleMethodArgumentException(MethodArgumentNotValidException exception) {
        APIResponse<?> response = new APIResponse<>();
        List<ErrorDTO> errors = new ArrayList<>();
        exception.getBindingResult().getFieldErrors()
                .forEach(error -> {
                    ErrorDTO errorDTO = new ErrorDTO(error.getField(), error.getDefaultMessage());
                    errors.add(errorDTO);
                });
        response.setStatus(Message.MSG_FAILED);
        response.setErrors(errors);
        return response;
    }

    @ExceptionHandler(ApplicationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public APIResponse<?> handleServiceException(ApplicationException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus(Message.MSG_FAILED);
        serviceResponse.setMessage(exception.getMessage());

        return serviceResponse;
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public APIResponse<?> handleNotFoundException(NotFoundException exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus(Message.MSG_FAILED);
        serviceResponse.setMessage(exception.getMessage());

        return serviceResponse;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public APIResponse<?> handleServiceException(Exception exception) {
        APIResponse<?> serviceResponse = new APIResponse<>();
        serviceResponse.setStatus(Message.MSG_FAILED);
        serviceResponse.setMessage(exception.getMessage());

        return serviceResponse;
    }
}
