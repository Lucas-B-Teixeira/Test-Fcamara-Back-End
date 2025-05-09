package fcamara.user_address_api.error.exception.badRequest;

import fcamara.user_address_api.error.BaseBodyError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class BadRequestExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<BaseBodyError> handlerBadRequestException(BadRequestException ex){
        BaseBodyError error = BaseBodyError.builder()
                .errorCode(HttpStatus.BAD_REQUEST.toString())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

}
