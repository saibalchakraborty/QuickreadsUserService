package com.quickreads.user.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.Welcome;
import com.quickreads.user.api.model.WelcomeResponse;
import com.quickreads.user.service.WelcomeService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/v1")
@Slf4j
public class WelcomeController {
	
	@Autowired
	private WelcomeService service;

	@GetMapping(value = "/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Welcome> welcome() {
		log.info("Incoming welcome :: ", LocalDateTime.now());
		Welcome welcome = service.welcome();
		return new ResponseEntity<Welcome>(welcome, HttpStatus.OK);
	}
	
	@PostMapping(value = "/add/welcome")
	public ResponseEntity<WelcomeResponse> createWelcome(@RequestBody Welcome welcome){
		WelcomeResponse response = service.addWelcome(welcome);
		return new ResponseEntity<WelcomeResponse>(response, HttpStatus.CREATED);
	}
}
