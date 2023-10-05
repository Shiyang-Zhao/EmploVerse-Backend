package com.java.springboot.EMSbackend.config.JWT;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.springboot.EMSbackend.model.userModel.JwtTokenUtil;
import com.java.springboot.EMSbackend.service.userService.UserService;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;
	private final UserService userService;

	@Autowired
	public JwtRequestFilter(JwtTokenUtil jwtTokenUtil, UserService userService) {
		this.jwtTokenUtil = jwtTokenUtil;
		this.userService = userService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String jwtToken = jwtTokenUtil.getJwtFromRequest(request);

		if (jwtToken != null) {
			try {
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

				// Ensure that there isn't already an authentication (session) established
				if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

					UserDetails userDetails = userService.loadUserByUsername(username);

					// Validate the token with the corresponding user details
					if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

						UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
						usernamePasswordAuthenticationToken
								.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
						// After setting the Authentication in the context, we specify
						// that the current user is authenticated. So it passes the Spring Security
						// Configurations successfully.
						SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
					}
				}
			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token", e);
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired", e);
			}

			jwtTokenUtil.removeExpiredTokensFromBlacklist();
		}

		chain.doFilter(request, response);
	}
}