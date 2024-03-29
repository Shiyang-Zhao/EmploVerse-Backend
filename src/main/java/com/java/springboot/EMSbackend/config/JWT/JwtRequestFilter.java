package com.java.springboot.EMSbackend.config.JWT;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.java.springboot.EMSbackend.service.userService.UserService;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

	@Value("${server.servlet.context-path}")
	private String contextPath;

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

		if (request.getRequestURI().startsWith(contextPath + "/auth/")) {
			chain.doFilter(request, response);
			return;
		}

		final String jwt = jwtTokenUtil.getJwtFromRequest(request);
		try {
			if (jwt != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				final String username = jwtTokenUtil.getUsernameFromToken(jwt);
				final UserDetails userDetails = userService.loadUserByUsername(username);

				if (jwtTokenUtil.validateToken(jwt, userDetails)) {
					UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authenticationToken);
				}
			}
		} catch (RuntimeException e) {
			logger.warn("JWT Token is invalid", e);
			SecurityContextHolder.clearContext();
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			return;
		}
		chain.doFilter(request, response);
	}
}