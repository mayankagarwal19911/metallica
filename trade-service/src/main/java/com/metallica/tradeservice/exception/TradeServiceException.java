package com.metallica.tradeservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public
class TradeServiceException  extends RuntimeException {
    public
    TradeServiceException (String cause) {
        super ( cause );
    }
}