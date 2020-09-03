package com.lucasmoura.ilegra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.lucasmoura.ilegra.service.WatcherService;

@SpringBootApplication
public class IlegraApplication implements ApplicationRunner {
    
	@Autowired
	private WatcherService watcherService;

	public static void main(String[] args) {
		SpringApplication.run(IlegraApplication.class, args);	
	}
	
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		this.watcherService.start();
	}

}
