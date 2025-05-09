package fcamara.user_address_api.error.exception.conflict;

import fcamara.user_address_api.error.BaseBodyError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ConflictExceptionHandler {
    @ExceptionHandler({ConflictException.class})
    public ResponseEntity<BaseBodyError> handlerConflictException(ConflictException ex){
        BaseBodyError error = BaseBodyError.builder()
                .errorCode(HttpStatus.CONFLICT.toString())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

}