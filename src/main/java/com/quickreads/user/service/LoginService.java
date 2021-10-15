package com.quickreads.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.quickreads.user.api.model.LoginRequest;
import com.quickreads.user.api.model.LoginResponse;
import com.quickreads.user.util.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	QuickreadsUserDetailService service;

	public LoginResponse authenticateUser(LoginRequest loginRequest){
		try {
			Authentication authenticate = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
			final UserDetails userDetails = service.loadUserByUsername(loginRequest.getUserName());
			final String jwt = jwtUtil.generateToken(userDetails);
			return LoginResponse.builder().jwt(jwt).isValidUser(authenticate.isAuthenticated()).build();
		}
		catch(Exception e) {
			log.error("Issue in authentication {}", e.getLocalizedMessage());
			return LoginResponse.builder().jwt(null).isValidUser(false).build();
		}
	}
}
