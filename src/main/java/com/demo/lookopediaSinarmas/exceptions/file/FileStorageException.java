package com.demo.lookopediaSinarmas.exceptions.file;

public class FileStorageException extends RuntimeException{

	public static final long serialVersionUID = 1L;
	
	public FileStorageException(String message) {
		super(message);
	}
	
	public FileStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
