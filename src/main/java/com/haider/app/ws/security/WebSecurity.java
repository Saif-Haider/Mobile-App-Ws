package com.haider.app.ws.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.haider.app.ws.service.UserService;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userDetailsService;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;

	}

	// "/users" IS PERMITTED TO ALL & ANY OTHER REQUEST NEEDS AUTHENTICATION
	// AuthenticationFilter added so as to authenticate user in sign in
	 // need to replace new AuthenticationFilter(authenticationManager()) with getAuthenticationFilter() for custom URL

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
				.permitAll().anyRequest().authenticated().and()
				.addFilter(getAuthenticationFilter()).addFilter(new AuthorizationFilter(authenticationManager()))
				//application starts communicating with the server side application.
				// There is a sessionthat is created and this session will uniquely 
				// identify  the application.sessions created and this sessions and 
				// cookies they can cache some of the information
				// which can make our authorization had there also cached
				// so we make  API stateless which will prevent a deposition from being cached.
				// by using below 2 lines
				
				.sessionManagement()      // Access sessionManagement                                
		        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);  // Create policy  
		        ;
	}
	// PASSING USERDETAILSSERVICES & THE ENCRYPTION METHOD WE ARE USING
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	// changes the default /login to /users/login
	protected AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
}
