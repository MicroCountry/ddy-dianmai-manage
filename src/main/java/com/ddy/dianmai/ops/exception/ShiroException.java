package com.ddy.dianmai.ops.exception;

public class ShiroException extends RuntimeException{
	
	private static final long serialVersionUID = 6794838795960651420L;

	public ShiroException(){
		super();
	}

	public ShiroException(String message){
		super(message);
	}

	public ShiroException(Throwable throwable){
		super(throwable);
	}

	public ShiroException(String message, Throwable throwable){
		super(message, throwable);
	}
}
