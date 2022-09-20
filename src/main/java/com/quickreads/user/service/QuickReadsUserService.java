package com.quickreads.user.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.constant.QuickReadsConstant;
import com.quickreads.user.constant.UserStatus;
import com.quickreads.user.constant.UserType;
import com.quickreads.user.repository.QuickReadsUserRepository;
import com.quickreads.user.util.PasswordEncrypter;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuickReadsUserService {

	@Autowired
	private QuickReadsUserRepository userRepository;
	@Autowired
	private PasswordEncrypter passEncrypter;

	/**
	 * Used to add a new user into the system
	 * 
	 * @param user
	 * @return
	 */
	public QuickReadsUserResponse createUser(QuickReadsUser user) {
		try {
			com.quickreads.user.repository.model.QuickReadsUser anyExistingUser = userRepository.getQuickreadsUser(user.getEmail(), UserStatus.ACTIVE.toString());
			if (Objects.nonNull(anyExistingUser)) {
				log.error("Failed saving the user : {} due to : {}", user.getEmail(), QuickReadsConstant.USER_EXISTS);
				return QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.USER_EXISTS.concat(" : " + user.getEmail())).build();
			}
			final String encodedPassword = passEncrypter.encryptPassword(user.getPassword());
			com.quickreads.user.repository.model.QuickReadsUser quickReadsUser = com.quickreads.user.repository.model.QuickReadsUser
					.builder().firstName(user.getFirstName()).lastName(user.getLastName()).userType(user.getUserType().toString())
					.email(user.getEmail()).phNumber(user.getPhNumber()).status(UserStatus.ACTIVE.toString()).password(encodedPassword).build();
			com.quickreads.user.repository.model.QuickReadsUser savedUser = userRepository.save(quickReadsUser);
			log.info("Successfully saved user : {}", savedUser);
			return QuickReadsUserResponse.builder().name(savedUser.getFirstName() + " " + savedUser.getLastName())
					.userName(savedUser.getEmail()).userType(UserType.valueOf(savedUser.getUserType())).responseStatus(QuickReadsConstant.SUCCESS).build();
		} catch (Exception e) {
			log.error("Failed saving the user : {}", e.getLocalizedMessage());
			return QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.RUN_TIME_ERROR).build();
		}
	}

	/**
	 * Used to get user information from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public QuickReadsUserResponse getUser(String userId) throws Exception {
		com.quickreads.user.repository.model.QuickReadsUser quickreadsUser = userRepository.getQuickreadsUser(userId, UserStatus.ACTIVE.toString());
		if (Objects.isNull(quickreadsUser)) {
			log.error("No active user found with id : {}", userId);
			return QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.USER_NOT_FOUND).build();
		}
		log.info("User found with details : {}", quickreadsUser);
		return QuickReadsUserResponse.builder().name(quickreadsUser.getFirstName() + " " + quickreadsUser.getLastName())
				.userName(quickreadsUser.getEmail()).userType(UserType.valueOf(quickreadsUser.getUserType())).responseStatus(QuickReadsConstant.SUCCESS).build();
	}

	/**
	 * Used to remove from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean removeUser(String userId) throws Exception {
		int updationResult = userRepository.updateQuickreadsUser(UserStatus.INACTIVE.toString(), userId);
		log.info("Updation result of the user : {} is : {}", userId, updationResult);
		if(updationResult == 0) {
			log.error("No user found for deletion with id : {}", userId);
			return false;
		}
		return true;
	}

	/**
	 * Used to update user
	 * 
	 * @param quickreadsUser
	 * @return
	 * @throws Exception
	 */
	public QuickReadsUserResponse updateUser(String emailId, QuickReadsUser quickreadsUser) throws Exception {
		com.quickreads.user.repository.model.QuickReadsUser user = userRepository
				.getQuickreadsUser(emailId, UserStatus.ACTIVE.toString());
		if(Objects.nonNull(user)) {
			user.setFirstName(
					(quickreadsUser.getFirstName() == null) ? user.getFirstName() : quickreadsUser.getFirstName());
			user.setLastName((quickreadsUser.getLastName() == null) ? user.getLastName() : quickreadsUser.getLastName());
			user.setUserType((quickreadsUser.getUserType() == null) ? user.getUserType() : quickreadsUser.getUserType().toString());
			user.setEmail((quickreadsUser.getEmail() == null) ? user.getEmail() : quickreadsUser.getEmail());
			user.setPhNumber((quickreadsUser.getPhNumber() == null) ? user.getPhNumber() : quickreadsUser.getPhNumber());
			user.setPassword((quickreadsUser.getPassword() == null) ? user.getPassword() : passEncrypter.encryptPassword(quickreadsUser.getPassword()));
			com.quickreads.user.repository.model.QuickReadsUser updatedUser = userRepository.save(user);
			log.info("Updated user details for id : {} are : {}", emailId, updatedUser);
			return generateQuickReadsUserResponse(updatedUser);
		}
		else {
			log.error("No active user found with id : {} to update", emailId);
			return QuickReadsUserResponse.builder().responseStatus(QuickReadsConstant.USER_NOT_FOUND).build();
		}
	}
	
	private QuickReadsUserResponse generateQuickReadsUserResponse(com.quickreads.user.repository.model.QuickReadsUser quickreadsUser) {
		return QuickReadsUserResponse.builder().name(quickreadsUser.getFirstName() + " " + quickreadsUser.getLastName())
				.userName(quickreadsUser.getEmail()).userType(UserType.valueOf(quickreadsUser.getUserType())).responseStatus(QuickReadsConstant.SUCCESS).build();
	}
}
