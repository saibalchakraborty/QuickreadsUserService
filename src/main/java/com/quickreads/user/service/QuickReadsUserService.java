package com.quickreads.user.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.Item;
import com.quickreads.user.api.model.QuickReadsUser;
import com.quickreads.user.api.model.QuickReadsUserResponse;
import com.quickreads.user.repository.QuickReadsUserRepository;
import com.quickreads.user.repository.QuickreadsItemRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuickReadsUserService {

	@Autowired
	private QuickReadsUserRepository userRepository;
	@Autowired
	private QuickreadsItemRepository itemrepository;

	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FALURE";
	private static final String USER_EXISTS = "USER_EXISTS";

	/**
	 * Used to add a new user into the system
	 * 
	 * @param user
	 * @return
	 */
	public QuickReadsUserResponse createUser(QuickReadsUser user) {
		try {
			List<com.quickreads.user.repository.model.QuickReadsUser> findAll = userRepository.findAll();
			if (findAll.stream().anyMatch(anyUser -> anyUser.getEmail().equals(user.getEmail()))) {
				log.error("Failed saving the user : {}", USER_EXISTS);
				return QuickReadsUserResponse.builder().name(null).userName(null).userType(null).status(USER_EXISTS)
						.build();
			}
			com.quickreads.user.repository.model.QuickReadsUser quickReadsUser = com.quickreads.user.repository.model.QuickReadsUser
					.builder().firstName(user.getFirstName()).lastName(user.getLastName()).userType(user.getUserType())
					.email(user.getEmail()).phNumber(user.getPhNumber()).password(user.getPassword()).build();
			com.quickreads.user.repository.model.QuickReadsUser savedUser = userRepository.save(quickReadsUser);
			log.info("Successfully saved user : {}", savedUser);
			return QuickReadsUserResponse.builder().name(savedUser.getFirstName() + " " + savedUser.getLastName())
					.userName(savedUser.getEmail()).userType(savedUser.getUserType()).status(SUCCESS).build();
		} catch (Exception e) {
			log.error("Failed saving the user : {}", e.getLocalizedMessage());
			return QuickReadsUserResponse.builder().name(null).userName(null).userType(null).status(FAILURE).build();
		}
	}

	/**
	 * Used to get user information from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public QuickReadsUser getUser(String userId) throws Exception {
		com.quickreads.user.repository.model.QuickReadsUser quickreadsUser = userRepository.getQuickreadsUser(userId);
		if (Objects.nonNull(quickreadsUser)) {
			return QuickReadsUser.builder().firstName(quickreadsUser.getFirstName())
					.lastName(quickreadsUser.getLastName()).userType(quickreadsUser.getUserType())
					.email(quickreadsUser.getEmail()).phNumber(quickreadsUser.getPhNumber()).build();
		}
		return QuickReadsUser.builder().build();
	}

	/**
	 * Used to remove from system
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	public boolean removeUser(String userId) throws Exception {
		// TODO: Later dont remove user from system just make status as inactive
		return userRepository.deleteQuickreadsUser(userId);
	}

	/**
	 * Used to retrieve all items of a user
	 * 
	 * @param userName
	 * @return
	 * @throws Exception
	 */
	public List<Item> getAllItems(String userName) throws Exception {
		List<com.quickreads.user.repository.model.Item> items = itemrepository.getItems(userName);
		List<Item> allItems = new ArrayList<>();
		if (Objects.nonNull(items)) {
			items.stream().forEach(
					item -> allItems.add(Item.builder().itemName(item.getItemName()).type(item.getType()).email(item.getEmail()).build()));
		}
		return allItems;
	}

	/**
	 * Used to update user
	 * 
	 * @param quickreadsUser
	 * @return
	 * @throws Exception
	 */
	public void updateUser(QuickReadsUser quickreadsUser) throws Exception {
		com.quickreads.user.repository.model.QuickReadsUser user = userRepository
				.getQuickreadsUser(quickreadsUser.getEmail());
		user.setFirstName(
				(quickreadsUser.getFirstName() == null) ? user.getFirstName() : quickreadsUser.getFirstName());
		user.setLastName((quickreadsUser.getLastName() == null) ? user.getLastName() : quickreadsUser.getLastName());
		user.setUserType((quickreadsUser.getUserType() == null) ? user.getUserType() : quickreadsUser.getUserType());
		user.setEmail((quickreadsUser.getEmail() == null) ? user.getEmail() : quickreadsUser.getEmail());
		user.setPhNumber((quickreadsUser.getPhNumber() == null) ? user.getPhNumber() : quickreadsUser.getPhNumber());
		user.setPassword((quickreadsUser.getPassword() == null) ? user.getPassword() : quickreadsUser.getPassword());
		userRepository.save(user);
	}
}
