package com.quickreads.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.LoginRequest;
import com.quickreads.user.api.model.LoginResponse;
import com.quickreads.user.service.LoginService;

import lombok.extern.slf4j.Slf4j;

//TODO::Implementation of JWT
@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class AuthenticationController {
	
	@Autowired
	private LoginService service;
	
	@PostMapping(value = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<LoginResponse> authenticateUser(@RequestBody LoginRequest loginRequest){
		log.info("Authenticating user : {} ", loginRequest.getUserName());
		return new ResponseEntity<LoginResponse>(service.authenticateUser(loginRequest), HttpStatus.OK);
	}
}