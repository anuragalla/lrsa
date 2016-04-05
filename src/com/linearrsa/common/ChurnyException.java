package com.linearrsa.common;

public class ChurnyException extends Exception{
	private static final long serialVersionUID = 1L;

	public ChurnyException(ExceptionType exceptionType) {
		this.code = exceptionType.getCode();
		this.message = exceptionType.getMessage();
	}

	public ChurnyException(String message, String code) {
		this.code = code;
		this.message = message;
	}

	public ChurnyException(Throwable cause) {
		super(cause);
		this.code = "NOCODE";
		this.message = cause.toString().toString();
	}

	public ChurnyException(String errorMessage) {
		super(errorMessage);
		this.code = "NOCODE";
		this.message = errorMessage;
	}

	private String code;
	private String message;

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
