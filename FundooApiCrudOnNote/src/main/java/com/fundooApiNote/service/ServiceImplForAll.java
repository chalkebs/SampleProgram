package com.fundooApiNote.service;

import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import com.fundooApiNote.exception.NewException;
import com.fundooApiNote.response.Response;
import com.fundooApiNote.response.ResponseOfToken;
import com.fundooApiNote.dto.Logindto;
import com.fundooApiNote.dto.Registerdto;
import com.fundooApiNote.model.User;
import com.fundooApiNote.repository.UserRepository;
import com.fundooApiNote.utility.ResponseHelper;
import com.fundooApiNote.utility.TokenGenerator;
import com.fundooApiNote.utility.Utility;

@Component
@Service("userService")
public  class ServiceImplForAll implements Services
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
			throw new NewException("User is already present...");
		}
		
		// encode user password
		String password = passwordEncoder.encode(userDto.getPassword());

		user.setPassword(password);
		user = userRepo.save(user);
		
		statusResponse = ResponseHelper.statusResponse(200, "Register successfully...");
		return statusResponse;
	}

	public ResponseOfToken UserLogin(Logindto loginDto)
	{
		Optional<User> user = userRepo.findByEmailId(loginDto.getEmailId());
		
		ResponseOfToken response = new ResponseOfToken();
		
		if (user.isPresent()) 
		{
			return authentication(user, loginDto.getPassword(), loginDto.getEmailId());
		}
		return response;
	}
	
	@Override
	public ResponseOfToken authentication(Optional<User> user, String password, String emailid) 
	{
		ResponseOfToken response = new ResponseOfToken();
		
		if(!user.get().isVerify())
		{
			String token = tokenUtil.createToken(user.get().getUserId());
			Utility.sendToken(emailid, "Token", token);
			
			response.setStatusCode(200);
			response.setStatusMessage(environment.getProperty("Successfully Logged in..."));
			response.setToken(token);
			
			return response;
		}
		else if (user.get().isVerify()) 
		{
			boolean status = passwordEncoder.matches(password, user.get().getPassword());
			
			if (status == true) 
			{				
				String token1 = tokenUtil.createToken(user.get().getUserId());
			
				Utility.sendToken(emailid, "Token", token1);
				
				response.setStatusCode(200);
				response.setStatusMessage("Successfully Logged in...");
				response.setToken(token1);
				
				return response;
			}
			throw new NewException(401, "Invalid Password...");
		}
		return response;
	}

	@Override
	public Response EmailValidation(String token) 
	{
		Long id = tokenUtil.decodeToken(token);
		
		User user = userRepo.findById(id)
				.orElseThrow(() 
				-> new NewException(404, "User Not found..."));
		
		user.setVerify(true);
		userRepo.save(user);
		
		statusResponse = ResponseHelper.statusResponse(200, "Successfully verified...");
		
		return statusResponse;
	}

}
