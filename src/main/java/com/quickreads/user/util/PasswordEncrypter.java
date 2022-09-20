package com.quickreads.user.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncrypter {

	@Autowired
	private PasswordEncoder encoder;
	
	public String encryptPassword(String password) {
		return encoder.encode(password);
	}
}
