package br.com.ldr.devshe.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import br.com.ldr.devshe.services.SecurityService;

@Component
public class JWTFilter extends GenericFilterBean implements ApplicationContextAware {

	private static final String TOKEN = "Authorization";
	
	
	
	private ApplicationContext applicationContext;
	
	private SecurityService getSecurityService() {
		return applicationContext.getBean(SecurityService.class);
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String token = httpRequest.getHeader(TOKEN);
		if (token != null) {
			JWTAuthentication auth = getSecurityService().parseToken(token);
			if (auth != null && auth.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(auth);
			}
		}
		
		chain.doFilter(request, response);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	
}
