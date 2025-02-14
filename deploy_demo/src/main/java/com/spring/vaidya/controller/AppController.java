package com.spring.vaidya.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {
	
	
	@GetMapping("/test")
	public String method1() {
		return "Hello we are testing spring deployement";
	}

}
