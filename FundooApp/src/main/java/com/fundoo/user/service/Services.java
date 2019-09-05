package com.fundoo.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import com.fundoo.exception.UserException;
import com.fundoo.response.Response;
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.Logindto;
import com.fundoo.user.dto.Registerdto;
import com.fundoo.user.model.UserEntity;

public interface Services
{
	Response UserRegistration(Registerdto userDto) throws UserException, UnsupportedEncodingException;

	Response UserLogin(Logindto loginDto) throws UserException, UnsupportedEncodingException;

	Response EmailValidation(String token) throws UserException;

	Response forgotPassword(Emaildto emailDto) throws UserException, UnsupportedEncodingException;

	Response resetPassword(String emailid, String password) throws UserException;

	Response authentication(Optional<UserEntity> user, String password)
			throws UnsupportedEncodingException, UserException;

}
