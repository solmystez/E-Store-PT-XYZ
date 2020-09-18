package com.demo.lookopediaSinarmas.exceptions.comment;

public class CommentNotFountExceptionResponse {
	
	private String commentMessage;

	public CommentNotFountExceptionResponse(String commentMessage) {
		this.commentMessage = commentMessage;
	}
	
	public String getCommentMessage() {
		return commentMessage;
	}

	public void setCommentMessage(String commentMessage) {
		this.commentMessage = commentMessage;
	}	
}
