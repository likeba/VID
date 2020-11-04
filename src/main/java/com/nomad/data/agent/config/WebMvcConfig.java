package com.nomad.data.agent.config;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebMvc
@Configuration
@Import(ApplicationConfig.class)
public class WebMvcConfig implements WebMvcConfigurer {

	@Value("${spring.servlet.multipart.max-file-size}")
	private Long maxFileSize;
	
	@Value("${spring.servlet.multipart.max-request-size}")
	private Long maxRequestSize;
	
	@Value("${upload.temp.dir}")
	private String uploadTempDir;
	
	@Bean
	public FilterRegistrationBean<CorsFilter> corsFilter() {
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowCredentials(true);
		config.setAllowedOrigins(Arrays.asList("*"));
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(source));
		bean.setOrder(0);
		
		return bean;
	}
	
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("UTF-8");
		multipartResolver.setMaxUploadSize(maxFileSize);
		multipartResolver.setMaxUploadSizePerFile(maxRequestSize);
		try {
			multipartResolver.setUploadTempDir(new FileSystemResource(uploadTempDir));
		}catch(IOException ioe) {
			log.warn(">>>>> Illegal or not existing dir '" + uploadTempDir + "' (temporary upload directory.)");
			log.error("", ioe);
		}
		return multipartResolver;
	}
	
	@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
        registry.addResourceHandler("/css/**").addResourceLocations("/html/css/");
        registry.addResourceHandler("/js/**").addResourceLocations("/html/js/");
        registry.addResourceHandler("/fonts/**").addResourceLocations("/html/fonts/");
        registry.addResourceHandler("/user/images/**").addResourceLocations("/html/user/images/");
        registry.addResourceHandler("/admin/images/**").addResourceLocations("/html/admin/images/");
        registry.addResourceHandler("/**").addResourceLocations("/html/");
    }
}
