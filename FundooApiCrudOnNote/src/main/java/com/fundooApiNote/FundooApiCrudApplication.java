package com.fundooApiNote;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class FundooApiCrudApplication 
{
	public static void main(String[] args) 
	{
		SpringApplication.run(FundooApiCrudApplication.class, args);
	}
}
