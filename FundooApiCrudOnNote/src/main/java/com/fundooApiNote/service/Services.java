package com.fundooApiNote.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fundooApiNote.exception.NewException;
import com.fundooApiNote.response.Response;
import com.fundooApiNote.response.ResponseOfToken;
import com.fundooApiNote.dto.Logindto;
import com.fundooApiNote.dto.Registerdto;
import com.fundooApiNote.model.User;

@Service
public interface Services
{
	Response UserRegistration(Registerdto userDto) throws NewException, UnsupportedEncodingException;

	ResponseOfToken UserLogin(Logindto loginDto) throws NewException, UnsupportedEncodingException;

	Response EmailValidation(String token) throws NewException;

	ResponseOfToken authentication(Optional<User> user, String password, String emailid)throws UnsupportedEncodingException, NewException;

	
}
