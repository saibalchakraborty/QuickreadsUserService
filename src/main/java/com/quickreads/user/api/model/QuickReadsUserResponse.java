package com.quickreads.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuickReadsUserResponse {

	private String name;
	private String userName;
	private String userType;
	private String status;

}
