package nl.demirel.tictactoe.tictactoe.common;

import lombok.extern.slf4j.Slf4j;
import nl.demirel.tictactoe.tictactoe.common.exception.InvalidRowOrColumnException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletResponse;


@Slf4j
@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    private final RestResponseFactory restResponseFactory;

    public ErrorHandlingControllerAdvice(RestResponseFactory restResponseFactory) {
        this.restResponseFactory = restResponseFactory;
    }

    @ExceptionHandler(Throwable.class)
    ResponseEntity<RestResponse<Void>> defaultExceptionHandler(
            final Throwable ex,
            final HttpServletResponse response) {
        log.error(ex.getMessage(), ex);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(restResponseFactory.error("ops... general error"));
    }

    @ExceptionHandler(InvalidRowOrColumnException.class)
    ResponseEntity<RestResponse<Void>> handleException(
            final InvalidRowOrColumnException ex,
            final HttpServletResponse response) {
        log.error(ex.getMessage(), ex);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(restResponseFactory.error(ex.getMessage()));
    }


}
