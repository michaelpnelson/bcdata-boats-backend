package ca.bc.gov.bcdata.boats;

public class ApiValidationException extends RuntimeException {

    public ApiValidationException(String message) {
        super(message);
    }
}