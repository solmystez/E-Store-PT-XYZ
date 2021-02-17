package com.demo.eStore.swagger;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.SpringVersion;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2UiConfiguration extends WebMvcConfigurerAdapter  {

	private ApiInfo metadata() {
		return new ApiInfoBuilder()//
	        .title("Sinarmas Project mini e-commerce")//
	        .description("Spring Boot 2.1.3")
//	        .version("1.0.0")//
//	        .license("MIT License").licenseUrl("http://opensource.org/licenses/MIT")//
//	        .contact(new Contact(null, null, "test@gmail.com"))//
	        .build();
	}

	@Bean
	public Docket api() {
		// @formatter:off
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.any())
				.apis(Predicates.not(RequestHandlerSelectors.basePackage("org.springframework.boot")))
				.paths(PathSelectors.any())
				//.paths(PathSelectors.ant("/swagger2-demo"))
				.build()
		        .apiInfo(metadata())
				.securityContexts(Collections.singletonList(securityContext()))
				.securitySchemes(Arrays.asList(apiKey()));
		// @formatter:on
	}

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addRedirectViewController("/documentation/v2/api-docs", "/v2/api-docs").setKeepQueryParams(true);
//		registry.addRedirectViewController("/documentation/swagger-resources/configuration/ui", "/swagger-resources/configuration/ui");
//		registry.addRedirectViewController("/documentation/swagger-resources/configuration/security", "/swagger-resources/configuration/security");
//		registry.addRedirectViewController("/documentation/swagger-resources", "/swagger-resources");
//	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}

	private ApiKey apiKey() {
		//Swagger 2.8.0++
	    return new ApiKey("Bearer", "Authorization", "header");

		//Swagger 2.6.0
//		return new ApiKey("Authorization", "Bearer", "header");
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder()
		        .securityReferences(defaultAuth())
		        .forPaths(PathSelectors.regex("/.*"))
		        .build();
//	    return SecurityContext.builder()
//	      .securityReferences(
//	        Arrays.asList(new SecurityReference("spring_oauth", scopes())))
//	      .forPaths(PathSelectors.regex("/foos.*"))
//	      .build();
	}
	
	private List<SecurityReference> defaultAuth() {
		final AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		final AuthorizationScope[] authorizationScopes = new AuthorizationScope[]{authorizationScope};
		return Collections.singletonList(new SecurityReference("Bearer", authorizationScopes));
	}
}