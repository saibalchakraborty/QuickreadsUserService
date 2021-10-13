package com.quickreads.user.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.ReaderResponse;
import com.quickreads.user.api.model.Reader;

@RestController
@RequestMapping(value = "/v1")
public class ReaderController {
	
	private static final String SUCCESS = "Success";
	private static final String FAILURE = "Failure";
	
	Logger logger = LoggerFactory.getLogger(ReaderController.class);

	@PostMapping(value = "/createUser", 
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ReaderResponse> createNewUser(@RequestBody Reader reader) {
		try {
			return new ResponseEntity<ReaderResponse>(new ReaderResponse(reader.getFirstName() + " " + reader.getLastName(), SUCCESS),
					HttpStatus.CREATED);
		}
		catch(Exception e) {
			return new ResponseEntity<ReaderResponse>(new ReaderResponse(reader.getFirstName() + " " + reader.getLastName(), FAILURE), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
}
