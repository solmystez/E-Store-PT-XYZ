package com.demo.lookopediaSinarmas.security;

import static com.demo.lookopediaSinarmas.security.SecurityConstants.H2_URL;
import static com.demo.lookopediaSinarmas.security.SecurityConstants.SIGN_UP_URLS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.demo.lookopediaSinarmas.services.otherService.CustomUserDetailService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		//for the future, if want to specify specific method level security like role based
		//this course not do any for that, just for future, do by yourself
		securedEnabled = true,
		jsr250Enabled = true,
		prePostEnabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter{ // = default security config
	//we extends that allow us to customize thos security config method from WebSecurityConfigurerAdapter

	
	@Autowired
	private JwtAuthenticationEntryPoint unauthorizedHandler;
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Bean
	public JwtAuthenticationFilter jwtAuthenthicationFilter() { return new JwtAuthenticationFilter(); }
	
//	authentication manager builder, when login via api
	@Override
	protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		//when user pass the password 
		authenticationManagerBuilder.userDetailsService(customUserDetailService).passwordEncoder(bCryptPasswordEncoder);
	}		
		
	@Override
	@Bean(BeanIds.AUTHENTICATION_MANAGER)
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}
	
	 @Override
	 public void configure(WebSecurity web) throws Exception {
	  //@formatter:off
//	     super.configure(web);
//	     web.httpFirewall(defaultHttpFirewall());
	  web.ignoring().antMatchers("/v2/api-docs",
	                     "/configuration/ui",
	                     "/swagger-resources/**",
	                     "/configuration/security",
	                     "/hystrix.stream",
	                     "/ng-table/**", "/webjars/**", "/bootstrap/**", "/modules/**", "/scripts/**",
	         "/index.html", "/assets/**", "/", "/routes", "/favicon.ico");
	 }

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {//generated from override method 
		//.cors = cross origin request
		http.cors().and().csrf().disable()
			.exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()//in this, we didn't define how we're going to do the ExceptionHandling
			//authenticationEntryPoint = for help handle, what first need to be thrown, when somebody not authenticated
			
			//another default config
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS) //this is rest API, we don't to save session or cookie this time, because we will use JSON WebToken
			//JSON Web Token = server doesn't need hold a session, just wait valid token then server will respons
			//Stateless : no hold state on server, because that's what redux for(hold a state)
			
			.and()
			.headers().frameOptions().sameOrigin() //for some of you still using H-2 Database, need this
			.and()
			.authorizeRequests() //prevent secret run in the background
			.antMatchers(//start specify some route as public
					 "/",
                     "/favicon.ico",
                     "/**/*.png",
                     "/**/*.gif",
                     "/**/*.svg",
                     "/**/*.jpg",
                     "/**/*.html",
                     "/**/*.css",
                     "/**/*.js"			
					).permitAll()  //every end with that,  just PermitAll for the route in spring security, 
			.antMatchers(SIGN_UP_URLS).permitAll()
			.antMatchers("/api/product/loadAllProductOnCatalog").permitAll()
			.antMatchers("/api/**").permitAll()
			.antMatchers("/swagger-ui.html").permitAll()
			.antMatchers("/api/product/findProduct/{product_id}").permitAll()
			.antMatchers(H2_URL).permitAll()
			.anyRequest().authenticated(); //this say, anything other that, need to be authenticated
	
		
		http.addFilterBefore(jwtAuthenthicationFilter(), UsernamePasswordAuthenticationFilter.class);
	}	
	

}
