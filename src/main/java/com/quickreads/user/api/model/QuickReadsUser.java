package com.quickreads.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickReadsUser {

	private String firstName;
	private String lastName;
	private String userType; //later change to ENUM
	private String email; //add validation
	private String phNumber; //add validation
	private String password;
}
