package com.fundoo.user.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

import javax.tools.JavaFileManager.Location;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fundoo.exception.UserException;
import com.fundoo.response.Response;
import com.fundoo.response.ResponseOfToken;
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.Logindto;
import com.fundoo.user.dto.Registerdto;
import com.fundoo.user.model.User;
import com.fundoo.user.repository.UserRepository;
import com.fundoo.utility.ResponseHelper;
import com.fundoo.utility.TokenGenerator;
import com.fundoo.utility.Utility;

@Component
@SuppressWarnings("unused")
@PropertySource("classpath:message.properties")
@Service
public  class ServiceImpl implements Services
{	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private TokenGenerator tokenUtil;

	@Autowired
	private Response statusResponse;
	
	@Autowired
	Utility utility;

	@Autowired
	private Environment environment;
	
	@Override
	public Response UserRegistration(Registerdto userDto) 
	{
		User user = modelMapper.map(userDto, User.class);

		Optional<User> alreadyPresent = userRepo.findByEmailId(user.getEmailId());
		
		if (alreadyPresent.isPresent()) 
		{
			statusResponse = ResponseHelper.statusResponse(401, "User already registered");
			return statusResponse;		
		}
		else
		{
			String password = passwordEncoder.encode(userDto.getPassword());

			user.setPassword(password);
			user = userRepo.save(user);
			
			String token = tokenUtil.createToken(user.getUserId());
			Utility.sendConfirmationEmail(user.getEmailId(),
					"Registered Successfully!!! Please verify the Email...",
					"Click on below link for verification \n http://localhost:8080/users/"+token);
			
			statusResponse = ResponseHelper.statusResponse(200, "register successfully");
			return statusResponse;
		}
		
	}

	public ResponseOfToken UserLogin(Logindto loginDto)
	{
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		
		ResponseOfToken response = new ResponseOfToken();
		
		if (user.isPresent()) 
		{
			return authentication(user, loginDto.getPassword());
		}
		return response;
	}
	
	@Override
	public ResponseOfToken authentication(Optional<User> user, String password) 
	{
		ResponseOfToken response = new ResponseOfToken();
		
		if(!user.get().isVerify())
		{
			response.setStatusCode(401);
			response.setStatusMessage(environment.getProperty("user.login.verification"));
			
			return response;
		}
		else if (user.get().isVerify()) 
		{
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			
			if (status == true) 
			{
				response.setStatusCode(200);
				response.setStatusMessage(environment.getProperty("user.login"));
				
				return response;
			}
			throw new UserException(401, environment.getProperty("user.login.password"));
		}
		return response;
	}

	@Override
	public Response EmailValidation(String token) 
	{
		Long id = tokenUtil.decodeToken(token);
		
		User user = userRepo.findById(id)
				.orElseThrow(() -> new UserException(404, environment.getProperty("user.validation.email")));
		
		user.setVerify(true);
		userRepo.save(user);
		
		statusResponse = ResponseHelper.statusResponse(200, environment.getProperty("user.validation"));
		
		return statusResponse;
	}
	

	@Override
	public Response forgotPassword(Emaildto emailDto) throws UserException, UnsupportedEncodingException 
	{
		Optional<User> alreadyPresent=userRepo.findByEmailId(emailDto.getEmailId());
		
		if(!alreadyPresent.isPresent()) 
		{
			throw new UserException(401,environment.getProperty("user.forgetPassword.emailId"));
		}
		else
		{
			Utility.send(emailDto.getEmailId(),"Password Reset Link...", "http://localhost:4200/resetpassword");
			return ResponseHelper.statusResponse(200, environment.getProperty("user.forgetpassword.link"));
		}
		
	}

	@Override
	public Response resetPassword(String emailId, String password) throws UserException 
	{
		Optional<User> alreadyPresent=userRepo.findByEmailId(emailId);
		if(alreadyPresent==null)
		{
			return ResponseHelper.statusResponse(401, "Email Id is not present...");
		}
		else
		{
			Long id=alreadyPresent.get().getUserId();
			User user=userRepo.findById(id).orElseThrow(()-> new 
					UserException(404,environment.getProperty("user.resetpassword.user")));
			String encodedPasword=passwordEncoder.encode(password);
			user.setPassword(encodedPasword);
			userRepo.save(user);
			return ResponseHelper.statusResponse(200, "password successfully reset");
		}
	}

}
