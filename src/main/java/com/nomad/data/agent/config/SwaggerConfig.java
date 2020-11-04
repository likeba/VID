package com.nomad.data.agent.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger 설정
 * 
 * @author Nomad Connection
 *
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

	private ApiInfo apiInfo(String title) {
		String contactName  = "Data Agent for DaVinCI Platform Developers";
		String contactUrl   = null;
		String contactEmail = null;
		return new ApiInfoBuilder()
					.title(title)
					.description("Data Agent for DaVinCI Platform REST API Document")
					.version("1.0.0")
					.contact(new Contact(contactName, contactUrl, contactEmail))
					.build();
	}
	
	@Bean
	public Docket commonApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.groupName("Common")
					.apiInfo(this.apiInfo("Common API"))
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.nomad.data.agent.common.controller"))
					.paths(PathSelectors.ant("/v1/common/**"))
					.build();
	}
	
	@Bean
	public Docket libraryApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.groupName("Library")
					.apiInfo(this.apiInfo("Library API"))
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.nomad.data.agent.library.controller"))
					.paths(PathSelectors.ant("v1/library/**"))
					.build();
	}
	
	@Bean
	public Docket datasetApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.groupName("Dataset")
					.apiInfo(this.apiInfo("Dataset API"))
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.nomad.data.agent.dataset.controller"))
					.paths(PathSelectors.ant("/v1/dataset/**"))
					.build();
	}
	
	@Bean
	public Docket datasourceApi() {
		return new Docket(DocumentationType.SWAGGER_2)
					.groupName("DataSource")
					.apiInfo(this.apiInfo("DataSource API"))
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.nomad.data.agent.dataset.controller"))
					.paths(PathSelectors.ant("/v1/datasource/**"))
					.build();
	}
}
