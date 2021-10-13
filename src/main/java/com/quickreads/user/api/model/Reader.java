package com.quickreads.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Reader {

	private String firstName;
	private String lastName;
	private String email;
	private String phNumber;
	private String password;
}
