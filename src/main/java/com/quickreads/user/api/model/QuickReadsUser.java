package com.quickreads.user.api.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.quickreads.user.constant.UserType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuickReadsUser {

	private String firstName;
	private String lastName;
	private UserType userType;
	private String email; //add validation
	private String phNumber; //add validation
	private String password;
}
