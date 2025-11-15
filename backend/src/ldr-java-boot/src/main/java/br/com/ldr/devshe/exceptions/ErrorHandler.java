package br.com.ldr.devshe.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import br.com.ldr.devshe.dto.Erro;

import java.io.IOException;
import java.util.Objects;

@ControllerAdvice
public class ErrorHandler {
	
	@ExceptionHandler({ServiceError.class})
	public ResponseEntity<Erro> handleException(ServiceError error, WebRequest request) {
		Erro erro = new Erro(error);
		return new ResponseEntity<>(erro, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler({NotFoundError.class})
	public ResponseEntity<Erro> handleException(NotFoundError error, WebRequest request) {
		Erro erro = new Erro();
		erro.setCode("NOT_FOUND");
		erro.setMessage(error.getMessage());
		return new ResponseEntity<>(erro, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({AccessError.class})
	public ResponseEntity<Erro> handleException(AccessError error, WebRequest request) {
		Erro erro = new Erro();
		erro.setCode("ACCESS_ERROR");
		erro.setMessage(error.getMessage());
		return new ResponseEntity<>(erro, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ConstraintViolationException.class})
	public void handleException(ConstraintViolationException exception,
								ServletWebRequest webRequest) throws IOException {
		Objects.requireNonNull(webRequest.getResponse()).sendError(HttpStatus.BAD_REQUEST.value(), exception.getMessage());
	}

	@ExceptionHandler({MissingServletRequestParameterException.class})
	public ResponseEntity<Erro> handleException(
			MissingServletRequestParameterException ex) {
		return new ResponseEntity<>(new Erro("400", ex.getMessage()), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler({MethodArgumentNotValidException.class})
	public ResponseEntity<Erro> handleException(
			MethodArgumentNotValidException ex) {
		return new ResponseEntity<>(new Erro("400", ex.getMessage()), HttpStatus.BAD_REQUEST);

	}

}
