package com.linearrsa.common;

/**
 *  @author Anurag Alla
 *
 */
public class ConstraintFailureException extends Exception {
	private static final long serialVersionUID = 1L;
	private String code;
	private String message;

	public ConstraintFailureException(ExceptionType exceptionType) {
		this.code = exceptionType.getCode();
		this.message = exceptionType.getMessage();
	}

	public ConstraintFailureException(String message, String code) {
		this.code = code;
		this.message = message;
	}

	public ConstraintFailureException(Throwable cause) {
		super(cause);
		this.code = "NOCODE";
		this.message = cause.toString().toString();
	}

	public ConstraintFailureException(String errorMessage) {
		super(errorMessage);
		this.code = "NOCODE";
		this.message = errorMessage;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
