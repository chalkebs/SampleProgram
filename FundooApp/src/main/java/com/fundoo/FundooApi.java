package com.fundoo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import com.fundoo.user.model.UserEntity;

@SpringBootApplication
@EnableJpaAuditing
@ComponentScan(basePackages = "com.fundoo")
@EnableConfigurationProperties({
    UserEntity.class
})
public class FundooApi 
{
	
	public static void main(String[] args)
	{
		SpringApplication.run(FundooApi.class, args);
	}
	
}
