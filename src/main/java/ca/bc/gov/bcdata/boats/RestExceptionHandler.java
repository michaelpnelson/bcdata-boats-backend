package ca.bc.gov.bcdata.boats;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  
   @ExceptionHandler(ApiValidationException.class)
   protected ResponseEntity<Object> handleApiException(ApiValidationException ex) {
       return ResponseEntity.badRequest().body(ex.getMessage());
   }
}
