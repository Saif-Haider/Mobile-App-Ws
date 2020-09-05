package com.haider.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haider.app.ws.ui.model.request.UserLoginRequestModel;

// USED FOR AUTHENTICATION OF USER DURING SIGN IN
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
	
	
	public Authentication attemptAuthentication(HttpServletRequest req,
			HttpServletResponse res) throws AuthenticationException {
	    try {
	    	UserLoginRequestModel creds = new ObjectMapper()
	    			                       .readValue(req.getInputStream(), UserLoginRequestModel.class);
	    	
	    	return authenticationManager.authenticate(
	    			new UsernamePasswordAuthenticationToken(
	    					creds.getEmail(), 
	    					creds.getPassword(), 
	    					new ArrayList<>()));
	    	
	    	
	    }catch (IOException e) {
	    	throw new RuntimeException(e);
	    }
	}


	
}
