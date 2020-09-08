package com.haider.app.ws.security;

import java.io.IOException;
import org.springframework.security.core.userdetails.User;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.haider.app.ws.SpringApplicationContext;
import com.haider.app.ws.service.UserService;
import com.haider.app.ws.shared.dto.UserDto;
import com.haider.app.ws.ui.model.request.UserLoginRequestModel;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// USED FOR AUTHENTICATION OF USER DURING SIGN IN
public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	
	// USER SIGNIN UserLoginRequestModel IS CREATED FROM REQUEST & GO TO successfulAuthentication IF
	// AUTHENTICATION IS SUCCESS FULL USER ARE AUTHENTICATED USING  loadUserByUsername IN USERSERVICE IMPL
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res)
			throws AuthenticationException {
		try {
			UserLoginRequestModel creds = new ObjectMapper().readValue(req.getInputStream(),
					UserLoginRequestModel.class);

			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(), creds.getPassword(), new ArrayList<>()));

		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	// since  AUTHENTICATON FILTER IS NOT A BEAN WE CANNOT AUTOWIRE IT NOT AUTOWIRE TO IT SO WE THIS 
	// APPLICATION CONTEXT SO WE NEED TO CRETE NEW INSTANCE OF IT MANUALLY
	
	@Override
	protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse res, FilterChain chain,
			Authentication auth) throws IOException, ServletException {

		String userName = ((User) auth.getPrincipal()).getUsername();

		String token = Jwts.builder().setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.TOKEN_SECRET).compact();
        
		UserService userService =(UserService)SpringApplicationContext.getBean("userServiceImpl"); 
		UserDto userDto = userService.getUser(userName);
		
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		res.addHeader("UserId", userDto.getUserId());

	}

}
