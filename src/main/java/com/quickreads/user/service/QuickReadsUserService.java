package com.quickreads.user.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.repository.QuickReadsUserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuickReadsUserService {

	@Autowired
	private QuickReadsUserRepository repository;

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FALURE";
	private static final String USER_EXISTS = "USER_EXISTS";

	public QuickReadsUserResponse createUser(QuickReadsUser user) {
		try {
			List<com.quickreads.user.repository.model.QuickReadsUser> findAll = repository.findAll();
			if(findAll.stream().anyMatch( anyUser -> anyUser.getEmail().equals(user.getEmail()))) {
				log.error("Failed saving the user : {}", USER_EXISTS);
				return QuickReadsUserResponse.builder().name(null).userName(null).userType(null).status(USER_EXISTS).build(); 
			}
			com.quickreads.user.repository.model.QuickReadsUser quickReadsUser = com.quickreads.user.repository.model.QuickReadsUser
					.builder().firstName(user.getFirstName()).lastName(user.getLastName()).userType(user.getUserType())
					.email(user.getEmail()).phNumber(user.getPhNumber()).password(user.getPassword()).build();
			com.quickreads.user.repository.model.QuickReadsUser savedUser = repository.save(quickReadsUser);
			log.info("Successfully saved user : {}", savedUser);
			return QuickReadsUserResponse.builder().name(savedUser.getFirstName() + " " + savedUser.getLastName())
					.userName(savedUser.getEmail()).userType(savedUser.getUserType()).status(SUCCESS).build();
		} catch (Exception e) {
			log.error("Failed saving the user : {}", e.getLocalizedMessage());
			return QuickReadsUserResponse.builder().name(null).userName(null).userType(null).status(FAILURE).build();
		}
	}
}
