package com.quickreads.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//Delete later
@RestController
@RequestMapping("/v1")
public class WelcomeController {

	@GetMapping(value = "/hello")
	public String hello() {
		return "hello world";
	}
}
