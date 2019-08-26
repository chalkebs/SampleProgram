package com.fundooApiNote.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class NewException extends RuntimeException
{
	private static final long serialVersionUID = 1L;

	int code;
	String statusMsg;
	
	public NewException(String statusMsg)
	{
		super(statusMsg);
	}
	
	public NewException(int code, String statusMsg)
	{
		super(statusMsg);
		this.code =code;
	}
}
