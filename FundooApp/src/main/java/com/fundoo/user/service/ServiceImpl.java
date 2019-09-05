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
import com.fundoo.user.dto.Emaildto;
import com.fundoo.user.dto.Logindto;
import com.fundoo.user.dto.Registerdto;
import com.fundoo.user.model.UserEntity;
import com.fundoo.user.repository.UserRepository;
import com.fundoo.utility.ResponseHelper;
import com.fundoo.utility.TokenGenerator;
import com.fundoo.utility.Utility;

@Component
@SuppressWarnings("unused")
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
		UserEntity user = modelMapper.map(userDto, UserEntity.class);

		Optional<UserEntity> alreadyPresent = userRepo.findByEmailId(user.getEmailId());
		
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
			
			statusResponse = ResponseHelper.statusResponse(200, "Registered Successfully");
			return statusResponse;
		}
		
	}

	public Response UserLogin(Logindto loginDto)
	{
		Optional<UserEntity> user = userRepo.findByEmailId(loginDto.getEmailId());
		
		Response response = new Response();
		
		if (user.isPresent()) 
		{
			return authentication(user, loginDto.getPassword());
		}
		else
		{
			response.setStatusCode(402);
			response.setStatusMessage("User Not Found");
			
			return response;
		}
			
	}
	
	@Override
	public Response authentication(Optional<UserEntity> user, String password) 
	{
		Response response = new Response();
		
		if(!user.get().isVerify())
		{
			response.setStatusCode(401);
			response.setStatusMessage("Verification Required");
			
			return response;
		}
		else if (user.get().isVerify()) 
		{
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			
			if (status == true) 
			{
				response.setStatusCode(200);
				response.setStatusMessage("Successfully Logged In");
				
				return response;
			}
			else
			{
				response.setStatusCode(403);
				response.setStatusMessage("Invalid password");
				
				return response;
			}
		}
		return response;
	}

	@Override
	public Response EmailValidation(String token) 
	{
		Long id = tokenUtil.decodeToken(token);

		UserEntity user = userRepo.findById(id)
				.orElseThrow(() -> new UserException(404, "User Not found"));
		
		user.setVerify(true);
		userRepo.save(user);
		
		statusResponse = ResponseHelper.statusResponse(200, "Successfully verified");
		
		return statusResponse;
	}
	

	@Override
	public Response forgotPassword(Emaildto emailDto) throws UserException, UnsupportedEncodingException 
	{
		Optional<UserEntity> alreadyPresent=userRepo.findByEmailId(emailDto.getEmailId());
		
		if(!alreadyPresent.isPresent()) 
		{
			return ResponseHelper.statusResponse(401, "User Not Found");
		}
		else
		{
			Utility.send(emailDto.getEmailId(),"Password Reset Link...", "http://localhost:4200/resetpassword");
			return ResponseHelper.statusResponse(200, "Password Reset Link is Sent to your Email Id...");
		}
		
	}

	@Override
	public Response resetPassword(String emailId, String password) throws UserException 
	{
		Optional<UserEntity> alreadyPresent=userRepo.findByEmailId(emailId);
		if(!alreadyPresent.isPresent())
		{
			return ResponseHelper.statusResponse(401, "Email Id is not present...");
		}
		else
		{
			Long id=alreadyPresent.get().getUserId();
			UserEntity user=userRepo.findById(id).orElseThrow(()-> new 
					UserException(404,"User Not found"));
			String encodedPasword=passwordEncoder.encode(password);
			user.setPassword(encodedPasword);
			userRepo.save(user);
			return ResponseHelper.statusResponse(200, "Password Successfully Reset");
		}
	}

}
