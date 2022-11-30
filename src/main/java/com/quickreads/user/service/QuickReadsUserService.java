package com.quickreads.user.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.GenericResponse;
import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.constant.QuickReadsConstant;
import com.quickreads.user.constant.UserStatus;
import com.quickreads.user.constant.UserType;
import com.quickreads.user.repository.QuickReadsUserRepository;
import com.quickreads.user.util.PasswordEncrypter;

import ch.qos.logback.classic.Logger;
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
				return QuickReadsUserResponse.builder()
						.responseStatus(QuickReadsConstant.USER_EXISTS.concat(" : " + user.getEmail()))
						.responseMsg(user.getEmail() + " already exists!")
						.build();
			}
			final String encodedPassword = passEncrypter.encryptPassword(user.getPassword());
			com.quickreads.user.repository.model.QuickReadsUser quickReadsUser = com.quickreads.user.repository.model.QuickReadsUser
					.builder().firstName(user.getFirstName()).lastName(user.getLastName()).userType(user.getUserType().toString())
					.email(user.getEmail()).phNumber(user.getPhNumber()).status(UserStatus.ACTIVE.toString()).password(encodedPassword).build();
			com.quickreads.user.repository.model.QuickReadsUser savedUser = userRepository.save(quickReadsUser);
			log.info("Successfully saved user : {}", savedUser);
			return generateQuickReadsUserResponse(savedUser, "Successfully saved user : " + savedUser.getEmail());
		} catch (Exception e) {
			log.error("Failed saving the user : {}", e.getLocalizedMessage());
			return QuickReadsUserResponse.builder()
					.responseStatus(QuickReadsConstant.RUN_TIME_ERROR)
					.responseMsg("Failed in adding user : " + user.getEmail() + " due to : " + e.getMessage())
					.build();
		}
	}

	/**
	 * Used to get user information from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public QuickReadsUserResponse getUser(String userId) {
		try {
			com.quickreads.user.repository.model.QuickReadsUser quickreadsUser = userRepository.getQuickreadsUser(userId, UserStatus.ACTIVE.toString());
			if (Objects.isNull(quickreadsUser)) {
				log.error("No active user found with id : {}", userId);
				return QuickReadsUserResponse.builder()
						.responseStatus(QuickReadsConstant.USER_NOT_FOUND)
						.responseMsg(userId + " does not exist!")
						.build();
			}
			log.info("User found with details : {}", quickreadsUser);
			return generateQuickReadsUserResponse(quickreadsUser, "Successfully found user : " + quickreadsUser.getEmail());
		}
		catch(Exception e) {
			log.error("Failure in finding user : {} due to : {}", userId, e.getMessage());
			return QuickReadsUserResponse.builder()
					.responseStatus(QuickReadsConstant.RUN_TIME_ERROR)
					.responseMsg("Failed in retrieving user : " + userId + " due to : " + e.getMessage())
					.build();
		}
	}

	/**
	 * Used to remove from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public GenericResponse removeUser(String userId) {
		try {
			int updationResult = userRepository.updateQuickreadsUser(UserStatus.INACTIVE.toString(), userId);
			log.info("Updation result of the user : {} is : {}", userId, updationResult);
			if (updationResult == 0) {
				log.error("No user found for deletion with id : {}", userId);
				GenericResponse.builder()
					.responseStatus(QuickReadsConstant.FAILURE)
					.responseMsg("Could not remove " + userId + ", as it is not present in system!")
					.build();
			}
		}
		catch(Exception e) {
			log.error("Failure in deleting user : {} due to : {}", userId, e.getMessage());
			GenericResponse.builder()
			.responseStatus(QuickReadsConstant.FAILURE)
			.responseMsg("Could not remove " + userId + "due to : " + e.getMessage())
			.build();
		}
		return GenericResponse.builder()
				.responseStatus(QuickReadsConstant.SUCCESS)
				.responseMsg("Successfully removed " + userId)
				.build();
	}

	/**
	 * Used to update user
	 * 
	 * @param quickreadsUser
	 * @return
	 * @throws Exception
	 */
	public QuickReadsUserResponse updateUser(String emailId, QuickReadsUser quickreadsUser) throws Exception {
		try {
			com.quickreads.user.repository.model.QuickReadsUser user = userRepository.getQuickreadsUser(emailId, UserStatus.ACTIVE.toString());
			if(Objects.nonNull(user)) {
				user.setFirstName(
						(quickreadsUser.getFirstName() == null) ? user.getFirstName() : quickreadsUser.getFirstName());
				user.setLastName((quickreadsUser.getLastName() == null) ? user.getLastName() : quickreadsUser.getLastName());
				user.setUserType((quickreadsUser.getUserType() == null) ? user.getUserType() : quickreadsUser.getUserType().toString());
				if(quickreadsUser.getEmail() == null || quickreadsUser.getEmail().equals(emailId)) {
					user.setEmail((quickreadsUser.getEmail() == null) ? user.getEmail() : quickreadsUser.getEmail());
				}
				else {
					log.error("Can not update email id : {}", emailId);
					return QuickReadsUserResponse.builder()
							.responseStatus(QuickReadsConstant.NOT_ALLOWED)
							.responseMsg("Email Id updation not allowed")
							.build();
				}
				user.setPhNumber((quickreadsUser.getPhNumber() == null) ? user.getPhNumber() : quickreadsUser.getPhNumber());
				user.setPassword((quickreadsUser.getPassword() == null) ? user.getPassword() : passEncrypter.encryptPassword(quickreadsUser.getPassword()));
				com.quickreads.user.repository.model.QuickReadsUser updatedUser = userRepository.save(user);
				log.info("Updated user details for id : {} are : {}", emailId, updatedUser);
				return generateQuickReadsUserResponse(updatedUser, "Succesfully updated user : " + emailId);
			}
			else {
				log.error("No active user found to update with id : {}", emailId);
				return QuickReadsUserResponse.builder()
						.responseStatus(QuickReadsConstant.USER_NOT_FOUND)
						.responseMsg(emailId + " does not exist for updation!")
						.build();
			}
		}
		catch(Exception e) {
			log.error("Failure in updating user : {} due to : {}", emailId, e.getMessage());
			return QuickReadsUserResponse.builder()
					.responseStatus(QuickReadsConstant.RUN_TIME_ERROR)
					.responseMsg("Failed in updating user : " + emailId + " due to : " + e.getMessage())
					.build();
		}
	}
	
	private QuickReadsUserResponse generateQuickReadsUserResponse(com.quickreads.user.repository.model.QuickReadsUser quickreadsUser, String responseMsg) {
		return QuickReadsUserResponse.builder()
				.name(quickreadsUser.getFirstName() + " " + quickreadsUser.getLastName())
				.userName(quickreadsUser.getEmail())
				.userType(UserType.valueOf(quickreadsUser.getUserType()))
				.responseStatus(QuickReadsConstant.SUCCESS)
				.responseMsg(responseMsg)
				.build();
	}
}
