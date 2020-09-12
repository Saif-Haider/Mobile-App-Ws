package com.haider.app.ws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

//	 calling constructor of super class
	public AuthorizationFilter(AuthenticationManager authManager) {
		super(authManager);
	}

//    Its a inbuilt function which we need to override it first it read the header corresponding to 
//    HEADER_STRING then it checks whether they are null or not start with prefix if it doesn't satisfy we continue to next filter 
//    else we go to  getAuthentication here we check the token and verify it with Jwts parser we need to use same
//    TOKEN_SECRET after that if we get a user we return to this function and tehn autherization is done else
//    return null

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		
		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		// here if we get null autherization will fail
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);

		if (token != null) {

			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

			String user = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token).getBody()
					.getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}

			return null;
		}

		return null;
	}

}
