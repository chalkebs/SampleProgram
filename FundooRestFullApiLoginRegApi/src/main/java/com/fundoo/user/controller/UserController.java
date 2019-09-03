package com.fundoo.user.controller;

import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundoo.exception.UserException;
import com.fundoo.response.Response;
import com.fundoo.response.ResponseOfToken;
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.Logindto;
import com.fundoo.user.dto.Registerdto;
import com.fundoo.user.model.User;
import com.fundoo.user.repository.UserRepository;
import com.fundoo.user.service.Services;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	Services service;

	@Autowired
	UserRepository userRepo;
	
	@PostMapping
	public ResponseEntity<Response> register(@RequestBody Registerdto userDto)
			throws UserException, UnsupportedEncodingException 
	{
		Response response = service.UserRegistration(userDto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<ResponseOfToken> onLogin(@RequestBody Logindto logindto)
			throws UserException, UnsupportedEncodingException 
	{
		ResponseOfToken response = service.UserLogin(logindto);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	// to verify Verification Required
	@GetMapping(value = "/")
	public ResponseEntity<Response> emailValidation(@RequestHeader String token) throws UserException 
	{
		System.out.println("UserController.emailValidation()");
		Response response = service.EmailValidation(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	// for forget password
	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody Emaildto emailDto)
			throws UnsupportedEncodingException, UserException, MessagingException 
	{ 
		Response status = service.forgotPassword(emailDto);
		return new ResponseEntity<Response>(status, HttpStatus.OK);
	}

	@PutMapping("/resetpassword")
	public ResponseEntity<?> resetPassword(@RequestBody User user)
			throws UserException 
	{
		Response response = service.resetPassword(user.getEmailId(),user.getPassword());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

}
