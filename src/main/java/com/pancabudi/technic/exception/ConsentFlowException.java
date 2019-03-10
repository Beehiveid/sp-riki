package com.pancabudi.technic.exception;

public class ConsentFlowException extends RuntimeException {
    public ConsentFlowException(String message) {
        super(message + " consent flow is not okay");
    }
}
