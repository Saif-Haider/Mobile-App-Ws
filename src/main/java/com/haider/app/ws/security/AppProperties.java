package com.haider.app.ws.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// used to read properties from application.properties file
@Component("AppProperties")
public class AppProperties {
	@Autowired
	private Environment environment;

	public String  getTokenSecret() {
		return environment.getProperty("tokenSecret");
	}

	

}
