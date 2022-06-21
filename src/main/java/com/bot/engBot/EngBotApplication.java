package com.bot.engBot;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ComponentScan({"com.bot.engBot.bot","com.bot.engBot.service", "com.bot.engBot.job"})
public class EngBotApplication {

	final static Logger log = Logger.getLogger(EngBotApplication.class);

	public static void main(String[] args) {
		PropertyConfigurator.configure(System.getProperty("user.dir") +"/log4j.propeties");
		ConfigurableApplicationContext applicationContext = new SpringApplicationBuilder(EngBotApplication.class)
				.properties("spring.config.name:application-dev")
				.build().run(args);

	}

}
