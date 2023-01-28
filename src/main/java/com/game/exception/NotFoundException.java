package com.game.exception;

import com.game.enums.ApplicationError;

public class NotFoundException extends ApplicationException{

    public NotFoundException(final ApplicationError error,
                             final Object... messageArgs) {
        super(error,messageArgs);

    }
}
