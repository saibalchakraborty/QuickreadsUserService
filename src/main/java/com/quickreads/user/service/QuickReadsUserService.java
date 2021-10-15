package com.quickreads.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.repository.QuickReadsUserRepository;

@Service
public class QuickReadsUserService {

	@Autowired
	private QuickReadsUserRepository repository;
	
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FALURE";
	
	private final Logger logger = LoggerFactory.getLogger(QuickReadsUserService.class);
	
	public QuickReadsUserResponse createUser(QuickReadsUser user) {
		try{
			com.quickreads.user.repository.model.QuickReadsUser quickReadsUser = com.quickreads.user.repository.model.QuickReadsUser.builder()
																			.firstName(user.getFirstName()).lastName(user.getLastName())
																			.userType(user.getUserType()).email(user.getEmail())
																			.phNumber(user.getPhNumber()).password(user.getPassword()).build();
		com.quickreads.user.repository.model.QuickReadsUser savedUser = repository.save(quickReadsUser);
		logger.info("Successfully saved user : " + savedUser);
		return QuickReadsUserResponse.builder().name(savedUser.getFirstName()+" "+savedUser.getLastName()).userName(savedUser.getEmail())
				.userType(savedUser.getUserType()).status(SUCCESS).build();
		}
		catch(Exception e) {
			logger.error("Failed saving the user : " + e.getLocalizedMessage());
			return QuickReadsUserResponse.builder().name(null).userName(null).userType(null).status(FAILURE).build();
		}
	}
}
