package com.quickreads.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.service.QuickReadsUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class QuickReadsUserController {

	@Autowired
	private QuickReadsUserService service;

	@PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickReadsUserResponse> createNewUser(@RequestBody QuickReadsUser quickReadsUser) {
		try {
			QuickReadsUserResponse createdUserResponse = service.createUser(quickReadsUser);
			return new ResponseEntity<QuickReadsUserResponse>(createdUserResponse, HttpStatus.CREATED);
		} catch (Exception e) {
			log.error("Failed in controller : {}", e.getLocalizedMessage());
			return new ResponseEntity<QuickReadsUserResponse>(new QuickReadsUserResponse(), HttpStatus.OK);
		}
	}
}
