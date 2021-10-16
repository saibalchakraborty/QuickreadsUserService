package com.quickreads.user.service;

import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.quickreads.user.api.model.Welcome;
import com.quickreads.user.api.model.WelcomeResponse;
import com.quickreads.user.repository.QuickReadsUserRepository;
import com.quickreads.user.repository.QuickreadsWelcomeRepository;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class WelcomeService {

	@Autowired
	private QuickreadsWelcomeRepository welcomeRepository;

	@Autowired
	private QuickReadsUserRepository userRepository;

	private static final Long ZERO = 0l;
	private static final String SUCCESS = "SUCCESS";
	private static final String FAILURE = "FAILURE";

	public Welcome welcome() {
		com.quickreads.user.repository.model.Welcome welcomeObj = null;
		Long userCount = null;
		try {
			Optional<com.quickreads.user.repository.model.Welcome> findAnyWelcome = welcomeRepository.findAll().stream()
					.filter(welcome -> welcome.isActive()).findAny();
			if (Objects.nonNull(findAnyWelcome)) {
				welcomeObj = findAnyWelcome.get();
				log.info("The welcome msg :: {}", welcomeObj);
			}
			userCount = userRepository.count();

		} catch (Exception e) {
			log.error("Welcome msg or user count retreival failed : {}", e.getLocalizedMessage());
		}
		if (Objects.nonNull(welcomeObj)) {
			return Welcome.builder().greetingMsg(welcomeObj.getGreetingMsg())
					.userCount((Objects.nonNull(userCount) ? userCount : ZERO))
					.aboutQuickreads(welcomeObj.getAboutQuickreads()).build();
		} else {
			return Welcome.builder().greetingMsg(null).userCount(ZERO).aboutQuickreads(null).build();
		}
	}

	public WelcomeResponse addWelcome(Welcome welcome) {
		String response = FAILURE;
		Optional<com.quickreads.user.repository.model.Welcome> findAnyWelcome = welcomeRepository.findAll().stream()
				.filter(welcomeObj -> welcomeObj.isActive()).findAny();
		try {
			if(findAnyWelcome.isPresent()) {
				com.quickreads.user.repository.model.Welcome activeWelcome = findAnyWelcome.get();
				activeWelcome.setActive(false);
				welcomeRepository.save(activeWelcome);
			}
			welcomeRepository.save(com.quickreads.user.repository.model.Welcome.builder().aboutQuickreads(welcome.getAboutQuickreads())
							.greetingMsg(welcome.getGreetingMsg()).active(true).build());
			
			response = SUCCESS;
		} catch (Exception e) {
			log.error("Failed : {}", e.getLocalizedMessage());
		}
		return WelcomeResponse.builder().status(response).build();
	}

}
