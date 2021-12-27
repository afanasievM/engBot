package com.bot.engBot;

import com.bot.engBot.bot.Bot;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import com.bot.engBot.service.BotUserService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan({"com.bot.engBot.bot","com.bot.engBot.service", "com.bot.engBot.job"})
public class EngBotApplication {

	final static Logger log = Logger.getLogger(EngBotApplication.class);

	public static void main(String[] args) throws InterruptedException {
		PropertyConfigurator.configure(System.getProperty("user.dir") +"/log4j.propeties");
//		SpringApplication.run(EngBotApplication.class, args);
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(EngBotApplication.class)
				.properties("spring.config.name:application-dev")
				.build().run(args);

//		ConfigurableEnvironment environment = applicationContext.getEnvironment();

	}

}
