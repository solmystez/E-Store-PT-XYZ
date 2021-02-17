package com.demo.eStore.exceptions.comment;

public class CommentNotFoundException extends RuntimeException{

	public CommentNotFoundException(String message) {
		super(message);
	}
}
