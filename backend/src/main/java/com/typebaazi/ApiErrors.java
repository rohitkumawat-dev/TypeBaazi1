package com.typebaazi;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
@RestControllerAdvice
public class ApiErrors {
 @ExceptionHandler(ResponseStatusException.class)
 ResponseEntity<Map<String,String>> handle(ResponseStatusException ex) {
  return ResponseEntity.status(ex.getStatusCode()).body(Map.of("message",
   ex.getReason()==null ? "Request failed." : ex.getReason()));
 }
}
