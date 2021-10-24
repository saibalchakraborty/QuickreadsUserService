package com.quickreads.user.controller;

import java.util.List;
import java.util.Objects;

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
import org.springframework.web.bind.annotation.RestController;

import com.quickreads.user.api.model.GenericResponse;
import com.quickreads.user.api.model.Item;
import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.api.model.QuickreadsUserDetails;
import com.quickreads.user.service.QuickReadsUserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class QuickReadsUserController {

	@Autowired
	private QuickReadsUserService service;

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";

	@PostMapping(value = "/createUser", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickReadsUserResponse> createNewUser(@RequestBody QuickReadsUser quickReadsUser) {
		log.info("Adding user : {}", quickReadsUser);
		QuickReadsUserResponse createdUserResponse = service.createUser(quickReadsUser);
		return new ResponseEntity<QuickReadsUserResponse>(createdUserResponse,
				createdUserResponse.getStatus().equals(SUCCESS) ? HttpStatus.CREATED
						: HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(value = "/getUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<QuickreadsUserDetails> getUserDetails(@PathVariable String userId) {
		try {
			log.info("Seraching for user : {}", userId);
			QuickReadsUser user = service.getUser(userId);
			List<Item> itemList = service.getAllItems(userId);
			QuickreadsUserDetails userDetails = QuickreadsUserDetails.builder().quickreadsUser(user).items(itemList).build();
			if (Objects.nonNull(user.getEmail())) {
				return new ResponseEntity<QuickreadsUserDetails>(userDetails, HttpStatus.OK);
			} else {
				return new ResponseEntity<QuickreadsUserDetails>(userDetails, HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Failed getting user : ", e.getLocalizedMessage());
			return new ResponseEntity<QuickreadsUserDetails>(QuickreadsUserDetails.builder().build(), HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "/deleteUser/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteUser(String userId) {
		try {
			log.info("Removing user ", userId);
			boolean status = service.removeUser(userId);
			if (status) {
				return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(SUCCESS).build(),
						HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(FAILURE).build(),
						HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			log.error("Failed to remove user ", userId);
			return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(FAILURE).build(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping(value = "/updateUser", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GenericResponse> deleteUser(@RequestBody QuickReadsUser quickReadsUser) {
		try {
			log.info("Updating user ", quickReadsUser);
			service.updateUser(quickReadsUser);
		} catch (Exception e) {
			log.error("Failed to update user ", e.getLocalizedMessage());
			return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(FAILURE).build(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<GenericResponse>(GenericResponse.builder().responseMsg(SUCCESS).build(), HttpStatus.OK);
	}
}
