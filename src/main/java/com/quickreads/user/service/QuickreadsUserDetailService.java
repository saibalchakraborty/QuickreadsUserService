package com.quickreads.user.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.quickreads.user.constant.UserStatus;
import com.quickreads.user.repository.QuickReadsUserRepository;
import com.quickreads.user.repository.model.QuickReadsUser;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuickreadsUserDetailService implements UserDetailsService{
	
	@Autowired
	private QuickReadsUserRepository repository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		QuickReadsUser quickreadsUser = null;
		try {
			log.info("searching database for user : {}", username);
			quickreadsUser = repository.getQuickreadsUser(username, UserStatus.ACTIVE.toString());
		}
		catch(Exception e) {
			log.error("Failed getting user with error {}", e.getLocalizedMessage());
		}
		return new User(quickreadsUser.getEmail(), quickreadsUser.getPassword(), new ArrayList<>());
	}

}
