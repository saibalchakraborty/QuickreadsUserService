package com.quickreads.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.GenericResponse;
import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.constant.QuickReadsConstant;
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
		log.info("Adding user : {}", quickReadsUser);
		QuickReadsUserResponse createdUserResponse = service.createUser(quickReadsUser);
		return new ResponseEntity<QuickReadsUserResponse>(createdUserResponse,
				createdUserResponse.getResponseStatus().equals(QuickReadsConstant.SUCCESS) ? HttpStatus.CREATED
						: HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/getUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickReadsUserResponse> getUserDetails(@PathVariable String userId) {
			log.info("Seraching for user : {}", userId);
			QuickReadsUserResponse userResponse = service.getUser(userId);
			if(QuickReadsConstant.USER_NOT_FOUND.equals(userResponse.getResponseStatus())) {
				return new ResponseEntity<QuickReadsUserResponse>(userResponse, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<QuickReadsUserResponse>(userResponse,
					userResponse.getResponseStatus().equals(QuickReadsConstant.SUCCESS) ? HttpStatus.OK
							: HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@DeleteMapping(value = "/deleteUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteUser(@RequestParam("id") String emailId) {
			log.info("Removing user : {}", emailId);
			GenericResponse response = service.removeUser(emailId);
			return new ResponseEntity<GenericResponse>(response, QuickReadsConstant.SUCCESS.equals(response.getResponseStatus()) ? HttpStatus.ACCEPTED 
					: HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PutMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickReadsUserResponse> deleteUser(@RequestParam("id") String emailId, @RequestBody QuickReadsUser quickReadsUser) {
			log.info("Updating user ", quickReadsUser);
			QuickReadsUserResponse updatedUserResponse = service.updateUser(emailId, quickReadsUser);
			return new ResponseEntity<QuickReadsUserResponse>(updatedUserResponse, HttpStatus.OK);
	}
}
