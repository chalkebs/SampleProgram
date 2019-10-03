package com.fundoo.response;

import org.springframework.stereotype.Component;

@Component
public class ResponseOfToken 
{
	private String statusMessage;	
	private int statusCode;
	private String token;
	private String emailId;
	private String firstName;
	private String lastName;
	
	public String getEmailId() 
	{
		return emailId;
	}

	public void setEmailId(String emailId) 
	{
		this.emailId = emailId;
	}

	public String getFirstName() 
	{
		return firstName;
	}

	public void setFirstName(String firstName) 
	{
		this.firstName = firstName;
	}

	public String getLastName() 
	{
		return lastName;
	}

	public void setLastName(String lastName) 
	{
		this.lastName = lastName;
	}
	
	public String getToken() 
	{
		return token;
	}

	public void setToken(String token) 
	{
		this.token = token;
	}

	public String getStatusMessage() 
	{
		return statusMessage;
	}
	
	public void setStatusMessage(String statusMessage) 
	{
		this.statusMessage = statusMessage;
	}
	
	public int getStatusCode() 
	{
		return statusCode;
	}
	
	public void setStatusCode(int statusCode) 
	{
		this.statusCode = statusCode;
	}
	
	
	@Override
	public String toString() {
		return "ResponseOfToken [statusMessage=" + statusMessage + ", "
				+ "statusCode=" + statusCode + ", token=" + token
				+ "]";
	}
	
}
