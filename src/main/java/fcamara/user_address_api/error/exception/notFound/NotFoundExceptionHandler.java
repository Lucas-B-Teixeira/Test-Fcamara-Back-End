package fcamara.user_address_api.error.exception.notFound;

import fcamara.user_address_api.error.BaseBodyError;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class NotFoundExceptionHandler {
    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<BaseBodyError> handlerNotFoundException(NotFoundException ex){
        BaseBodyError error = BaseBodyError.builder()
                .errorCode(HttpStatus.NOT_FOUND.toString())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}