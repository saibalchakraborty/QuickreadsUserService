package com.quickreads.user.api.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Welcome {
	
	private String greetingMsg;
	private Long userCount;
	private String aboutQuickreads;

}
