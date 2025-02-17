package com.bia.dev_bank.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class HelloWorld {

	@GetMapping("/")
	public String helloWorld() {
		return "Hello World!";
	}

}
