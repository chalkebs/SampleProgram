package com.fundooApiNote.dto;

import lombok.Data;

@Data
public class Emaildto {
	private String emailId;
	
	public void setEmailId(String emailId)
	{
		this.emailId = emailId;
	}
	
	public String getEmailId() {

		return emailId;
	}
}
