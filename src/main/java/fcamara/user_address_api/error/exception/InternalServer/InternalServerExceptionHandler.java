package fcamara.user_address_api.error.exception.InternalServer;

import fcamara.user_address_api.error.BaseBodyError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class InternalServerExceptionHandler {
    @ExceptionHandler({InternalServerException.class})
    public ResponseEntity<BaseBodyError> handlerInternalServerException(InternalServerException ex){
        BaseBodyError error = BaseBodyError.builder()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

}
