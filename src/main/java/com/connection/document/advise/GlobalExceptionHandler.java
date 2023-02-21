package com.connection.document.advise;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.Instant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import com.connection.document.common.MessageTypeConstant;
import com.connection.document.exception.NoSuchElementException;
import com.connection.document.exception.ServerError;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ServerError> serverErrorHandling(NoSuchElementException ex, HttpServletRequest req,
			HttpServletResponse res, WebRequest re) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		ex.printStackTrace(printWriter);
		String stackTrace = stringWriter.toString();

		String msg = ex.getMessage();
		ServerError se = null;
		se = ServerError.builder().timestamp(Instant.now().toEpochMilli()).status(HttpStatus.NOT_FOUND.value())
				.errorCode(MessageTypeConstant.ERROR.getMessage()).errorMessage(msg)
				.traceId(Instant.now().getEpochSecond()).errorDeatils(stackTrace).path(re.getDescription(false))
				.build();
		return new ResponseEntity<>(se, HttpStatus.NOT_FOUND);

	}

}