package com.game.exception;

import com.game.enums.ApplicationError;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {

    private final ApplicationError applicationError;

    public ApplicationException(final ApplicationError error,
                                final Object... messageArgs) {
        super(String.format(error.getMessage(), messageArgs));
        this.applicationError = error;
    }
}
