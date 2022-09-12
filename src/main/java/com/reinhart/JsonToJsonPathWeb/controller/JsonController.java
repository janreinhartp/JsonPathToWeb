package com.reinhart.JsonToJsonPathWeb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reinhart.JsonToJsonPathWeb.services.JsonService;

@RestController
@RequestMapping("/")
public class JsonController {

	@Autowired
	JsonService service;

	@GetMapping("/welcome")
	public String WelcomeScreen() {
		return "Welcome to JsonPathWeb";
	}

	@GetMapping("/")
	public String showJsonPathAndValue(@RequestParam String path) {
		String parameter = "$[" + path + "]..*";
		String JsonData = service.readJsonFile();
		return service.htmlBuilder(service.ShowJsonPath(JsonData, parameter), JsonData);

	}

}
