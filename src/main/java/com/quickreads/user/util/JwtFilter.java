package com.quickreads.user.util;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.quickreads.user.service.QuickreadsUserDetailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter{
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private QuickreadsUserDetailService service;
	
	private static final String AUTHORIZATION = "Authorization";
	private static final String BEARER = "Bearer ";
	private static final String EMPTY = "";

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authorization = request.getHeader(AUTHORIZATION);
		String token = null;
		String userName = null;
		if(authorization!=null && authorization.startsWith(BEARER)) {
			token = authorization.replace(BEARER, EMPTY);
			log.info("jwt : ", token);
			userName = jwtUtil.getUsernameFromToken(token);
		}
		if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = service.loadUserByUsername(userName);
			if(jwtUtil.validateToken(token, userDetails)) {
				UsernamePasswordAuthenticationToken passwordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				passwordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(passwordAuthenticationToken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
