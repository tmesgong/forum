package com.company.global;

import com.company.exception.AuthException;
import com.company.util.AssemblyException;
import com.company.util.Msg;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class AppControllerAdvice extends ResponseEntityExceptionHandler {
    @ExceptionHandler({AssemblyException.class, AuthException.class})
    public ResponseEntity<Msg> handle(
            Exception e) throws Exception {
        if (e instanceof AssemblyException) {
            return ResponseEntity.ok(Msg.newFailedMsg(1, "校验错误", ((AssemblyException) e).getErrors()));
        } else if (e instanceof AuthException) {
            return ResponseEntity.ok(Msg.newFailedMsg(2, e.getMessage()));
        } else {
            throw e;
        }
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<Msg> handleOther(Exception e){
        e.printStackTrace();

        return ResponseEntity.ok(Msg.newFailedMsg(4, "未知错误"));
    }
}
