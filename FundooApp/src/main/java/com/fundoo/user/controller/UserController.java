package com.fundoo.user.controller;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

import javax.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fundoo.exception.UserException;
import com.fundoo.response.Response;
import com.fundoo.response.ResponseOfToken;
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.Logindto;
import com.fundoo.user.dto.Registerdto;
import com.fundoo.user.model.UserEntity;
import com.fundoo.user.repository.UserRepository;
import com.fundoo.user.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/users")
public class UserController 
{
	@Autowired
	UserService service;

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

	@GetMapping(value = "/{token}")
	public ResponseEntity<Response> emailValidation(@PathVariable String token) throws UserException 
	{
		Response response = service.EmailValidation(token);
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody Emaildto emailDto)
			throws UnsupportedEncodingException, UserException, MessagingException 
	{ 
		Response status = service.forgotPassword(emailDto);
		return new ResponseEntity<Response>(status, HttpStatus.OK);
	}

	@PutMapping("/resetpassword/")
	public ResponseEntity<?> resetPassword(@RequestBody UserEntity user, @RequestHeader String token)
			throws UserException 
	{
		Response response = service.resetPassword(token ,user.getPassword());
		return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@PostMapping("/uploadImage/")
    public ResponseEntity<Response> uploadFile(@RequestHeader String token, @RequestParam MultipartFile file) 
	{
		Response response  = service.storeFile(token, file);
        return new ResponseEntity<Response>(response, HttpStatus.OK);
	}
	
	@GetMapping("/showProfilePic/")
	public Resource showProfilePic(@RequestHeader String token) throws MalformedURLException 
	{
		Resource resource  = service.displayProfilePic(token);
        return resource;
	}
	
}

