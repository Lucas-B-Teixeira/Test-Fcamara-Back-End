package fcamara.user_address_api.error.exception.forbidden;

import fcamara.user_address_api.error.BaseBodyError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ForbiddenExceptionHandler {
    @ExceptionHandler({ForbiddenException.class})
    public ResponseEntity<BaseBodyError> handlerForbiddenException(ForbiddenException ex){
        BaseBodyError error = BaseBodyError.builder()
                .errorCode(HttpStatus.FORBIDDEN.toString())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

}
