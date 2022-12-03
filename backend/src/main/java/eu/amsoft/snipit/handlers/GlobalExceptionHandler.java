package eu.amsoft.snipit.handlers;

import eu.amsoft.snipit.exception.EntityNotFoundException;
import eu.amsoft.snipit.ressources.dto.ExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity<ExceptionResponse> unauthorizedException(final Exception e) {
        final String message = e.getMessage();
        log.error(message);
        return generateResponse(message);
    }

    private ResponseEntity<ExceptionResponse> generateResponse(final String message) {
        final ExceptionResponse response = ExceptionResponse.builder()
                .errorMessage(message)
                .errorCode(NOT_FOUND.value())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity
                .status(NOT_FOUND)
                .body(response);
    }
}
