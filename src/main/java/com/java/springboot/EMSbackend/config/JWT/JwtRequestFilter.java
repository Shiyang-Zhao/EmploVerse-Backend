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

		if (jwtToken != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			try {
				String username = jwtTokenUtil.getUsernameFromToken(jwtToken);
				UserDetails userDetails = userService.loadUserByUsername(username);

				if (jwtTokenUtil.validateToken(jwtToken, userDetails) && !jwtTokenUtil.isTokenBlacklisted(jwtToken)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}

			} catch (IllegalArgumentException e) {
				logger.error("Unable to get JWT Token", e);
				// Consider sending an error response
			} catch (ExpiredJwtException e) {
				logger.warn("JWT Token has expired", e);
				// Consider sending an error response
			}
		}

		chain.doFilter(request, response);
	}
}