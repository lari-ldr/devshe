package br.com.ldr.devshe.exceptions;

public class ServiceError extends RuntimeException {
	
	public ServiceError(String message) {
		super(message);
	}
	
	public ServiceError(String message, Throwable t) {
		super(message, t);
	}

}
