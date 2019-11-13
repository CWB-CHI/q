package com.chi.question.config;


import com.chi.question.interceptor.LoginedInterceptor;
import com.chi.question.interceptor.PassportInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
	@Autowired
	private PassportInterceptor passportInterceptor;
	@Autowired
	private LoginedInterceptor loginedInterceptor;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(passportInterceptor);
		registry.addInterceptor(loginedInterceptor).addPathPatterns("/user/**");
	}
}