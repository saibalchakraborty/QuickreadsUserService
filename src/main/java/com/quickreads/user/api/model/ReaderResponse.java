package com.quickreads.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReaderResponse {

	private String name;
	private String status; //chanage to enum later

}
