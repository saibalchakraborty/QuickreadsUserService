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
		try {
			log.info("Seraching for user : {}", userId);
			QuickReadsUserResponse userResponse = service.getUser(userId);
			if(QuickReadsConstant.USER_NOT_FOUND.equals(userResponse.getResponseStatus())) {
				return new ResponseEntity<QuickReadsUserResponse>(userResponse, HttpStatus.NOT_FOUND);
			}
			return new ResponseEntity<QuickReadsUserResponse>(userResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Failed getting user : ", e.getLocalizedMessage());
			return new ResponseEntity<QuickReadsUserResponse>(QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.RUN_TIME_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping(value = "/deleteUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteUser(@RequestParam("id") String emailId) {
		try {
			log.info("Removing user : {}", emailId);
			boolean status = service.removeUser(emailId);
			if (status) {
				return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(QuickReadsConstant.SUCCESS).build(),
						HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(QuickReadsConstant.FAILURE).build(),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Failed to remove user ", emailId);
			return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(QuickReadsConstant.FAILURE).build(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickReadsUserResponse> deleteUser(@RequestParam("id") String emailId, @RequestBody QuickReadsUser quickReadsUser) {
		try {
			log.info("Updating user ", quickReadsUser);
			QuickReadsUserResponse updatedUserResponse = service.updateUser(emailId, quickReadsUser);
			return new ResponseEntity<QuickReadsUserResponse>(updatedUserResponse, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Failed to update user ", e.getLocalizedMessage());
			return new ResponseEntity<QuickReadsUserResponse>(QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.RUN_TIME_ERROR).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
